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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import io.codekontor.slizaa.jtype.scanner.bytecode.internal.JTypeClassVisitor;
import io.codekontor.slizaa.jtype.scanner.model.JTypeLabel;
import io.codekontor.slizaa.jtype.scanner.model.JTypeModelRelationshipType;
import io.codekontor.slizaa.jtype.scanner.model.JavaUtils;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import io.codekontor.slizaa.scanner.spi.parser.AbstractParser;
import io.codekontor.slizaa.scanner.spi.parser.IParser;
import io.codekontor.slizaa.scanner.spi.parser.IParserContext;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.scanner.spi.parser.ParserType;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelRelationshipType;

/**
 * <p>
 * A {@link IParser} that parses java class files and creates a graph representation of the contained types.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeByteCodeParser extends AbstractParser<JTypeByteCodeParserFactory> {

  /**
   * <p>
   * Creates a new instance of type {@link JTypeByteCodeParser}.
   * </p>
   * 
   * @param parserFactory
   *          the {@link IParserFactory} that created this {@link IParser}.
   */
  JTypeByteCodeParser(JTypeByteCodeParserFactory parserFactory) {
    super(parserFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParserType getParserType() {
    return ParserType.BINARY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canParse(IFile resource) {

    //
    if (!resource.getPath().endsWith(".class")) {
      return false;
    }

    //
    return JavaUtils.isValidJavaPackage(resource.getDirectory());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doParseResource(IContentDefinition content, IFile resource, INode resourceBean,
      IParserContext context) {

    // tag parent directory as package
    if (!context.getParentDirectoryNode().getLabels().contains(JTypeLabel.Package)) {
      context.getParentDirectoryNode().addLabel(JTypeLabel.Package);
    }

    // add CLASSFILE label
    resourceBean.addLabel(JTypeLabel.ClassFile);

    // create the visitor...
    JTypeClassVisitor visitor = new JTypeClassVisitor(this.getParserFactory());

    // ...parse the class
    ClassReader reader = new ClassReader(resource.getContent());
    reader.accept(visitor, ClassReader.EXPAND_FRAMES);

    // the constant pool may references classes that are not referenced from within the code, e.g. see
    // https://bugs.openjdk.java.net/browse/JDK-7153958
    Set<String> allReferencedTypes = visitor.getTypeLocalReferenceCache().getAllReferencedTypes();
    for (String constantPoolClass : getConstantPoolClasses(reader)) {
      if (!allReferencedTypes.contains(constantPoolClass.replace('/', '.'))) {
        visitor.getTypeLocalReferenceCache().addTypeReference(visitor.getTypeBean(),
            constantPoolClass.replace('/', '.'), JTypeModelRelationshipType.REFERENCES);
      }
    }

    // add the contained type
    resourceBean.addRelationship(visitor.getTypeBean(), CoreModelRelationshipType.CONTAINS);
  }

  /**
   * <p>
   * </p>
   *
   * @param reader
   * @return
   */
  public static Set<String> getConstantPoolClasses(ClassReader reader) {
    Set<String> strings = new HashSet<>();

    int itemCount = reader.getItemCount();
    char[] buffer = new char[reader.getMaxStringLength()];

    for (int n = 1; n < itemCount; n++) {
      int pos = reader.getItem(n);
      if (pos == 0 || reader.b[pos - 1] != 7) {
        continue;
      }

      Arrays.fill(buffer, (char) 0);
      String string = reader.readUTF8(pos, buffer);

      if (string.startsWith("[")) {
        continue;
      }
      if (string.length() < 1) {
        continue;
      }

      strings.add(string);
    }

    return strings;
  }
}
