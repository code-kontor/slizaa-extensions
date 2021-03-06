/**
 * slizaa-extensions-jtype-hierarchicalgraph - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.hierarchicalgraph;

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.AbstractQueryBasedHierarchyProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JType_HierarchyProvider extends AbstractQueryBasedHierarchyProvider {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  protected String[] toplevelNodeIdQueries() {
    return new String[] { "MATCH (g:Group) WHERE NOT ()-[:CONTAINS]->(g) RETURN id(g)", 
        "MATCH (m:Module) WHERE NOT ()-[:CONTAINS]->(m) RETURN id(m)" };
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  protected String[] parentChildNodeIdsQueries() {
    return new String[] {
    	"MATCH (g1:Group)-[:CONTAINS]->(g2:Group) RETURN id(g1), id(g2)", // groups -> groups
        "MATCH (g:Group)-[:CONTAINS]->(m:Module) RETURN id(g), id(m)", // groups -> modules
        "MATCH (module:Module)-[:CONTAINS]->(d:Directory) WHERE d.isEmpty=false RETURN DISTINCT id(module), id(d)", // module -> directory
        "MATCH (module:Module)-[:CONTAINS]->(d:Directory)-[:CONTAINS]->(mt:MissingType) RETURN DISTINCT id(module), id(d)", // module -> directory (Missing types)
        "MATCH (d:Directory)-[:CONTAINS]->(mt:MissingType) RETURN id(d), id(mt)", // directory -> missingType
        "MATCH (d:Directory)-[:CONTAINS]->(r:Resource)-[:CONTAINS]->(t:Type) WHERE NOT EXISTS(t.innerClass) RETURN id(d), id(r)", // directory -> resource
        "MATCH (r:Resource)-[:CONTAINS]->(t:Type) WHERE NOT EXISTS(t.innerClass) RETURN id(r), id(t)", // resource -> type
        "MATCH (t:Type)-[rel:IS_INNER_CLASS_DEFINED_BY]->(tref:TypeReference)-[:BOUND_TO]->(t2:Type) return id(t2), id(t)", // type -> inner type
        "MATCH (t:Type)-[:CONTAINS]->(m:Method) RETURN id(t), id(m)", // type -> methods
        "MATCH (t:Type)-[:CONTAINS]->(f:Field) RETURN id(t), id(f)" // type -> fields
    };
  }
}
