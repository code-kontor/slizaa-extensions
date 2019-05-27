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
package io.codekontor.slizaa.neo4j.apoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaImportExportProcedures {

  @Context
  public GraphDatabaseService _databaseService;

  // This gives us a log instance that outputs messages to the
  // standard log, `neo4j.log`
  @Context
  public Log                  log;

  @Procedure(name = "slizaa.exportDatabase", mode = Mode.READ)
  public void exportDatabase(@Name("file") String filePath) throws IOException {

    try {
      //
      Path targetPath = Paths.get(filePath);
      targetPath.toFile().getParentFile().mkdirs();

      //
      List<String> nodes = _databaseService.getAllNodes().stream()
          .map(node -> node.getId() + ":" + mapLabels(node.getLabels()) + ":" + node.getAllProperties())
          .collect(Collectors.toList());

      //
      List<String> relationShips = _databaseService.getAllRelationships().stream()
          .map(relationShip -> relationShip.getId() + ":" + relationShip.getStartNodeId() + ":"
              + relationShip.getEndNodeId() + ":" + mapType(relationShip.getType()) + ":"
              + (relationShip.getAllProperties().isEmpty() ? "" : relationShip.getAllProperties()))
          .collect(Collectors.toList());

      //
      Files.write(targetPath, Stream.concat(nodes.stream(), relationShips.stream()).collect(Collectors.toList()));

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private String mapType(RelationshipType type) {
    return "12";
  }

  private String mapLabels(Iterable<Label> labels) {
    StringBuilder builder = new StringBuilder();
    Iterator<Label> iterator = labels.iterator();
    while (iterator.hasNext()) {
      Label label = (Label) iterator.next();
      builder.append("12");
      if (iterator.hasNext()) {
        builder.append(",");
      }
    }
    return builder.toString();
  }

  public class Output {
    public Node out;
  }
}