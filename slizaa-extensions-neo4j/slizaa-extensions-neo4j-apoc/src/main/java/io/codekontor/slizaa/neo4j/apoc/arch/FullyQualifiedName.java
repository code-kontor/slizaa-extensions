/**
 * slizaa-extensions-neo4j-apoc - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.apoc.arch;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FullyQualifiedName {

  /** - */
  private String             _fqn;

  /** - */
  private String[]           _parts;

  /** - */
  private FullyQualifiedName _parent;

  /**
   * <p>
   * Creates a new instance of type {@link FullyQualifiedName}.
   * </p>
   *
   * @param fqn
   */
  public FullyQualifiedName(String fqn) {
    _fqn = checkNotNull(fqn);
    _parts = splitFqn(fqn);
  }

  /**
   * <p>
   * Creates a new instance of type {@link FullyQualifiedName}.
   * </p>
   *
   * @param fqn
   */
  public FullyQualifiedName(String[] fqn) {
    _parts = checkNotNull(fqn);
    _fqn = concatFqn(_parts);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public FullyQualifiedName getParent() {

    if (_parent == null && hasParent()) {
      _parent = new FullyQualifiedName(Arrays.copyOf(_parts, _parts.length - 1));
    }

    return _parent;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public boolean hasParent() {
    return _parts.length > 1;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getSimpleName() {
    return _parts[_parts.length - 1];
  }


  public String getFullyQualifiedName() {
    return _fqn;
  }
  
  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String[] getParts() {
    return _parts;
  }

  @Override
  public String toString() {
    return _fqn;
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   * @return
   */
  private static String[] splitFqn(String fqn) {

    //
    while (checkNotNull(fqn).endsWith("/")) {
      fqn = fqn.substring(0, fqn.length() - 1);
    }

    //
    while (fqn.startsWith("/")) {
      fqn = fqn.substring(1, fqn.length());
    }

    //
    return fqn.split("/");
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   * @return
   */
  private static String concatFqn(String[] fqn) {

    //
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < checkNotNull(fqn).length; i++) {
      stringBuilder.append(fqn[i]);
      if (i + 1 < checkNotNull(fqn).length) {
        stringBuilder.append("/");
      }
    }
    return stringBuilder.toString();
  }
}
