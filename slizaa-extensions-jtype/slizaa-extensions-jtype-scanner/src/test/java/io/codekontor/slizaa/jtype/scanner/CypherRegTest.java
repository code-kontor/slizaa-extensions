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
package io.codekontor.slizaa.jtype.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import io.codekontor.slizaa.scanner.cypherregistry.CypherStatementRegistry;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CypherRegTest {

  /** - */
  private ICypherStatementRegistry _statementRegistry;

  @Before
  public void init() {
    this._statementRegistry = new CypherStatementRegistry(
        () -> CypherRegistryUtils.getCypherStatementsFromClasspath(this.getClass().getClassLoader()));
    this._statementRegistry.rescan();
  }

  @Test
  public void testExistingStatements() {
    assertThat(this._statementRegistry.getAllStatements()).hasSize(4);
  }
}
