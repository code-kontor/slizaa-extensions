/**
 * slizaa-extensions-jtype-scanner-apoc - Slizaa Static Software Analysis Tools
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
/**
 *
 */
package io.codekontor.slizaa.jtype.scanner.apoc;

import java.io.File;
import java.util.function.Consumer;

import io.codekontor.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.testfwk.SlizaaTestServerRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeSlizaaTestServerRule extends SlizaaTestServerRule {

  /**
   * <p>
   * Creates a new instance of type {@link JTypeSlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param contentDefinitions
   */
  public JTypeSlizaaTestServerRule(File workingDirectory, IContentDefinitionProvider contentDefinitions) {
    super(workingDirectory, contentDefinitions, createBackendLoaderConfigurer());
  }

  /**
   * <p>
   * Creates a new instance of type {@link JTypeSlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitions
   */
  public JTypeSlizaaTestServerRule(IContentDefinitionProvider contentDefinitions) {
    super(contentDefinitions, createBackendLoaderConfigurer());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static Consumer<IMvnResolverJob> createBackendLoaderConfigurer() {

    // @formatter:off
    return job -> job
        .withDependency(mavenArtifact("io.codekontor.slizaa.extensions", "slizaa-extensions-neo4j-importer", versionAsInProject()))
        .withDependency(mavenArtifact("io.codekontor.slizaa.extensions", "slizaa-extensions-neo4j-graphdbfactory", versionAsInProject()))
        .withDependency(mavenArtifact("io.codekontor.slizaa.extensions", "slizaa-extensions-jtype-scanner", versionAsInProject()))
        .withExclusionPattern("*:slizaa-scanner-spi-api")
        .withExclusionPattern("*:jdk.tools");
    // @formatter:on
  }
}
