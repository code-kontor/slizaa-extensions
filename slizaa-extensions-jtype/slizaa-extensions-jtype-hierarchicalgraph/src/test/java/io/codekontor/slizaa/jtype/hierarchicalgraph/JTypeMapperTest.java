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

import java.util.List;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.HGNodeUtils;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.JTypeSlizaaTestServerRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class JTypeMapperTest {

  @ClassRule
  public static JTypeSlizaaTestServerRule SERVER = new JTypeSlizaaTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.google.guava", "guava", "23.6.1-jre" }));

  @ClassRule
  public static BoltClientConnectionRule  CLIENT = new BoltClientConnectionRule();

  @Test
  public void testMatrix() {

    HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
        .convert(new JType_Hierarchical_MappingProvider(), CLIENT.getBoltClient(), null);

    //
    List<HGNode> packageNodes = CLIENT.getBoltClient()
        .syncExecCypherQuery("MATCH (:Package {fqn: 'com/google/common'})-[:CONTAINS]->(p:Package) RETURN id(p)")
        .list(record -> rootNode.lookupNode(record.get("id(p)").asLong()));

    //
    int[][] dependencies = GraphUtils.computeAdjacencyMatrix(packageNodes);

    //
    assertThat(dependencies).isEqualTo(new int[][] { { 372, 75, 1, 21, 0, 0, 0, 0, 0, 0, 55, 0, 0, 0, 0, 3 },
        { 0, 4114, 22, 283, 0, 0, 0, 0, 0, 0, 443, 0, 0, 0, 0, 12 },
        { 0, 0, 106, 34, 0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0 }, { 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 17, 35, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0 }, { 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 12, 0, 6, 0, 0, 66, 0, 0, 0, 15, 0, 2, 5, 4, 0 }, { 0, 14, 1, 15, 2, 0, 0, 14, 3, 2, 30, 0, 0, 0, 0, 0 },
        { 0, 0, 16, 11, 0, 0, 0, 0, 294, 0, 36, 0, 0, 0, 0, 2 },
        { 6, 14, 4, 55, 0, 0, 0, 0, 9, 237, 70, 0, 0, 0, 0, 3 }, { 0, 0, 0, 58, 0, 0, 0, 0, 0, 0, 495, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 67, 4, 123, 0, 0, 0, 0, 0, 0, 118, 0, 867, 0, 0, 2 },
        { 0, 24, 1, 21, 0, 0, 0, 0, 0, 0, 71, 0, 22, 484, 0, 0 },
        { 0, 76, 1, 15, 0, 0, 0, 0, 0, 3, 47, 0, 0, 0, 235, 0 },
        { 0, 0, 9, 18, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 74 } });
  }

  @Test
  @Ignore
  public void dump() {

    //
    HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
        .convert(new JType_Hierarchical_MappingProvider(), CLIENT.getBoltClient(), null);

    // //
    HGNodeUtils.dumpNode(rootNode);
  }
}
