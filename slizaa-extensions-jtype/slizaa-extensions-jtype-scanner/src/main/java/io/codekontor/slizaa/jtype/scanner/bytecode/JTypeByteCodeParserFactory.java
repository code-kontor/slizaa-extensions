/**
 * slizaa-extensions-jtype-scanner - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.scanner.bytecode;

import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.jtype.scanner.bytecode.internal.PrimitiveDatatypeNodeProvider;
import io.codekontor.slizaa.scanner.spi.annotations.ParserFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.parser.ICypherStatementExecutor;
import io.codekontor.slizaa.scanner.spi.parser.IParser;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * The {@link IParserFactory} to create instances of {@link JTypeByteCodeParser}.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@ParserFactory
public class JTypeByteCodeParserFactory extends IParserFactory.Adapter implements IParserFactory {

  /** - */
  IPrimitiveDatatypeNodeProvider _datatypeNodeProvider = null;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IPrimitiveDatatypeNodeProvider getDatatypeNodeProviderMap() {
    return this._datatypeNodeProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IParser createParser(IContentDefinitionProvider contentDefinition) {
    return new JTypeByteCodeParser(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStart(IContentDefinitionProvider contentDefinitions, ICypherStatementExecutor cypherStatementExecutor,
      IProgressMonitor subMonitor) throws Exception {

      this._datatypeNodeProvider = new PrimitiveDatatypeNodeProvider(cypherStatementExecutor);

    //
      cypherStatementExecutor.executeCypherStatement("create index on :Type(fqn)");
      cypherStatementExecutor.executeCypherStatement("create index on :TypeReference(fqn)");
      cypherStatementExecutor.executeCypherStatement("create index on :Field(fqn)");
      cypherStatementExecutor.executeCypherStatement("create index on :FieldReference(fqn)");
      cypherStatementExecutor.executeCypherStatement("create index on :Method(fqn)");
      cypherStatementExecutor.executeCypherStatement("create index on :MethodReference(fqn)");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStop(IContentDefinitionProvider contentDefinition, ICypherStatementExecutor cypherStatementExecutor,
      IProgressMonitor subMonitor) {

    //
    this._datatypeNodeProvider = null;
  }
}
