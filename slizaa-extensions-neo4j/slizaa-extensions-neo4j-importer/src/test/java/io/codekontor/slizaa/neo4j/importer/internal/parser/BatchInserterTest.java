/**
 * slizaa-extensions-neo4j-importer - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.importer.internal.parser;

import io.codekontor.slizaa.neo4j.importer.internal.parser.BatchInserterFacade;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.neo4j.graphdb.Label;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class BatchInserterTest {

    /** - */
    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Test
    public void testBatchInserter() throws IOException {

        File folder = _temporaryFolder.newFolder();

        BatchInserterFacade batchInserterFacade = new BatchInserterFacade(folder.getAbsolutePath());

        batchInserterFacade.batchInserter().createNode(Collections.singletonMap("name", "test"), Label.label("test"));
        batchInserterFacade.batchInserter().createNode(Collections.singletonMap("name", "fest"), Label.label("fest"));

        batchInserterFacade.close();
    }
}
