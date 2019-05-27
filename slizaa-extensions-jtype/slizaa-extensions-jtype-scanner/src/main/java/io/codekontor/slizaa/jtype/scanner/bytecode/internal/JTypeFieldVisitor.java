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

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class JTypeFieldVisitor extends FieldVisitor {

  /** - */
  private JTypeClassVisitor _classVisitor;

  /**
   * @param recorder
   * @param type
   */
  public JTypeFieldVisitor(JTypeClassVisitor classVisitor) {
    super(Opcodes.ASM7);

    // set the class visitor
    this._classVisitor = classVisitor;
  }

  /**
   * {@inheritDoc}
   */
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    // TODO!

    //
    return null;
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
    // TODO!

    //
    return null;
  }

  @Override
  public void visitAttribute(Attribute attr) {
    // ignored for now...
  }
}
