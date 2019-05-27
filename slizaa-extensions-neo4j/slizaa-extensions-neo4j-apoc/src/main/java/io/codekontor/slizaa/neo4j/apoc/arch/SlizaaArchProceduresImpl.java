/**
 * slizaa-extensions-neo4j-apoc - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.apoc.arch;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.procedure.Name;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelElementType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelRelationshipType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.IGroupNode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.IModuleNode;

import com.google.common.collect.ImmutableMap;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaArchProceduresImpl {

  /** - */
  private static Label            GROUP_LABEL                    = Label.label(CoreModelElementType.Group.name());

  /** - */
  private static Label            MODULE_LABEL                   = Label.label(CoreModelElementType.Module.name());

  /** - */
  private static Label            RESOURCE_LABEL                 = Label.label(CoreModelElementType.Resource.name());

  /** - */
  private static String           CONTAINED_PACKAGE_CYPHER_QUERY = "MATCH (m:Module)-[:CONTAINS]->(p:Package {fqn: $package_fqn}) WHERE id(m) = $module_id RETURN m,p";

  /** - */
  private static RelationshipType CONTAINS_RELATIONSHIP          = RelationshipType
      .withName(CoreModelRelationshipType.CONTAINS.name());

  /**
   * <p>
   * </p>
   *
   * @param db
   *          the database
   * @param module
   *          the module description
   */
  public static Node createModule(GraphDatabaseService db, FullyQualifiedName fqn, String version) {

    // check the module node
    Result result = checkNotNull(db).execute("Match (m:Module {fqn:'$fqn', version:'$version'}) return m",
        ImmutableMap.of("fqn", fqn.toString(), "version", version));

    if (result.hasNext()) {
      return (Node) result.next().get("m");
    }

    // TODO: check properties

    Node parentGroup = fqn.hasParent() ? createGroup(db, fqn.getParent()) : null;

    // create the new node
    Node node = checkNotNull(db).createNode();

    //
    node.addLabel(MODULE_LABEL);

    // set the properties
    node.setProperty(IModuleNode.FQN, fqn.getSimpleName());
    node.setProperty(IModuleNode.PROPERTY_MODULE_NAME, fqn.getSimpleName());
    node.setProperty(IModuleNode.PROPERTY_MODULE_VERSION, version);

    // relink parent
    if (parentGroup != null) {
      parentGroup.createRelationshipTo(node, CONTAINS_RELATIONSHIP);
    }

    // return the newly created node
    return node;
  }

  /**
   * <p>
   * </p>
   *
   * @param db
   * @param fqn
   * @return
   */
  public static Node createGroup(GraphDatabaseService db, FullyQualifiedName fqn) {

    // check the group node
    Node group = checkNotNull(db).findNode(GROUP_LABEL, IGroupNode.FQN, checkNotNull(fqn).toString());
    if (group != null) {
      return group;
    }

    // create the parent group
    Node parentGroup = null;
    if (fqn.hasParent()) {
      parentGroup = createGroup(db, fqn.getParent());
    }

    // create the new node
    Node node = checkNotNull(db).createNode();

    // set the labels
    node.addLabel(Label.label(CoreModelElementType.Group.name()));

    // set the properties
    node.setProperty(IGroupNode.FQN, fqn.getFullyQualifiedName());
    node.setProperty(IGroupNode.NAME, fqn.getSimpleName());

    // relink parent
    if (parentGroup != null) {
      parentGroup.createRelationshipTo(node, CONTAINS_RELATIONSHIP);
    }

    // return the newly created node
    return node;
  }

  /**
   * <p>
   * </p>
   *
   * @param module
   * @param group
   */
  public static void moveModule(@Name("module") Node module, @Name("group") Node group) {

    //
    checkState(checkNotNull(module).hasLabel(MODULE_LABEL));
    checkState(checkNotNull(group).hasLabel(GROUP_LABEL));

    // delete existing parent relations...
    module.getRelationships(Direction.INCOMING, CONTAINS_RELATIONSHIP).forEach(rel -> rel.delete());

    // ...and add the new one
    group.createRelationshipTo(module, CONTAINS_RELATIONSHIP);
    module.setProperty(INode.FQN,
        group.getProperty(INode.FQN) + "/" + module.getProperty(IModuleNode.PROPERTY_MODULE_NAME) + "_"
            + module.getProperty(IModuleNode.PROPERTY_MODULE_VERSION));
  }

  /**
   * <p>
   * </p>
   *
   * @param resource
   * @param module
   */
  public static void moveResource(Node resource, Node module) {

    //
    checkState(checkNotNull(resource).hasLabel(RESOURCE_LABEL));
    checkState(checkNotNull(module).hasLabel(MODULE_LABEL));

    //
    FullyQualifiedName fullyQualifiedName = new FullyQualifiedName((String) resource.getProperty(INode.FQN));
    Node parentPackage = getOrCreatePackage(module, fullyQualifiedName.getParent());

    //
    System.out.println("moveResource: " + parentPackage);
  }

  /**
   * <p>
   * </p>
   *
   * @param group
   */
  public static void deleteGroup(@Name("group") Node group) {

    checkState(checkNotNull(group).hasLabel(GROUP_LABEL));

    // delete existing relations...
    group.getRelationships().forEach(rel -> rel.delete());

    // ...delete the node
    group.delete();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static Node getOrCreatePackage(Node module, FullyQualifiedName path) {

    checkNotNull(module);
    checkNotNull(path);

    //
    Map<String, Object> parameters = ImmutableMap.of("package_fqn", path.getFullyQualifiedName(), "module_id", module.getId());
    Result result = db(module).execute(CONTAINED_PACKAGE_CYPHER_QUERY, parameters);

    //
    if (!result.hasNext()) {

      //
      Node resultNode = db(module).createNode();
      
      if (path.hasParent()) {
        Node parent = getOrCreatePackage(module, path.getParent());
      }
    }
    
    //
    System.out.println(result);
    
    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  private static GraphDatabaseService db(Node node) {
    return checkNotNull(node).getGraphDatabase();
  }
}
