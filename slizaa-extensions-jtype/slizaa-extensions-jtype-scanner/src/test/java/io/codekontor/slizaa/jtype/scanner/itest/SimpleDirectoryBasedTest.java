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
import java.io.FilenameFilter;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.scanner.contentdefinition.FileBasedContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

public class SimpleDirectoryBasedTest {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(getSystemDefinition());

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    this._client.getBoltClient().syncExecAndConsume(
        "Match (t:TYPE) return count(t)",
        result -> System.out.println(result.single().get(0).asInt()));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static IContentDefinitionProvider getSystemDefinition() {

    //
    File[] children = new File("samples").listFiles((FilenameFilter) (file, name) -> new File(file, name).isFile()
        && name.endsWith(".jar") && !name.contains("source_"));

    //
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider();

    //
    for (File file : children) {

      // name
      String name = file.getName();
      int indexOfDot = name.lastIndexOf('.');
      if (indexOfDot != -1) {
        name = name.substring(0, indexOfDot);
      }

      // add new (custom) content provider
      provider.createFileBasedContentDefinition(name, "0.0.0", new File[] { file }, null, AnalyzeMode.BINARIES_ONLY);
    }

    //
    return provider;
  }
}
