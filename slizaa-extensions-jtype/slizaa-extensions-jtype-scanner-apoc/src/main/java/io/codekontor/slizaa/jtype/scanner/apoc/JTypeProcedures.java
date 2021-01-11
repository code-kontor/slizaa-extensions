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
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

public class JTypeProcedures {

  /** - */
  @Context
  public GraphDatabaseService _databaseService;

  @Procedure(name = "slizaa.jtype.createMissingTypes", mode = Mode.WRITE)
  public Stream<CreateMissingTypesResult> createMissingTypes() {

    try (Transaction transaction = this._databaseService.beginTx()) {

      final MissingTypesCreator virtualPackageCreator = new MissingTypesCreator(transaction);

      Result result = transaction
          .execute("MATCH (tref:TypeReference) WHERE NOT (tref)-[:BOUND_TO]->(:Type) RETURN tref, tref.fqn");

      result.forEachRemaining(map -> {

        // get TypeReference and fqn
        String typeReferenceFullyQualifiedName = map.get("tref.fqn").toString();
        Node typeReferenceNode = (Node) map.get("tref");

        // byte, short, int, long, float, double, char, and boolean
        if (typeReferenceFullyQualifiedName.equals("byte") || typeReferenceFullyQualifiedName.equals("short")
            || typeReferenceFullyQualifiedName.equals("int") || typeReferenceFullyQualifiedName.equals("long")
            || typeReferenceFullyQualifiedName.equals("float") || typeReferenceFullyQualifiedName.equals("double")
            || typeReferenceFullyQualifiedName.equals("char") || typeReferenceFullyQualifiedName.equals("boolean")) {

          //
          throw new RuntimeException("" + typeReferenceNode.getAllProperties());
        }

        //

        try {
          Node virtualTypeNode = virtualPackageCreator.getOrCreateVirtualType(typeReferenceFullyQualifiedName);
          typeReferenceNode.createRelationshipTo(virtualTypeNode, RelationshipType.withName("BOUND_TO"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      transaction.commit();
      
      // TODO
      return Stream.of(new CreateMissingTypesResult(1));
    }
  }

  public static class CreateMissingTypesResult {
    public Long count;

    public CreateMissingTypesResult(long count) {
      this.count = count;
    }
    
  }
}
