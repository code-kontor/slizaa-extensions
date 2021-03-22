/**
 * slizaa-extensions-neo4j-graphdbfactory - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.neo4j.graphdbfactory.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.neo4j.configuration.GraphDatabaseInternalSettings;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.connectors.BoltConnector.EncryptionLevel;
import org.neo4j.configuration.connectors.HttpConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.api.procedure.GlobalProcedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactory implements IGraphDbFactory {

  private final Supplier<List<Class<?>>> _databaseExtensionsSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   */
  public GraphDbFactory() {
    this(null);
  }

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   * @param databaseExtensionsSupplier
   */
  public GraphDbFactory(Supplier<List<Class<?>>> databaseExtensionsSupplier) {
    this._databaseExtensionsSupplier = databaseExtensionsSupplier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDbBuilder newGraphDb(int port, File storeDir) {
    return new GraphDbBuilder(port, storeDir, this._databaseExtensionsSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDbBuilder newGraphDb(File databaseDir) {
    return new GraphDbBuilder(-1, databaseDir, this._databaseExtensionsSupplier);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class GraphDbBuilder implements IGraphDbBuilder {

    /** - */
    private final int                      _port;

    /** - */
    private final File                     _storeDir;

    /** - */
    private Object                         _userObject;

    /** - */
    private final Map<String, Object>      _configuration = new HashMap<>();

    /** - */
    private final Supplier<List<Class<?>>> _databaseExtensionsSupplier;

    /**
     * <p>
     * Creates a new instance of type {@link GraphDbBuilder}.
     * </p>
     *
     * @param port
     * @param storeDir
     */
    public GraphDbBuilder(int port, File storeDir, Supplier<List<Class<?>>> databaseExtensionsSupplier) {
      this._port = port;
      this._storeDir = checkNotNull(storeDir);
      this._databaseExtensionsSupplier = databaseExtensionsSupplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> IGraphDbBuilder withUserObject(T userObject) {

      //
      this._userObject = userObject;

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDbBuilder withConfiguration(String key, Object value) {

      //
      this._configuration.put(key, value);

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDb create() {

      DatabaseManagementServiceBuilder databaseManagementServiceBuilder = new DatabaseManagementServiceBuilder(
          this._storeDir.toPath());

      if (this._port != -1) {
        databaseManagementServiceBuilder.setConfig(BoltConnector.enabled, true)
            .setConfig(BoltConnector.listen_address, new SocketAddress(this._port))
            .setConfig(BoltConnector.encryption_level, EncryptionLevel.DISABLED)
            .setConfig(HttpConnector.enabled, false);
      }

      // configure slf4j logging
      databaseManagementServiceBuilder.setUserLogProvider(new Slf4jLogProvider());

      DatabaseManagementService managementService = databaseManagementServiceBuilder.build();
      GraphDatabaseService graphDatabase = managementService.database("neo4j");

      //
      registerDatabaseExtensions(graphDatabase);

      // the Neo4jGraphDb
      return new Neo4jGraphDb(managementService, graphDatabase, this._port, this._userObject);
    }

    /**
     * <p>
     * </p>
     *
     * @param graphDatabase
     */
    private void registerDatabaseExtensions(GraphDatabaseService graphDatabase) {

      //
      List<Class<?>> extensionsToRegister = new LinkedList<>();

      // step 1: _databaseExtensionsSupplier
      if (this._databaseExtensionsSupplier != null) {

        // extract the classes...
        List<Class<?>> classes = this._databaseExtensionsSupplier.get();

        // ...and add them extension list
        if (classes != null) {
          extensionsToRegister.addAll(classes);
        }
      }

      // step 2: apoc classess
      extensionsToRegister.addAll(apocListClasses());

      //
      for (Class<?> clazz : extensionsToRegister) {
        System.out.println("Register extension class: " + clazz.getName());
      }

      // get the procedure service
      GlobalProcedures globalProcedures = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
          .resolveDependency(GlobalProcedures.class);

      // register all elements
      for (Class<?> element : extensionsToRegister) {
        try {
          globalProcedures.registerFunction(element);
          globalProcedures.registerProcedure(element);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private Collection<? extends Class<?>> apocListClasses() {

      //
      List<Class<?>> result = new ArrayList<Class<?>>();

      //
      ClassLoader classLoader = this.getClass().getClassLoader();

      //
      try {

        //
        Enumeration<URL> apocLists = classLoader.getResources("apoc.list");

        if (apocLists != null) {

          //
          while (apocLists.hasMoreElements()) {

            URL url = apocLists.nextElement();

            //
            try (InputStream stream = url.openStream()) {

              //
              List<Class<?>> classesList = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                  .lines().map(className -> {
                    try {
                      return classLoader.loadClass(className);
                    } catch (Exception e) {
                      return null;
                    }
                  }).filter(v -> v != null).collect(Collectors.toList());

              //
              result.addAll(classesList);
            }
          }
        }
      }

      //
      catch (IOException e) {
        // TODO
        e.printStackTrace();
      }

      return result;
    }
  }
}
