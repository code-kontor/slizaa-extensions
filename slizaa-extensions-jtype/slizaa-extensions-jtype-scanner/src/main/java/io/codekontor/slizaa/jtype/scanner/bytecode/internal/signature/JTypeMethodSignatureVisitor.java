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
/***
 * ASM: a very small and fast Java bytecode manipulation framework Copyright (c) 2000-2011 INRIA, France Telecom All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. 3. Neither the name of the copyright holders nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.codekontor.slizaa.jtype.scanner.bytecode.internal.signature;

import io.codekontor.slizaa.jtype.scanner.bytecode.internal.TypeLocalReferenceCache;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * <li><i>MethodSignature</i> = ( <tt>visitFormalTypeParameter</tt> <tt>visitClassBound</tt>?
 * <tt>visitInterfaceBound</tt>* )* ( <tt>visitParameterType</tt>* <tt>visitReturnType</tt> <tt>visitExceptionType</tt>*
 * )
 * </p>
 */
public final class JTypeMethodSignatureVisitor extends AbstractSignatureVisitor {

  /**
   * <p>
   * Creates a new instance of type {@link JTypeMethodSignatureVisitor}.
   * </p>
   *
   * @param bean
   * @param classLocalTypeReferenceCache
   */
  public JTypeMethodSignatureVisitor(INode bean, TypeLocalReferenceCache classLocalTypeReferenceCache) {
    super(bean, classLocalTypeReferenceCache);
  }
}
