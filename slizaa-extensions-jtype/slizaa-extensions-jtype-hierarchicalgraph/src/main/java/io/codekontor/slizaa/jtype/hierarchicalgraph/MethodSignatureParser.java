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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodSignatureParser {

  public static final String pattern = "([^\\s]+) ([^\\s]+)(\\(([^\\s]*)\\))";
  
  private Pattern r = Pattern.compile(pattern);
  
  public String parse( String line) {

    // Now create matcher object.
    Matcher m = r.matcher(line);
    if (m.find()) {
      
      String returnType = simpleName(m.group(1));
      String name = simpleName(m.group(2));

      //
      StringBuilder builder = new StringBuilder();
      String[] arguments = split(m.group(4));
      for (int i = 0; i < arguments.length; i++) {
        builder.append(simpleName(arguments[i]));
        if (i+1 < arguments.length) {
          builder.append(", ");
        }
      }
      
      return name + "(" + builder.toString() + "): " + returnType;
    } else {
      return line;
    }
  }

  private static String[] split(String names) {

    //
    if (names == null) {
      return new String[0];
    }

    //
    names = names.trim();

    //
    if (names.length() == 0) {
      return new String[0];
    }

    //
    return names.split(",");
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @return
   */
  private static String simpleName(String name) {

    //
    if (name == null) {
      return name;
    }

    //
    int lastIndex = name.lastIndexOf('.');
    if (lastIndex != -1) {
      return name.substring(lastIndex + 1, name.length());
    }

    //
    return name;
  }
}