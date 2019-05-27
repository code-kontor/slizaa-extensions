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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
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
public class SlizaaArchProcedures {

  @Context
  public GraphDatabaseService db;

  /** log instance to the standard log, `neo4j.log` */
  @Context
  public Log                  log;

  /**
   * <p>
   * </p>
   *
   * @param fqn
   * @param version
   * @return
   */
  @Procedure(name = "slizaa.arch.createModule", mode = Mode.WRITE)
  public Stream<Output> createModule(@Name("fqn") String fqn, @Name("version") String version) {
    List<Output> result = new LinkedList<>();
    result.add(new Output(SlizaaArchProceduresImpl.createModule(db, new FullyQualifiedName(checkNotNull(fqn)), version)));
    return result.stream();
  }

  /**
   * <p>
   * </p>
   *
   * @param groupFqn
   * @return
   */
  @Procedure(name = "slizaa.arch.createGroup", mode = Mode.WRITE)
  public Stream<Output> createGroup(@Name("groupFqn") String groupFqn) {
    List<Output> result = new LinkedList<>();
    result.add(new Output(SlizaaArchProceduresImpl.createGroup(db, new FullyQualifiedName(groupFqn))));
    return result.stream();
  }

  /**
   * <p>
   * </p>
   *
   * @param group
   */
  @Procedure(name = "slizaa.arch.deleteGroup", mode = Mode.WRITE)
  public void deleteGroup(@Name("module") Node group) {
    SlizaaArchProceduresImpl.deleteGroup(group);
  }

  /**
   * <p>
   * </p>
   *
   * @param module
   * @param group
   */
  @Procedure(name = "slizaa.arch.moveModule", mode = Mode.WRITE)
  public void moveModule(@Name("module") Node module, @Name("group") Node group) {
    SlizaaArchProceduresImpl.moveModule(module, group);
  }

  @Procedure(name = "slizaa.arch.moveResource", mode = Mode.WRITE)
  public void moveResource(@Name("resource") Node resource, @Name("Module") Node module) {
    SlizaaArchProceduresImpl.moveResource(resource, module);
  }
  
  public class Output {
    public Node node;

    public Output(Node node) {
      this.node = node;
    }
  }

}