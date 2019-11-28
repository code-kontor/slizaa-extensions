/**
 * slizaa-extensions-jtype-hierarchicalgraph - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.hierarchicalgraph;

import java.util.function.Function;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.AbstractLabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.ILabelDefinitionProcessor;
import io.codekontor.slizaa.jtype.hierarchicalgraph.signatureparser.FieldSignatureParser;
import io.codekontor.slizaa.jtype.hierarchicalgraph.signatureparser.MethodSignatureParser;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JType_LabelProvider extends AbstractLabelDefinitionProvider
    implements ILabelDefinitionProvider, JType_Constants {

  /** - */
  private boolean                     _showFullyQualifiedName;

  /** - */
  private final MethodSignatureParser _methodSignatureParser;

  private final FieldSignatureParser  _fieldSignatureParser;

  /**
   * <p>
   * </p>
   *
   * @param showFullyQualifiedName
   */
  public JType_LabelProvider(boolean showFullyQualifiedName) {
    this._showFullyQualifiedName = showFullyQualifiedName;

    //
    this._methodSignatureParser = new MethodSignatureParser();
    this._fieldSignatureParser = new FieldSignatureParser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ILabelDefinitionProcessor createLabelDefinitionProcessor() {

    // @formatter:off
    return exclusiveChoice().

    // Group
        when(nodeHasLabel("Group")).then(handleGroup()).

        // Module
        when(nodeHasLabel("Module")).then(handleModule()).

        // Package
        when(nodeHasLabel("Directory")).then(handleDirectory()).

        // Resource
        when(nodeHasLabel("Resource")).then(handleResource()).

        // Type
        when(nodeHasLabel("Type")).then(handleType()).

        // Method
        when(nodeHasLabel("Method")).then(handleMethod()).

        // Field
        when(nodeHasLabel("Field")).then(handleField()).

        // all other nodes
        otherwise(setBaseImage(ICONS_OBJ_JAR_SVG).and(setLabelText(propertyValue("fqn"))));

    // @formatter:on
  }

  private ILabelDefinitionProcessor handleGroup() {
    return setBaseImage(ICONS_OBJ_FOLDER_SVG).and(setLabelText(propertyValue("name")));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected ILabelDefinitionProcessor handleModule() {
    return setBaseImage(ICONS_OBJ_JAR_SVG).and(setLabelText(propertyValue("name")));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected ILabelDefinitionProcessor handleDirectory() {

    // @formatter:off
    return exclusiveChoice().

    // Packages
        when(nodeHasLabel("Package")).then(setBaseImage(ICONS_OBJ_PACKAGE_SVG).and(
            setLabelText(propertyValue(this._showFullyQualifiedName ? "fqn" : "name", str -> str.replace('/', '.')))))
        .

        // Directories
        otherwise(setBaseImage(ICONS_OBJ_FOLDER_SVG)
            .and(setLabelText(propertyValue(this._showFullyQualifiedName ? "fqn" : "name"))));
    // @formatter:on
  }

  private ILabelDefinitionProcessor handleResource() {

    // @formatter:off
    return executeAll(

        exclusiveChoice().when(nodeHasLabel("ClassFile")).then(setBaseImage(ICONS_OBJ_CLASSFILE_SVG))
            .otherwise(setBaseImage(ICONS_OBJ_FILE_SVG)),

        setLabelText(propertyValue("name")));
    // @formatter:on
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected ILabelDefinitionProcessor handleType() {

    // @formatter:off
    return executeAll(

        setLabelText(propertyValue("name")), setIsOverlayImage(true),

        /*
         * when(nodeHasProperty("final")) .then(setOverlayImage(ICO, OverlayPosition.TOP_RIGHT)),
         */

        when(nodeHasLabel("Class")).then(setBaseImage(ICONS_OBJ_CLASS_SVG)),
        when(nodeHasLabel("Annotation")).then(setBaseImage(ICONS_OBJ_ANNOTATION_SVG)),
        when(nodeHasLabel("Enum")).then(setBaseImage(ICONS_OBJ_ENUM_SVG)),
        when(nodeHasLabel("Interface")).then(setBaseImage(ICONS_OBJ_INTERFACE_SVG)));
    // @formatter:on
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected ILabelDefinitionProcessor handleMethod() {

    // @formatter:off
    return executeAll(

        setLabelText(convertMethodSignature(propertyValue("fqn"))), setIsOverlayImage(true),

        when(nodeHasPropertyWithValue("visibility", "public")).then(setBaseImage(ICONS_OBJ_METHOD_PUBLIC_SVG)),
        when(nodeHasPropertyWithValue("visibility", "private")).then(setBaseImage(ICONS_OBJ_METHOD_PRIVATE_SVG)),
        when(nodeHasPropertyWithValue("visibility", "protected")).then(setBaseImage(ICONS_OBJ_METHOD_PROTECTED_SVG)),
        when(nodeHasPropertyWithValue("visibility", "default")).then(setBaseImage(ICONS_OBJ_METHOD_DEFAULT_SVG)),

        when(nodeHasLabel("Constructor")).then(setOverlayImage(ICONS_OVR_CONSTRUCTOR_SVG, OverlayPosition.TOP_RIGHT)),

        when(nodeHasPropertyWithValue("static", "true"))
            .then(setOverlayImage(ICONS_OVR_STATIC_SVG, OverlayPosition.TOP_RIGHT))

    );
    // @formatter:on
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected ILabelDefinitionProcessor handleField() {

    // @formatter:off
    return executeAll(

        setLabelText(convertFieldSignature(propertyValue("fqn"))), setIsOverlayImage(true),

        when(nodeHasPropertyWithValue("visibility", "public")).then(setBaseImage(ICONS_OBJ_FIELD_PUBLIC_SVG)),
        when(nodeHasPropertyWithValue("visibility", "private")).then(setBaseImage(ICONS_OBJ_FIELD_PRIVATE_SVG)),
        when(nodeHasPropertyWithValue("visibility", "protected")).then(setBaseImage(ICONS_OBJ_FIELD_PROTECTED_SVG)),
        when(nodeHasPropertyWithValue("visibility", "default")).then(setBaseImage(ICONS_OBJ_FIELD_DEFAULT_SVG)),

        when(nodeHasPropertyWithValue("static", "true"))
            .then(setOverlayImage(ICONS_OVR_STATIC_SVG, OverlayPosition.TOP_RIGHT)));

    // @formatter:on
  }

  /**
   * <p>
   * </p>
   *
   * @param function
   * @return
   */
  protected Function<HGNode, String> convertMethodSignature(Function<HGNode, String> function) {
    return (node) -> this._methodSignatureParser.parse(function.apply(node));
  }

  protected Function<HGNode, String> convertFieldSignature(Function<HGNode, String> function) {
    return (node) -> this._fieldSignatureParser.parse(function.apply(node));
  }
}
