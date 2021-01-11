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

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.AbstractQueryBasedDependencyProvider;

public class JType_DependencyProvider extends AbstractQueryBasedDependencyProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initialize() {

    // @formatter:off
    addProxyDependencyDefinitions(
        new String[] {
            "Match (t1:Type)-[r:DEPENDS_ON]->(tref:TypeReference)-[:BOUND_TO]->(t2:Type) RETURN id(t1), id(t2), id(r), 'DEPENDS_ON'",
            "Match (t1:Type)-[r:DEPENDS_ON]->(tref:TypeReference)-[:BOUND_TO]->(t2:MissingType) RETURN id(t1), id(t2), id(r), 'DEPENDS_ON'"
        },
        new String[] {
            "MATCH (n1)-[rel]->(ref)-[:BOUND_TO]->(n2) "
            + "WHERE id(n1) in $from AND id(n2) in $to "
            + "AND ("
            + "(n1:Type)-[rel:EXTENDS|IMPLEMENTS]->(ref:TypeReference) "
            + "OR (n1:Type)-[rel:ANNOTATED_BY]->(ref:TypeReference)"
            + "OR (n1:Type)-[rel:REFERENCES]->(ref:TypeReference) "
            + "OR (n1:Type)-[rel:DEFINES_INNER_CLASS]->(ref:TypeReference)"
            + "OR (n1:Type)-[rel:IS_INNER_CLASS_DEFINED_BY]->(ref:TypeReference)"
            + "OR (n1:Field)-[rel:IS_OF_TYPE]->(ref:TypeReference) "
            + "OR (n1:Field)-[rel:ANNOTATED_BY]->(ref:TypeReference) "
            + "OR (n1:Field)-[rel:REFERENCES]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:INVOKES]->(ref:MethodReference) "
            + "OR (n1:Method)-[rel:READS|WRITES]->(ref:FieldReference) "
            + "OR (n1:Method)-[rel:READS_FIELD_OF_TYPE|WRITES_FIELD_OF_TYPE]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:THROWS]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:RETURNS]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:HAS_PARAMETER]->(ref:TypeReference)"
            + "OR (n1:Method)-[rel:DEFINES_LOCAL_VARIABLE]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:USES_TYPE_CONSTANT]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:ANNOTATED_BY]->(ref:TypeReference) "
            // + "OR (n1:Method)-[rel:INVOKES_METHOD_FROM]->(ref:TypeReference) "
            // + "OR (n1:Method)-[rel:INVOKED_METHOD_RETURNS]->(ref:TypeReference) "
            // + "OR (n1:Method)-[rel:INVOKED_METHOD_HAS_PARAMETER]->(ref:TypeReference) "
            + "OR (n1:Method)-[rel:REFERENCES]->(ref:TypeReference) "
            + ") "
            + "RETURN id(n1), id(n2), id(rel), type(rel)"
        });
    // @formatter:on
  }
}
