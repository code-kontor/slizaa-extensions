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
package io.codekontor.slizaa.jtype.scanner.jdeps;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.simpleBinaryFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.jtype.scanner.jdeps.internal.JavapWrapper;

@Ignore
@RunWith(Parameterized.class)
public class JdepsTest {

  @Rule
  public JTypeTestServerRule _slizaaTestServerRule = new JTypeTestServerRule(
      simpleBinaryFile("dummy", "dummy", getJarFile()));

  @Rule
  public BoltClientConnectionRule  _client               = new BoltClientConnectionRule();

  @Rule
  public JDepsRule                 _jDepsRule            = new JDepsRule(() -> getJarFile());

  /** - */
  private File                     _jarFile;

  /**
   * <p>
   * Creates a new instance of type {@link JdepsTest}.
   * </p>
   *
   * @param jarFile
   */
  public JdepsTest(File jarFile) {
    this._jarFile = checkNotNull(jarFile);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public File getJarFile() {
    return this._jarFile;
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {
    System.out.println("Testing " + this._jarFile.getAbsolutePath());
    this._jDepsRule.getJdepAnalysis().keySet().stream().sorted().forEach(fqn -> assertSameReferences(fqn));
    System.out.println("Done " + this._jarFile.getAbsolutePath());
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   */
  private void assertSameReferences(String fqn) {

    //
    this._client.getBoltClient().syncExecAndConsume(
        "Match (t:TYPE {fqn: $name})-[:DEPENDS_ON]->(tr:TYPE_REFERENCE) return tr.fqn",
        Collections.singletonMap("name", fqn),
        result -> {
          List<String> referencesDetectedBySlizaa = result.list(record -> record.get(0).asString());
          try {
            assertThat(referencesDetectedBySlizaa)
                .containsExactlyInAnyOrder(this._jDepsRule.getJdepAnalysis().get(fqn).toArray(new String[0]));
          } catch (AssertionError e) {
            JavapWrapper.doIt(this._jarFile.getAbsolutePath(), fqn);
            throw e;
          }
        });
  }

  @Parameters
  public static Collection<Object[]> data() {

    // the result
    Collection<Object[]> result = new ArrayList<>();

    String dir = "D:\\50-Development\\environments\\schrott\\slizaa-master\\ws\\TestReferenceProject\\libs";

    try {
      Files.newDirectoryStream(Paths.get(dir), "*.jar").forEach(p -> result.add(new Object[] { p.toFile() }));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // return result
    return result;
  }
}
