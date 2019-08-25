/**
 * slizaa-extensions-jtype-scanner - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.scanner.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

/**
  */
public class CreateSampleDatabase {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "org.mapstruct", "mapstruct", "1.2.0.Final" },
          new String[] { "org.mapstruct", "mapstruct-processor", "1.2.0.Final" }));

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  @Ignore
  public void test() throws Exception {

    //
    StatementResult statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("Match (t:Type) return count(t)");
    assertThat(statementResult.single().get(0).asInt()).isEqualTo(1054);

    //
    _server.exportDatabaseAsZipFile("mapstruct_1-2-0-Final-db.zip", false);
  }
}
