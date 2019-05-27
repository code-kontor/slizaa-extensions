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
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.JTypeSlizaaTestServerRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class JTypeNodeComparatorTest {

	@ClassRule
	public static JTypeSlizaaTestServerRule SERVER = new JTypeSlizaaTestServerRule(
			multipleBinaryMvnArtifacts(new String[] { "com.google.guava", "guava", "23.6.1-jre" }));

	@ClassRule
	public static BoltClientConnectionRule CLIENT = new BoltClientConnectionRule();

	@Test
	public void testNodeComparator() {

		HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
				.convert(new JType_Hierarchical_MappingProvider(), CLIENT.getBoltClient(), null);

		//
		List<HGNode> packageNodes = CLIENT.getBoltClient()
				.syncExecCypherQuery("MATCH (p:Package {fqn: 'com/google/common/base'}) RETURN id(p)")
				.list(record -> rootNode.lookupNode(record.get("id(p)").asLong()));

		//
		assertThat(packageNodes).hasSize(1);

		List<HGNode> children = packageNodes.get(0).getChildren();

		//
		for (HGNode child : children) {
			GraphDbNodeSource nodeSource = child.getNodeSource(GraphDbNodeSource.class).get();
//      System.out.println(nodeSource.getLabels() + " : " + nodeSource.getProperties());
		}
	}
}
