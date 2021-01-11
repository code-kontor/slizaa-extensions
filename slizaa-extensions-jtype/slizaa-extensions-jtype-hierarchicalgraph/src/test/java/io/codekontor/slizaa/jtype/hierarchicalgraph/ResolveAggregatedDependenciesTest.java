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

import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.Utilities;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.internal.DefaultDependencySet;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.HGNodeUtils;
import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
@Ignore
public class ResolveAggregatedDependenciesTest {

  @ClassRule
  public static JTypeTestServerRule      SERVER = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.fasterxml.jackson.core", "jackson-annotations", "2.5.0" },
          new String[] { "com.fasterxml.jackson.core", "jackson-core", "2.5.1" },
          new String[] { "com.fasterxml.jackson.core", "jackson-databind", "2.5.1" },
          new String[] { "com.fasterxml.jackson.module", "jackson-module-jsonSchema", "2.5.1" },
          new String[] { "com.github.fge", "jackson-coreutils", "1.8" }));

  @ClassRule
  public static BoltClientConnectionRule CLIENT = new BoltClientConnectionRule();

  private HGRootNode rootNode;

  @Before
  public void before() {
    rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
            .convert(new JType_MappingProvider(), CLIENT.getBoltClient(), null);
  }

  @After
  public void after() {
    rootNode = null;
  }


  @Test
  public void testResolveIncomingDependencies() {

    assertThat(rootNode.getAccumulatedIncomingCoreDependencies()).hasSize(10493);
    rootNode.resolveIncomingProxyDependencies();
    assertThat(rootNode.getAccumulatedIncomingCoreDependencies()).hasSize(10493);

    rootNode.getAccumulatedIncomingCoreDependencies().stream().filter(dep -> dep instanceof HGProxyDependency)
        .forEach(dep -> assertThat(((HGProxyDependency) dep).isResolved()).isTrue());
  }

  @Test
  public void testResolveOutgoingDependencies() {

    assertThat(rootNode.getAccumulatedOutgoingCoreDependencies()).hasSize(10493);
    rootNode.resolveOutgoingProxyDependencies();
    assertThat(rootNode.getAccumulatedOutgoingCoreDependencies()).hasSize(10493);

    rootNode.getAccumulatedOutgoingCoreDependencies().stream().filter(dep -> dep instanceof HGProxyDependency)
            .forEach(dep -> assertThat(((HGProxyDependency) dep).isResolved()).isTrue());
  }

  @Test
  public void testResolveClassProxyDependencies() {

    long id = CLIENT.getBoltClient()
        .syncExecCypherQuery(
            "Match (t:Type) Where t.fqn = 'com.fasterxml.jackson.core.base.GeneratorBase' Return id(t)",
            result -> result.single().get("id(t)").asLong());
    
    HGNode node = rootNode.lookupNode(id);

    // Dump all accumulated outgoing core dependencies
    List<String> coreDependencyList = node.getAccumulatedOutgoingCoreDependencies().stream()
            .sorted(Comparator.comparing(dep -> HGNodeUtils.toString(dep.getTo())))
            .map(dep -> HGNodeUtils.toString(dep))
            .collect(Collectors.toList());
    
    assertThat(coreDependencyList).containsExactly(
		"GeneratorBase -[DEPENDS_ON]-> Base64Variant",
		"GeneratorBase -[DEPENDS_ON]-> DefaultPrettyPrinter",
		"GeneratorBase -[DEPENDS_ON]-> DupDetector",
		"GeneratorBase -[DEPENDS_ON]-> JsonGenerator",
		"GeneratorBase -[DEPENDS_ON]-> JsonGenerator$Feature",
		"GeneratorBase -[DEPENDS_ON]-> JsonStreamContext",
		"GeneratorBase -[DEPENDS_ON]-> JsonWriteContext",
		"GeneratorBase -[DEPENDS_ON]-> ObjectCodec",
		"GeneratorBase -[DEPENDS_ON]-> PrettyPrinter",
		"GeneratorBase -[DEPENDS_ON]-> SerializableString",
		"GeneratorBase -[DEPENDS_ON]-> TreeNode",
		"GeneratorBase -[DEPENDS_ON]-> Version",
		"GeneratorBase -[DEPENDS_ON]-> VersionUtil",
		"GeneratorBase -[DEPENDS_ON]-> java.io.IOException",
		"GeneratorBase -[DEPENDS_ON]-> java.io.InputStream",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.Class",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.IllegalStateException",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.Integer",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.Object",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.String",
		"GeneratorBase -[DEPENDS_ON]-> java.lang.StringBuilder"
    );

    // resolve all outgoing proxy dependencies
    node.getAccumulatedOutgoingCoreDependencies().forEach(dep -> {
      if (dep instanceof HGProxyDependency) {
        HGProxyDependency proxyDependency = ((HGProxyDependency) dep);
        assertThat(proxyDependency.isResolved()).isFalse();
        proxyDependency.resolve();
        assertThat(proxyDependency.isResolved()).isTrue();
      }
    });
  }

  @Test
  @Ignore
  public void testDependencySet() {

    long id = CLIENT.getBoltClient()
            .syncExecCypherQuery(
                    "Match (t:Type) Where t.fqn = 'com.fasterxml.jackson.core.base.GeneratorBase' Return id(t)",
                    result -> result.single().get("id(t)").asLong());
    
    HGNode node = rootNode.lookupNode(id);

    DefaultDependencySet dependencySet = new DefaultDependencySet(node.getAccumulatedOutgoingCoreDependencies());

    Set<HGNode> children = dependencySet.getFilteredNodeChildren(node, SourceOrTarget.SOURCE, false);
    System.out.println(children.size());

    dependencySet.resolveAllProxyDependencies();

    children = dependencySet.getFilteredNodeChildren(node, SourceOrTarget.SOURCE, false);
    System.out.println(children.size());
  }

  @Test
  public void testResolveAggregatedLibraries() {

    for (HGCoreDependency dependency : rootNode.getAccumulatedIncomingCoreDependencies()) {
      if (dependency instanceof HGProxyDependency) {
        HGProxyDependency proxyDependency = (HGProxyDependency) dependency;
        proxyDependency.resolve();

//        if (proxyDependency.getAccumulatedCoreDependencies().size() == 0) {
//          System.out.println(HGNodeUtils.getProperties(proxyDependency.getFrom()).get("fqn") + " -"
//              + proxyDependency.getType() + "-> " + HGNodeUtils.getProperties(proxyDependency.getTo()).get("fqn"));
//        }
      }
    }

    //
    for (HGCoreDependency dependency : rootNode.getAccumulatedIncomingCoreDependencies()) {
      if (dependency instanceof HGProxyDependency) {
        HGProxyDependency proxyDependency = (HGProxyDependency) dependency;
        assertThat(proxyDependency.isResolved()).isTrue();
      }
    }
  }

  @Test
  public void testResolveAggregatedLibrariesAtOnce() {

    //
    HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
            .convert(new JType_MappingProvider(), CLIENT.getBoltClient(), null);

    List<HGProxyDependency> proxyDependencies = rootNode.getAccumulatedIncomingCoreDependencies().stream()
            .filter(dep -> dep instanceof HGProxyDependency)
            .map(dep -> (HGProxyDependency) dep)
            .collect(Collectors.toList());

    //
    Utilities.resolveProxyDependencies(proxyDependencies);

    //
    for (HGCoreDependency dependency : rootNode.getAccumulatedIncomingCoreDependencies()) {
      if (dependency instanceof HGProxyDependency) {
        HGProxyDependency proxyDependency = (HGProxyDependency) dependency;
        assertThat(proxyDependency.isResolved()).isTrue();
      }
    }
  }
}
