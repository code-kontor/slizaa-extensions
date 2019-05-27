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

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaTypeUtils {

  /**
   * <p>
   * </p>
   * 
   * @param fqn
   * @return
   */
  public static String getSimpleName(String fullyQualifiedName) {
    checkNotNull(fullyQualifiedName);

    return fullyQualifiedName.lastIndexOf('.') != -1 ? fullyQualifiedName
        .substring(fullyQualifiedName.lastIndexOf('.') + 1) : fullyQualifiedName;
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static boolean isLocalOrAnonymousTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    return fullQualifiedName.matches(".*\\$\\d.*");
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static boolean isInnerTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    return fullQualifiedName.matches(".*\\$.*");
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static String getEnclosingNonInnerTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    // local or anonymous?
    if (isInnerTypeName(fullQualifiedName)) {

      String[] parts = fullQualifiedName.split("\\$");
      return parts[0];

    } else {
      return fullQualifiedName;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static String getEnclosingNonLocalAndNonAnonymousTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    // local or anonymous?
    if (isLocalOrAnonymousTypeName(fullQualifiedName)) {

      String[] parts = fullQualifiedName.split("\\$\\d");
      return parts[0];

    } else {
      return fullQualifiedName;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param classFilePath
   * @return
   */
  public static String convertToFullyQualifiedName(String classFilePath) {
    return convertToFullyQualifiedName(classFilePath, ".class");
  }

  /**
   * <p>
   * </p>
   * 
   * @param classFilePath
   * @param postfix
   * @return
   */
  public static String convertToFullyQualifiedName(String classFilePath, String postfix) {

    String fullyQualifiedName = classFilePath.substring(0, classFilePath.length() - postfix.length());

    if (fullyQualifiedName.startsWith("/")) {
      fullyQualifiedName = fullyQualifiedName.substring(1);
    }

    fullyQualifiedName = fullyQualifiedName.replace('/', '.');

    return fullyQualifiedName;
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullyQualifiedName
   * @return
   */
  public static String convertFromFullyQualifiedName(String fullyQualifiedName) {

    String result = fullyQualifiedName.replace('.', '/');

    return result + ".class";
  }
}
