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

import org.objectweb.asm.Type;
import io.codekontor.slizaa.jtype.scanner.model.IFieldReferenceNode;
import io.codekontor.slizaa.jtype.scanner.model.IMethodReferenceNode;
import io.codekontor.slizaa.jtype.scanner.model.ITypeReferenceNode;
import io.codekontor.slizaa.jtype.scanner.model.JTypeLabel;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;
import io.codekontor.slizaa.scanner.spi.parser.model.NodeFactory;

public class JTypeNodeHelper {

  /**
   * <p>
   * </p>
   *
   * @param batchInserter
   * @param fullyQualifiedName
   * @return
   */
  public static INode createTypeReferenceNode(String fullyQualifiedName) {

    //
    if (fullyQualifiedName.startsWith("[")) {
      Type arrayType = Type.getType(fullyQualifiedName);
      Type type = Utils.resolveArrayType(arrayType);
      fullyQualifiedName = type.getClassName();
    }

    //
    assertNoPrimitiveType(fullyQualifiedName);

    //
    INode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.TypeReference);
    node.putProperty(ITypeReferenceNode.FQN, fullyQualifiedName.replace('/', '.'));
    return node;
  }

  /**
   * <p>
   * </p>
   *
   * @param fieldDescriptor
   * @return
   */
  public static INode createFieldReferenceNode(final FieldReferenceDescriptor fieldReferenceDescriptor) {

    //
    INode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.FieldReference);
    node.putProperty(IFieldReferenceNode.OWNER_TYPE_FQN, fieldReferenceDescriptor.getOwnerTypeName().replace('/', '.'));
    node.putProperty(IFieldReferenceNode.NAME, fieldReferenceDescriptor.getFieldName());
    node.putProperty(IFieldReferenceNode.TYPE, fieldReferenceDescriptor.getFieldType());
    node.putProperty(IFieldReferenceNode.STATIC, fieldReferenceDescriptor.isStatic());
    node.putProperty(IMethodReferenceNode.FQN, fieldReferenceDescriptor.getFieldType() + " "
        + fieldReferenceDescriptor.getOwnerTypeName() + "." + fieldReferenceDescriptor.getFieldName());

    //
    return node;
  }

  // TODO: MOVE
  public static INode createMethodReferenceNode(MethodReferenceDescriptor referencedMethod) {

    String ownerTypeName = referencedMethod.getOwnerTypeName().replace('/', '.');

    //
    INode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.MethodReference);
    node.putProperty(IMethodReferenceNode.OWNER_TYPE_FQN, referencedMethod.getOwnerTypeName().replace('/', '.'));
    node.putProperty(IMethodReferenceNode.NAME, referencedMethod.getMethodName());
    node.putProperty(IMethodReferenceNode.SIGNATURE, referencedMethod.getMethodSignature());
    node.putProperty(IMethodReferenceNode.FQN, Utils.getMethodSignature(
        ownerTypeName + "." + referencedMethod.getMethodName(), referencedMethod.getMethodSignature()));
    node.putProperty(IMethodReferenceNode.IS_INTERFACE, referencedMethod.isInterface());

    //
    return node;
  }

  /**
   * Checks whether the value contains the flag.
   *
   * @param value
   *          the value
   * @param flag
   *          the flag
   * @return <code>true</code> if (value & flag) == flag, otherwise <code>false</code>.
   */
  public static boolean hasFlag(int value, int flag) {
    return (value & flag) == flag;
  }

  /**
   * <p>
   * </p>
   *
   * @param referencedTypeName
   */
  public static void assertNoPrimitiveType(String referencedTypeName) {

    // byte, short, int, long, float, double, char, and boolean
    if (referencedTypeName.equals("byte") || referencedTypeName.equals("short") || referencedTypeName.equals("int")
        || referencedTypeName.equals("long") || referencedTypeName.equals("float")
        || referencedTypeName.equals("double") || referencedTypeName.equals("char")
        || referencedTypeName.equals("boolean")) {

      //
      throw new RuntimeException(referencedTypeName);
    }
  }
}
