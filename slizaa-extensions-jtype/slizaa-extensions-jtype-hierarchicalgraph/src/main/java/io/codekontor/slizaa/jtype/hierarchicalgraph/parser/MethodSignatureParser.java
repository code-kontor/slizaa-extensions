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
package io.codekontor.slizaa.jtype.hierarchicalgraph.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodSignatureParser {

  public static final String pattern = "([^\\s]+) ([^\\s]+)(\\(([^\\s]*)\\))";
  
  private Pattern r = Pattern.compile(pattern);
  
  public String parse( String line) {

    Matcher m = r.matcher(line);
    if (m.find()) {
      
      String returnType = ParserUtil.simpleName(m.group(1));
      String name = ParserUtil.simpleName(m.group(2));
      boolean isConstructor = false;
      if ("<init>".equals(name)) {
        isConstructor = true;
        name = typeName(m.group(2), name);
      }

      //
      StringBuilder builder = new StringBuilder();
      String[] arguments = ParserUtil.split(m.group(4), ",");
      for (int i = 0; i < arguments.length; i++) {
        builder.append(ParserUtil.simpleName(arguments[i]));
        if (i+1 < arguments.length) {
          builder.append(", ");
        }
      }

      if (isConstructor) {
        return name + "(" + builder.toString() + ")";
      } else {
        return name + "(" + builder.toString() + "): " + returnType;
      }
    } else {
      return line;
    }
  }

  private static String typeName(String fqn, String methodName ) {

    //
    if (fqn == null) {
      return fqn;
    }

    String result = null;

    //
    int lastIndex = fqn.lastIndexOf('.' + methodName);
    if (lastIndex != -1) {
      result = fqn.substring(0, lastIndex);
    }

    lastIndex = result.lastIndexOf('.');
    if (lastIndex != -1) {
      result = result.substring(lastIndex+1);
    }

    //
    return result;
  }
}