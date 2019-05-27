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

import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

import apoc.result.RelationshipResult;

public class JTypeProcedures {

  /** - */
  @Context
  public GraphDatabaseService _databaseService;

  @Procedure(name = "slizaa.jtype.createMissingTypes", mode = Mode.WRITE)
  public Stream<RelationshipResult> test() {

    //
    final MissingTypesCreator virtualPackageCreator = new MissingTypesCreator(this._databaseService);

    //
    Result result = this._databaseService
        .execute("MATCH (tref:TypeReference) WHERE NOT (tref)-[:BOUND_TO]->(:Type) RETURN tref, tref.fqn");

    //
    result.forEachRemaining(map -> {

      //
      String fullyQualifiedPackageName = map.get("tref.fqn").toString();

      //
      Node typeReferenceNode = (Node) map.get("tref");

      // byte, short, int, long, float, double, char, and boolean
      if (fullyQualifiedPackageName.equals("byte") || fullyQualifiedPackageName.equals("short")
          || fullyQualifiedPackageName.equals("int") || fullyQualifiedPackageName.equals("long")
          || fullyQualifiedPackageName.equals("float") || fullyQualifiedPackageName.equals("double")
          || fullyQualifiedPackageName.equals("char") || fullyQualifiedPackageName.equals("boolean")) {

        //
        throw new RuntimeException("" + typeReferenceNode.getAllProperties());
      }

      //
      Node virtualTypeNode = virtualPackageCreator.getOrCreateVirtualType(fullyQualifiedPackageName);

      //
      typeReferenceNode.createRelationshipTo(virtualTypeNode, RelationshipType.withName("BOUND_TO"));
    });

    //
    return Stream.of();
  }
}
