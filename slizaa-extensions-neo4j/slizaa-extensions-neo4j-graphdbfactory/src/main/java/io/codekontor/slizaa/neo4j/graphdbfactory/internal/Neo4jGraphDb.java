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

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.graphdb.GraphDatabaseService;

import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Neo4jGraphDb implements IGraphDb {

  /** - */
  private Object                    _userObject;

  /** - */
  private GraphDatabaseService      _databaseService;

  /** - */
  private DatabaseManagementService _databaseManagementService;

  /** - */
  private int                       _port;

  /** - */
  private boolean                   _isInShutdown = false;

  /** - */
  private final Thread              shutdownHook  = new Thread(() -> {
                                                    _isInShutdown = true;
                                                    shutdown();
                                                  });

  /**
   * <p>
   * Creates a new instance of type {@link Neo4jGraphDb}.
   * </p>
   *
   * @param databaseService
   * @param port
   * @param userObject
   */
  public Neo4jGraphDb(DatabaseManagementService databaseManagementService, GraphDatabaseService databaseService,
      int port, Object userObject) {
    this._databaseService = checkNotNull(databaseService);
    this._databaseManagementService = checkNotNull(databaseManagementService);
    this._port = port;
    this._userObject = userObject;

    Runtime.getRuntime().addShutdownHook(shutdownHook);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getUserObject(Class<T> type) {

    //
    if (this._userObject != null && checkNotNull(type).isAssignableFrom(this._userObject.getClass())) {
      return (T) this._userObject;
    }

    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasUserObject(Class<T> userObject) {
    return getUserObject(userObject) != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this._port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shutdown() {
    this._databaseManagementService.shutdown();
    if (!_isInShutdown) {
      Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }
  }

  @Override
  public void close() throws Exception {
    shutdown();
  }

  public GraphDatabaseService getDatabaseService() {
    return this._databaseService;
  }
}
