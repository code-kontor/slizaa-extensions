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
package io.codekontor.slizaa.jtype.scanner.model;

import io.codekontor.slizaa.scanner.spi.parser.model.RelationshipType;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public enum JTypeModelRelationshipType implements RelationshipType {

  EXTENDS, IMPLEMENTS, BOUND_TO, HAS_PARAMETER, RETURNS, THROWS, IS_OF_TYPE, READS, WRITES, READS_FIELD_OF_TYPE, WRITES_FIELD_OF_TYPE, ANNOTATED_BY, DEFINES_LOCAL_VARIABLE, USES_TYPE_CONSTANT, INVOKES, INVOKES_METHOD_FROM, INVOKED_METHOD_RETURNS, INVOKED_METHOD_HAS_PARAMETER, DEFINES_INNER_CLASS, IS_INNER_CLASS_DEFINED_BY, INNER_CLASS_REFERENCES, IS_DEFINED_BY,

  //
  DEPENDS_ON,

  @Deprecated
  REFERENCES, 
}
