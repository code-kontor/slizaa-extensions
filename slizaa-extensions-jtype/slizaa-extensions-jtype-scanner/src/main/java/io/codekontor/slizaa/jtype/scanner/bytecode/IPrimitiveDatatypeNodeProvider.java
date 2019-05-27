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

import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * The {@link IPrimitiveDatatypeNodeProvider} provides the {@link INode NodeBeans} for the primitive datatypes (e.g.
 * <code>int</code>, <code>long</code>).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IPrimitiveDatatypeNodeProvider {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeByte();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeShort();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeInt();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeLong();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeFloat();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeDouble();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeChar();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeBoolean();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getVoid();
}
