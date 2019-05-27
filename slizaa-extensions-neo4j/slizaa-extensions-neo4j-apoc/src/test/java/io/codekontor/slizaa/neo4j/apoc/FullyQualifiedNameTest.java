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

import org.junit.Test;
import io.codekontor.slizaa.neo4j.apoc.arch.FullyQualifiedName;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FullyQualifiedNameTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testFullyQualifiedName() {

    //
    FullyQualifiedName fqn_1 = new FullyQualifiedName("test/fest/pest");
    assertThat(fqn_1.getSimpleName()).isEqualTo("pest");
    
    //
    assertThat(fqn_1.getParent()).isNotNull();
    assertThat(fqn_1.getParent().toString()).isEqualTo("test/fest");
    assertThat(fqn_1.getParent().getSimpleName()).isEqualTo("fest");
    
    //
    assertThat(fqn_1.getParent().getParent()).isNotNull();
    assertThat(fqn_1.getParent().getParent().toString()).isEqualTo("test");
    assertThat(fqn_1.getParent().getParent().getSimpleName()).isEqualTo("test");
    
    //
    assertThat(fqn_1.getParent().getParent().getParent()).isNull();
  }
}
