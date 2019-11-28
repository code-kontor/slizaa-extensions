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
package io.codekontor.slizaa.jtype.hierarchicalgraph.signatureparser;

public class ParserUtil {


    public static String[] split(String value, String regex) {

        //
        if (value == null) {
            return new String[0];
        }

        //
        value = value.trim();

        //
        if (value.length() == 0) {
            return new String[0];
        }

        //
        return value.split(regex);
    }

    /**
     * <p>
     * </p>
     *
     * @param name
     * @return
     */
    public static String simpleName(String name) {

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
