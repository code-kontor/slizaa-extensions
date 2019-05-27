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

import static com.google.common.base.Preconditions.checkNotNull;

import io.codekontor.slizaa.scanner.spi.parser.IParserContext;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ParserContext implements IParserContext {

  /** - */
  private boolean         _parseReferences;

  /** - */
  private INode _parentDirectory;

  /** - */
  private INode _moduleDirectory;

  public ParserContext(INode moduleDirectory, INode parentDirectory, boolean parseReferences) {
    _parseReferences = parseReferences;
    _parentDirectory = checkNotNull(parentDirectory);
    _moduleDirectory = checkNotNull(moduleDirectory);
  }

  @Override
  public boolean parseReferences() {
    return _parseReferences;
  }

  @Override
  public INode getParentDirectoryNode() {
    return _parentDirectory;
  }

  @Override
  public INode getParentModuleNode() {
    return _moduleDirectory;
  }
}
