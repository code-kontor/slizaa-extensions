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
package io.codekontor.slizaa.jtype.scanner.jdeps;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import io.codekontor.slizaa.jtype.scanner.jdeps.internal.JDepsWrapper;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JDepsRule implements TestRule {

  /** - */
  private Map<String, List<String>> _jdepAnalysis;

  /** - */
  private Supplier<File>            _fileSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link JDepsRule}.
   * </p>
   *
   * @param fileSupplier
   */
  public JDepsRule(Supplier<File> fileSupplier) {
    _fileSupplier = checkNotNull(fileSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        _jdepAnalysis = new JDepsWrapper().analyze(_fileSupplier.get().getAbsolutePath());

        base.evaluate();

        //
        _jdepAnalysis.clear();
        _jdepAnalysis = null;
      }
    };
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Map<String, List<String>> getJdepAnalysis() {
    return _jdepAnalysis;
  }
}
