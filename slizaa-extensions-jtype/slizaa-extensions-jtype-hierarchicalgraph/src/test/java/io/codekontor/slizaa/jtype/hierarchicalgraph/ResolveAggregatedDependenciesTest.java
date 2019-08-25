/**
 * slizaa-extensions-jtype-hierarchicalgraph - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.hierarchicalgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.HGNodeUtils;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class ResolveAggregatedDependenciesTest {

  @ClassRule
  public static JTypeTestServerRule SERVER = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.fasterxml.jackson.core", "jackson-annotations", "2.5.0" },
          new String[] { "com.fasterxml.jackson.core", "jackson-core", "2.5.1" },
          new String[] { "com.fasterxml.jackson.core", "jackson-databind", "2.5.1" },
          new String[] { "com.fasterxml.jackson.module", "jackson-module-jsonSchema", "2.5.1" },
          new String[] { "com.github.fge", "jackson-coreutils", "1.8" }));

  @ClassRule
  public static BoltClientConnectionRule  CLIENT = new BoltClientConnectionRule();

  @Test
  public void testResolveAggregatedLibraries() {

    //
    HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
        .convert(new JType_Hierarchical_MappingProvider(), CLIENT.getBoltClient(), null);

    //
    int unresolvedCount = 0;

    //
    for (HGCoreDependency dependency : rootNode.getAccumulatedIncomingCoreDependencies()) {

      //
      if (dependency instanceof HGProxyDependency) {

        HGProxyDependency proxyDependency = (HGProxyDependency) dependency;

        //
        proxyDependency.resolveProxyDependencies();

        //
        if (proxyDependency.getResolvedCoreDependencies().size() == 0) {

          //
          unresolvedCount++;

          //
          System.out.println(HGNodeUtils.getProperties(proxyDependency.getFrom()).get("fqn") + " -"
              + proxyDependency.getType() + "-> " + HGNodeUtils.getProperties(proxyDependency.getTo()).get("fqn"));
        }
      }

    }

    //
    assertThat(unresolvedCount == 0);
  }
}
