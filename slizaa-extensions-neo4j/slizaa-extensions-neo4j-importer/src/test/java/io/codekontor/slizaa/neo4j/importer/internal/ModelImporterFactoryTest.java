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
package io.codekontor.slizaa.neo4j.importer.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;

public class ModelImporterFactoryTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void testModelImporterFactory() throws IOException {

    File databaseDirectory = temporaryFolder.newFolder();

    //
    MvnBasedContentDefinitionProvider contentDefinitionProvider = new MvnBasedContentDefinitionProviderFactory().emptyContentDefinitionProvider();
    contentDefinitionProvider.addArtifact("org.springframework:spring-core:5.0.9.RELEASE");
    contentDefinitionProvider.addArtifact("org.springframework:spring-context:5.0.9.RELEASE");

    // delete all contained files
    Files.walk(databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
        .map(Path::toFile).forEach(File::delete);

    //
    IModelImporter modelImporter = new ModelImporterFactory().createModelImporter(
        (io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider) contentDefinitionProvider,
        databaseDirectory, Collections.emptyList(),
        Collections.emptyList());

    IProgressMonitor progressMonitor = new DefaultProgressMonitor( "Parsing...", 100, DefaultProgressMonitor.consoleLogger());
    modelImporter.parse(progressMonitor);
  }
}
