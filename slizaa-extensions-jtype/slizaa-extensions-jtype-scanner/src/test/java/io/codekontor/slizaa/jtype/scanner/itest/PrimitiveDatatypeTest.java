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
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

public class PrimitiveDatatypeTest {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.google.guava", "guava", "23.6.1-jre" }));

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testPrimitiveDataType() {

    // check type references
    this._client.getBoltClient().syncExecAndConsume(
        "MATCH (p:PrimitiveDataType) RETURN count(p)",
        result -> assertThat(result.single().get("count(p)").asInt()).isEqualTo(8));

    //
    this._client.getBoltClient().syncExecAndConsume(
        "MATCH (t:TypeReference) WHERE t.fqn IN ['byte', 'short', 'int', 'long', 'float', 'double', 'char', 'boolean'] RETURN count(t)",
        result -> assertThat(result.single().get("count(t)").asInt()).isEqualTo(0));
  }
}
