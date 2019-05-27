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
package io.codekontor.slizaa.jtype.scanner.bytecode.internal;

import io.codekontor.slizaa.jtype.scanner.bytecode.IPrimitiveDatatypeNodeProvider;
import io.codekontor.slizaa.jtype.scanner.model.JTypeLabel;
import io.codekontor.slizaa.scanner.spi.parser.ICypherStatementExecutor;
import io.codekontor.slizaa.scanner.spi.parser.ICypherStatementExecutor.IResult;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;
import io.codekontor.slizaa.scanner.spi.parser.model.NodeFactory;

public class PrimitiveDatatypeNodeProvider implements IPrimitiveDatatypeNodeProvider {

  /** - */
  private INode _primitiveDatatypeByte;

  /** - */
  private INode _primitiveDatatypeShort;

  /** - */
  private INode _primitiveDatatypeInt;

  /** - */
  private INode _primitiveDatatypeLong;

  /** - */
  private INode _primitiveDatatypeFloat;

  /** - */
  private INode _primitiveDatatypeDouble;

  /** - */
  private INode _primitiveDatatypeChar;

  /** - */
  private INode _primitiveDatatypeBoolean;

  /** - */
  private INode _void;

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeByte() {
    return _primitiveDatatypeByte;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeShort() {
    return _primitiveDatatypeShort;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeInt() {
    return _primitiveDatatypeInt;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeLong() {
    return _primitiveDatatypeLong;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeFloat() {
    return _primitiveDatatypeFloat;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeDouble() {
    return _primitiveDatatypeDouble;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeChar() {
    return _primitiveDatatypeChar;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeBoolean() {
    return _primitiveDatatypeBoolean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INode getVoid() {
    return _void;
  }

  /**
   * <p>
   * Creates a new instance of type {@link PrimitiveDatatypeNodeProvider}.
   * </p>
   * 
   * @param cypherStatementExecutor
   */
  public PrimitiveDatatypeNodeProvider(ICypherStatementExecutor cypherStatementExecutor) {

    // we have to create nodes for the primitive data types (byte, short, int etc.) if they don't already exist
    _primitiveDatatypeByte = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "byte",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeShort = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "short",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeInt = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "int",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeLong = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "long",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeFloat = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "float",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeDouble = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "double",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeChar = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "char",
        JTypeLabel.PrimitiveDataType);
    _primitiveDatatypeBoolean = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "boolean",
        JTypeLabel.PrimitiveDataType);

    // void
    _void = createPrimitveDataTypeIfNotExists(cypherStatementExecutor, "void", JTypeLabel.Void);
  }

  /**
   * <p>
   * Tests if the database contains a node with the label {@link JTypeLabel#PRIMITIVE_DATA_TYPE} that represents the
   * specified primitive data type). If the node does not exist, a new node will be created.
   * </p>
   * 
   * @param graphDatabase
   * @param primtiveDataType
   * @return
   */
  private INode createPrimitveDataTypeIfNotExists(ICypherStatementExecutor cypherStatementExecutor,
      String primtiveDataType, JTypeLabel typeType) {

    //
    IResult result = cypherStatementExecutor.executeCypherStatement(
        String.format("MERGE (n:%s {fqn: '%s'}) RETURN id(n)", typeType.name(), primtiveDataType));

    //
    long nodeid = (long) result.single().get("id(n)");
    INode nodeBean = NodeFactory.createNode(nodeid);
    nodeBean.addLabel(typeType);
    nodeBean.putProperty("fqn", primtiveDataType);

    //
    return nodeBean;
  }
}
