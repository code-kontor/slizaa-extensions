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

import java.io.IOException;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

/**
 * 
 */
public class SimpleJTypeMapStructTest {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "org.mapstruct", "mapstruct", "1.0.0.Beta1" },
          new String[] { "org.mapstruct", "mapstruct-processor", "1.0.0.Beta1" }));

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   *
   * @throws IOException
   */
  @Test
  public void test() throws Exception {

    // 
    this._client.getBoltClient()
        .syncExecAndConsume("Match (t:Type) return count(t)", result -> assertThat(result.single().get(0).asInt()).isEqualTo(148));
  }
}
