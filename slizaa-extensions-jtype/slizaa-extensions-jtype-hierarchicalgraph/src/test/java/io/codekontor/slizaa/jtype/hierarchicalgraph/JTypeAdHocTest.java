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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.jtype.hierarchicalgraph.utils.HGNodeUtils;
import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import io.codekontor.slizaa.scanner.contentdefinition.DirectoryBasedContentDefinitionProvider;
import io.codekontor.slizaa.scanner.contentdefinition.DirectoryBasedContentDefinitionProviderFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class JTypeAdHocTest {

    private static final File ROOT_DIR =
            new File(System.getProperty("user.dir") + File.separator + "adhoc-example");

    @ClassRule
    public static JTypeTestServerRule SERVER = new JTypeTestServerRule(
            directoryContent(new File(ROOT_DIR, "content")))
            .withDatabaseDirectory(new File(ROOT_DIR, "database"))
            .withNoDeletionAfterTest();

    @ClassRule
    public static BoltClientConnectionRule CLIENT = new BoltClientConnectionRule();

    @Test
    public void adHocTest() throws IOException {

        //
        HGRootNode rootNode = MappingFactory.createMappingServiceForStandaloneSetup()
                .convert(new JType_Hierarchical_MappingProvider(), CLIENT.getBoltClient(), null);

        //
        try (FileWriter fileWriter = new FileWriter(new File(ROOT_DIR,"dump.txt"))) {
            HGNodeUtils.dumpNode(rootNode, fileWriter);
        }
    }
}
