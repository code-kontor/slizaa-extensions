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

public class JavaUtils {

  /**
   * <p>
   * </p>
   *
   * @param path
   * @return
   */
  public static boolean isValidJavaPackage(String path) {

    checkNotNull(path);

    //
    String[] elements = path.split("/");

    //
    for (int i = 0; i < elements.length - 1; i++) {

      String element = elements[i];

      if (!isValidJavaIdentifier(element)) {
        return false;
      }
    }

    //
    return true;
  }

  /**
   * <p>
   * </p>
   *
   * @param directory
   * @return
   */
  public static String getPackageNameFromDirectory(String directory) {
    return directory.replace('/', '.');
  }

  /**
   * <p>
   * </p>
   * 
   * @param s
   * @return
   */
  public final static boolean isValidJavaIdentifier(String s) {

    // an empty or null string cannot be a valid identifier
    if (s == null || s.length() == 0) {
      return false;
    }

    char[] c = s.toCharArray();
    if (!Character.isJavaIdentifierStart(c[0])) {
      return false;
    }

    for (int i = 1; i < c.length; i++) {
      if (!Character.isJavaIdentifierPart(c[i])) {
        return false;
      }
    }

    return true;
  }
}
