/**
 * slizaa-extensions-jtype-scanner-apoc - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.scanner.apoc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelElementType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelRelationshipType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MissingTypesCreator {

  // the Label 'MissingType'
  private final Label LABEL_MISSING_TYPE = Label.label("MissingType");

  // the Label 'Package'
  private final Label LABEL_PACKAGE = Label.label("Package");

  // the Label 'Directory'
  private final Label LABEL_DIRECTORY = Label.label("Directory");

  // the contains relationship
  private final RelationshipType REL_CONTAINS = RelationshipType
          .withName(CoreModelRelationshipType.CONTAINS.name());

  /** - */
  private GraphDatabaseService       _graphDatabaseService;

  /** - */
  private LoadingCache<String, Node> _virtualPackagesCache;

  /** - */
  private LoadingCache<String, Node> _virtualTypesCache;

  /** - */
  private Node                       _missingTypeModuleNode;

  /**
   * <p>
   * Creates a new instance of type {@link MissingTypesCreator}.
   * </p>
   *
   * @param graphDatabaseService
   */
  public MissingTypesCreator(GraphDatabaseService graphDatabaseService) {

    //
    this._graphDatabaseService = checkNotNull(graphDatabaseService);

    //
    this._missingTypeModuleNode = this._graphDatabaseService
        .createNode(Label.label(CoreModelElementType.Module.name()));
    this._missingTypeModuleNode.setProperty("fqn", "<<Missing Types>>");
    this._missingTypeModuleNode.setProperty("name", "<<Missing Types>>");

    //
    this._virtualPackagesCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Node>() {
      @Override
      public Node load(String packageName) {
        return createVirtualPackage(packageName);
      }
    });

    //
    this._virtualTypesCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Node>() {
      @Override
      public Node load(String typeName) {
        return createVirtualType(typeName);
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Node getOrCreateVirtualType(String typeName) {
    return this._virtualTypesCache.getUnchecked(typeName);
  }

  /**
   * <p>
   * </p>
   *
   * @param packageName
   * @return
   */
  private Node createVirtualPackage(String packageName) {

    //
    Node parentNode = null;

    //
    int index = packageName.lastIndexOf('.');
    String simplePackageName = packageName;
    if (index != -1) {
      simplePackageName = packageName.substring(index + 1);
      String parentPackageName = packageName.substring(0, index);
      parentNode = MissingTypesCreator.this._virtualPackagesCache.getUnchecked(parentPackageName);
    }

    //
    Map<String, String> properties = new HashMap<>();
    properties.put("fqn", packageName);
    properties.put("name", simplePackageName);

    Node packageNode = createNode(properties, this.LABEL_PACKAGE, this.LABEL_DIRECTORY);
    if (parentNode != null) {
      parentNode.createRelationshipTo(packageNode, this.REL_CONTAINS);
    }
    this._missingTypeModuleNode.createRelationshipTo(packageNode, this.REL_CONTAINS);

    //
    return packageNode;
  }

  /**
   * <p>
   * </p>
   *
   * @param typeName
   * @return
   */
  private Node createVirtualType(String typeName) {

    //
    Node parentNode = null;

    //
    int index = typeName.lastIndexOf('.');
    String simpleTypeName = typeName;
    if (index != -1) {
      simpleTypeName = typeName.substring(index + 1);
      parentNode = MissingTypesCreator.this._virtualPackagesCache.getUnchecked(typeName.substring(0, index));
    }

    //
    Map<String, String> properties = new HashMap<>();
    properties.put("fqn", typeName);
    properties.put("name", simpleTypeName);

    Node typeNode = createNode(properties, this.LABEL_MISSING_TYPE);
    if (parentNode != null) {
      parentNode.createRelationshipTo(typeNode, this.REL_CONTAINS);
    }
    this._missingTypeModuleNode.createRelationshipTo(typeNode, this.REL_CONTAINS);

    //
    return typeNode;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private Node createNode(Map<String, String> properties, Label... labels) {

    //
    Node node = this._graphDatabaseService.createNode(labels);

    //
    for (Entry<String, String> entry : properties.entrySet()) {
      node.setProperty(entry.getKey(), entry.getValue());
    }

    //
    return node;
  }
}
