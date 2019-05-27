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
package io.codekontor.slizaa.jtype.scanner.jdeps.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CollectionLog implements Log {

  private static final String       THREE_SPACES           = "\\u0020\\u0020\\u0020";

  private static final String       PATTERN_STRING_CLASS   = THREE_SPACES + "([\\w\\.$]*)(.*)";

  private static final String       REFERENCE_STRING_CLASS = THREE_SPACES + THREE_SPACES
      + "\\u002D\\u003E\\u0020([\\w\\.$]*)(.*)";

  static final Pattern              PATTERN_CLASS          = Pattern.compile(PATTERN_STRING_CLASS);

  static final Pattern              REFERENCE_CLASS        = Pattern.compile(REFERENCE_STRING_CLASS);

  /** - */
  private Map<String, List<String>> _result                = new HashMap<>();

  /** - */
  private List<String>              _currentReferences     = null;

  @Override
  public void info(CharSequence message) {
    //
  }

  @Override
  public void error(CharSequence message) {
    //
  }

  @Override
  public void debug(CharSequence message) {

    // class?
    Matcher matcher = REFERENCE_CLASS.matcher(message);
    if (matcher.matches() && _currentReferences != null) {
      _currentReferences.add(matcher.group(1));
      return;
    }

    // reference?
    matcher = PATTERN_CLASS.matcher(message);
    if (matcher.matches()) {
      _currentReferences = _result.computeIfAbsent(matcher.group(1), key -> new LinkedList<>());
    }
  }

  public Map<String, List<String>> getResult() {
    return _result;
  }
}