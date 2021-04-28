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
package io.codekontor.slizaa.neo4j.graphdbfactory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.neo4j.graphdbfactory.internal.GraphDbFactory;

import java.io.File;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactoryTest {


  @ClassRule
  public static BoltClientConnectionRule client = new BoltClientConnectionRule("localhost", 5001);
	
  @Rule
  public TemporaryFolder _temporaryFolder = new TemporaryFolder();
  
  /**
   * <p>
   * </p>
   */
  @Test
  public void testGraphDbFactory() {

    GraphDbFactory graphDbFactory = new GraphDbFactory();
    IGraphDb graphDb = graphDbFactory.newGraphDb(5001, _temporaryFolder.getRoot()).create();
    
    assertThat(graphDb).isNotNull();

    graphDb.shutdown();
  }
}
