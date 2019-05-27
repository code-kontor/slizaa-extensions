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
package io.codekontor.slizaa.jtype.scanner.bytecode.internal.signature;

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;
import io.codekontor.slizaa.jtype.scanner.bytecode.internal.TypeLocalReferenceCache;
import io.codekontor.slizaa.jtype.scanner.model.JTypeModelRelationshipType;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractSignatureVisitor extends SignatureVisitor {

  /** - */
  private INode                   _bean;

  /** - */
  private TypeLocalReferenceCache _classLocalTypeReferenceCache;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractSignatureVisitor}.
   * </p>
   *
   * @param bean
   * @param classLocalTypeReferenceCache
   */
  public AbstractSignatureVisitor(INode bean, TypeLocalReferenceCache classLocalTypeReferenceCache) {
    super(Opcodes.ASM7);

    //
    this._bean = checkNotNull(bean);

    //
    this._classLocalTypeReferenceCache = checkNotNull(classLocalTypeReferenceCache);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void visitClassType(final String name) {
    this._classLocalTypeReferenceCache.addTypeReference(this._bean, name, JTypeModelRelationshipType.REFERENCES);
  }
}
