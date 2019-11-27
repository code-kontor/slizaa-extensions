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

import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.INodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class JTypeGuavaExampleTest {

  @ClassRule
  public static JTypeTestServerRule       SERVER = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.google.guava", "guava", "21.0" }));

  @ClassRule
  public static BoltClientConnectionRule  CLIENT = new BoltClientConnectionRule();

  private static HGRootNode               rootNode;

  private static ILabelDefinitionProvider labelDefinitionProvider;

  @BeforeClass
  public static void init() {
    rootNode = MappingFactory.createMappingServiceForStandaloneSetup().convert(new JType_Hierarchical_MappingProvider(),
        CLIENT.getBoltClient(), null);

    labelDefinitionProvider = rootNode.getExtension(ILabelDefinitionProvider.class);
  }

  @Test
  public void testMissingTypesSortedToTheBottom() {

    List<HGNode> nodes = HierarchicalGraphUtils.sorted(rootNode.getChildren());

    // get the missing types node
    HGNode missingTypesNode = nodes.get(nodes.size() - 1);
    String label = labelDefinitionProvider.getLabelDefinition(missingTypesNode).getText();

    // assert
    assertThat(label).isEqualTo("<<Missing Types>>");
  }

  @Test
  public void testMissingNodes() {

    List<Long> missingTypes = CLIENT.getBoltClient().syncExecCypherQuery("MATCH (t:MissingType) RETURN t")
        .list(record -> record.get("t").asNode().id());

    assertThat(missingTypes).hasSize(341);
    for (Long missingType : missingTypes) {
      HGNode missingTypeNode = rootNode.lookupNode(missingType);
      assertThat(missingTypeNode).isNotNull();
    }
  }

  @Test
  public void testMatrix() {

    //
    List<HGNode> packageNodes = CLIENT.getBoltClient()
        .syncExecCypherQuery("MATCH (:Package {fqn: 'com/google/common'})-[:CONTAINS]->(p:Package) RETURN id(p)")
        .list(record -> rootNode.lookupNode(record.get("id(p)").asLong()));

    //
    int[][] dependencies = GraphUtils.computeAdjacencyMatrix(packageNodes);

    //
    assertThat(dependencies).isEqualTo(new int[][] { { 280, 66, 1, 18, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 3 },
        { 0, 3911, 21, 275, 0, 0, 0, 0, 0, 0, 437, 0, 0, 0, 0, 12 },
        { 0, 0, 72, 28, 0, 0, 0, 0, 0, 0, 33, 0, 0, 0, 0, 0 }, { 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 17, 34, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0 }, { 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 12, 0, 6, 0, 0, 66, 0, 0, 0, 15, 0, 2, 5, 4, 0 }, { 0, 16, 1, 15, 2, 0, 0, 12, 3, 2, 29, 0, 0, 0, 0, 0 },
        { 0, 0, 16, 10, 0, 0, 0, 0, 294, 0, 29, 0, 0, 0, 0, 1 },
        { 0, 14, 4, 56, 0, 0, 0, 0, 9, 228, 75, 0, 0, 0, 0, 3 }, { 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 499, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 65, 2, 119, 0, 0, 0, 0, 0, 0, 109, 0, 816, 0, 0, 2 },
        { 0, 24, 1, 20, 0, 0, 0, 0, 0, 0, 71, 0, 20, 482, 0, 0 },
        { 0, 73, 1, 15, 0, 0, 0, 0, 0, 3, 46, 0, 0, 0, 224, 0 },
        { 0, 0, 9, 18, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 75 } });
  }

  @Test
  public void testNodeComparatorForPackages() {

    //
    List<HGNode> packageNodes = CLIENT.getBoltClient()
        .syncExecCypherQuery("MATCH (p:Package {fqn: 'com/google/common/base'}) RETURN id(p)")
        .list(record -> rootNode.lookupNode(record.get("id(p)").asLong()));

    //
    assertThat(packageNodes).hasSize(1);

    List<HGNode> children = HierarchicalGraphUtils.sorted(packageNodes.get(0).getChildren());
    List<String> labels = children.stream().map(child -> (labelDefinitionProvider.getLabelDefinition(child).getText()))
        .collect(Collectors.toList());

      assertThat(labels).containsExactly(
      "internal",
      "Absent.class",
      "AbstractIterator.class",
      "Ascii.class",
      "CaseFormat.class",
      "CharMatcher.class",
      "Charsets.class",
      "CommonMatcher.class",
      "CommonPattern.class",
      "Converter.class",
      "Defaults.class",
      "Enums.class",
      "Equivalence.class",
      "ExtraObjectsMethodsForWeb.class",
      "FinalizablePhantomReference.class",
      "FinalizableReference.class",
      "FinalizableReferenceQueue.class",
      "FinalizableSoftReference.class",
      "FinalizableWeakReference.class",
      "Function.class",
      "FunctionalEquivalence.class",
      "Functions.class",
      "JdkPattern.class",
      "Joiner.class",
      "MoreObjects.class",
      "Objects.class",
      "Optional.class",
      "PairwiseEquivalence.class",
      "PatternCompiler.class",
      "Platform.class",
      "Preconditions.class",
      "Predicate.class",
      "Predicates.class",
      "Present.class",
      "SmallCharMatcher.class",
      "Splitter.class",
      "StandardSystemProperty.class",
      "Stopwatch.class",
      "Strings.class",
      "Supplier.class",
      "Suppliers.class",
      "Throwables.class",
      "Ticker.class",
      "Utf8.class",
      "Verify.class",
      "VerifyException.class",
      "package-info.class");
  }

  @Test
  public void testClass() {

    List<HGNode> typesNodes = CLIENT.getBoltClient()
        .syncExecCypherQuery("MATCH (t:Type {fqn: 'com.google.common.base.Stopwatch'}) RETURN id(t)")
        .list(record -> rootNode.lookupNode(record.get("id(t)").asLong()));

    assertThat(typesNodes).hasSize(1);

    List<HGNode> children = HierarchicalGraphUtils.sorted(typesNodes.get(0).getChildren());
    List<String> labels = children.stream().map(child -> (labelDefinitionProvider.getLabelDefinition(child).getText()))
        .collect(Collectors.toList());

    ILabelDefinitionProvider labelDefinitionProvider = rootNode.getExtension(ILabelDefinitionProvider.class);

    // TODO: Test!
    for (HGNode child : children) {
      GraphDbNodeSource nodeSource = (GraphDbNodeSource) child.getNodeSource();
      System.out.println(labelDefinitionProvider.getLabelDefinition(child).getText());
      // System.out.println(nodeSource.getProperties());
    }
  }

  // TODO: Copied from graphql node utils
  private static List<HGNode> sorted(Collection<HGNode> nodes) {
    if (nodes != null && !nodes.isEmpty()) {
      HGRootNode rootNode = nodes.iterator().next().getRootNode();
      INodeComparator nodeComparator = rootNode.getExtension(INodeComparator.class);
      return nodes.stream().sorted(new Comparator<HGNode>() {
        @Override
        public int compare(HGNode node1, HGNode node2) {
          int category1 = nodeComparator.category(node1);
          int category2 = nodeComparator.category(node2);
          if (category1 == category2) {
            return nodeComparator.compare(node1, node2);
          } else {
            return category1 - category2;
          }
        }
      }).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
