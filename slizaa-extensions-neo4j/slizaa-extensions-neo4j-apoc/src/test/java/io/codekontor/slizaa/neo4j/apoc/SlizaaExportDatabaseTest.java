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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.harness.junit.Neo4jRule;
import io.codekontor.slizaa.neo4j.apoc.SlizaaImportExportProcedures;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaExportDatabaseTest {

  @Rule
  public Neo4jRule       neo4j            = new Neo4jRule().withProcedure(SlizaaImportExportProcedures.class);

  @Rule
  public TemporaryFolder _temporaryFolder = new TemporaryFolder();

  /**
   * <p>
   * </p>
   *
   * @throws Throwable
   */
  @Test
  public void exportDatabase() throws Throwable {

    //
    try (Driver driver = GraphDatabase.driver(neo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {

      //
      Session session = driver.session();

      //
      session.run(
          "CREATE (u1:User {name:'Brookreson'})-[r:CONTAINS]->(u2:User {name:'Honki'}) RETURN id(u1), id(r), id(u2)");

      //
      File exportFile = _temporaryFolder.newFile("exportDatabase.txt");
      
      //
      session.run("CALL slizaa.exportDatabase({fileName})",
          Collections.singletonMap("fileName", exportFile.getAbsolutePath())).summary();

      // TODO: ASSERTS!
      try (Stream<String> stream = Files.lines(Paths.get(exportFile.getAbsolutePath()))) {
        stream.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}