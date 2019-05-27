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

public interface IMethodNode {

  public static final String NATIVE          = "native";

  public static final String FQN             = "fqn";

  public static final String NAME            = "name";

  public static final String FINAL           = "final";

  public static final String STATIC          = "static";

  public static final String VISIBILITY      = "visibility";

  public static final String ABSTRACT        = "abstract";

  public static final String SYNTHETIC       = "synthetic";

  // TODO Relationship
  public static final String PARAMETER_INDEX = "parameterIndex";

  public static final String PARAMETER_COUNT = "parameterCount";
}
