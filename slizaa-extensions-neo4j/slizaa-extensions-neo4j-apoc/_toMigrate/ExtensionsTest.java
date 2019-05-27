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

import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.neo4j.testfwk.ContentDefinitionsUtils.simpleBinaryFile;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import io.codekontor.slizaa.neo4j.testfwk.SlizaaClientRule;
import io.codekontor.slizaa.neo4j.testfwk.SlizaaTestServerRule;

public class ExtensionsTest {

  /** - */
  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(simpleBinaryFile("test", "1.2.3",
      ExtensionsTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  /** - */
  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  @Test
  public void test_createGroup_1() {

    // create
    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createGroup('spunk/dunk')").list();
    assertThat(records).hasSize(1);

    // test
    records = this._client.getSession()
        .run("MATCH p=(g1:Group {name:'spunk'})-[:CONTAINS]->(g2:Group {name:'dunk'}) RETURN p").list();
    assertThat(records).hasSize(1);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test_Procedure_1() {

    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createModule('spunk/module_1', '1.0.0' )")
        .list();
    assertThat(records).hasSize(1);

    // test
    records = this._client.getSession()
        .run("MATCH p=(g:Group {name:'spunk'})-[:CONTAINS]->(m:Module {name:'module_1'}) RETURN p").list();
    assertThat(records).hasSize(1);
  }
}