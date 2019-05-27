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

import java.io.File;
import java.util.List;

import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatement;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.neo4j.importer.internal.parser.ModelImporter;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ModelImporterFactory implements IModelImporterFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public IModelImporter createModelImporter(IContentDefinitionProvider contentDefinitionProvider,
      File databaseDirectory, List<IParserFactory> parserFactories, List<ICypherStatement> cypherStatements) {

    //
    return new ModelImporter(contentDefinitionProvider, databaseDirectory, parserFactories, cypherStatements);
  }
}
