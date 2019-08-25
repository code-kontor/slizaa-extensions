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

import java.io.File;
import java.util.Collections;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.scanner.contentdefinition.FileBasedContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 */
public class SimpleJTypeJDKTest {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(getSystemDefinition());

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   */
  @Test
  @Ignore
  public void test() {

    //
    StatementResult statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("Match (t:TYPE) return count(t)");
    System.out.println(statementResult.single().get(0).asInt());

    //
    this._client.getBoltClient().syncExecCypherQuery("CALL slizaa.exportDatabase({fileName})",
        Collections.singletonMap("fileName", "C:\\tmp\\exportDatabase.txt")).summary();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static IContentDefinitionProvider getSystemDefinition() {

    // create property string
    String version = System.getProperty("java.version");
    String classpath = System.getProperty("sun.boot.class.path");

    //
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider();

    //
    for (String path : classpath.split(File.pathSeparator)) {

      if (new File(path).exists()) {

        // name
        String name = path.substring(path.lastIndexOf(File.separator) + 1);
        int indexOfDot = name.lastIndexOf('.');
        if (indexOfDot != -1) {
          name = name.substring(0, indexOfDot);
        }

        // add the JARs
        provider.createFileBasedContentDefinition("jdk-" + name, version, new File[] {}, null,
            AnalyzeMode.BINARIES_ONLY);
      }
    }

    //
    return provider;
  }
}
