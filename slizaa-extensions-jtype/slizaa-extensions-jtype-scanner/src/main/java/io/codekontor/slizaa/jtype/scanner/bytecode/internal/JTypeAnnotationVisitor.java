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
import org.objectweb.asm.Opcodes;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeAnnotationVisitor extends AnnotationVisitor {

  /**
   * <p>
   * Creates a new instance of type {@link JTypeAnnotationVisitor}.
   * </p>
   */
  public JTypeAnnotationVisitor() {
    super(Opcodes.ASM7);
  }

  @Override
  public void visit(String name, Object value) {
    super.visit(name, value);
  }

  @Override
  public void visitEnum(String name, String desc, String value) {
    super.visitEnum(name, desc, value);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String name, String desc) {
    return super.visitAnnotation(name, desc);
  }

  @Override
  public AnnotationVisitor visitArray(String name) {
    return super.visitArray(name);
  }

}
