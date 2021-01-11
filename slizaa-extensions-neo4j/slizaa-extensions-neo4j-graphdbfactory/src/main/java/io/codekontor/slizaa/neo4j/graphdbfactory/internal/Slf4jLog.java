/**
 * slizaa-extensions-neo4j-graphdbfactory - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.graphdbfactory.internal;

import java.util.function.Consumer;

import org.neo4j.logging.AbstractLog;
import org.neo4j.logging.Log;
import org.neo4j.logging.Logger;

/**
 * An adapter from a {@link org.slf4j.Logger} to a {@link Log} interface
 */
public class Slf4jLog extends AbstractLog {
  private final Object           _lock;

  private final org.slf4j.Logger _slf4jLogger;

  /**
   * @param slf4jLogger
   *          the SLF4J logger to output to
   */
  public Slf4jLog(final org.slf4j.Logger slf4jLogger) {
    this._lock = this;
    this._slf4jLogger = slf4jLogger;
  }

  @Override
  public boolean isDebugEnabled() {
      return false;
  }

  @Override
  public void debug(String message) {
      _slf4jLogger.debug(message);
  }

  @Override
  public void debug(String message, Throwable throwable) {
      _slf4jLogger.debug(message, throwable);
  }

  @Override
  public void debug(String format, Object... arguments) {
      _slf4jLogger.debug(convertFormat(format), arguments);
  }

  @Override
  public void info(String message) {
      _slf4jLogger.info(message);
  }

  @Override
  public void info(String message, Throwable throwable) {
      _slf4jLogger.info(message, throwable);
  }

  @Override
  public void info(String format, Object... arguments) {
      _slf4jLogger.info(convertFormat(format), arguments);
  }

  @Override
  public void warn(String message) {
      _slf4jLogger.warn(message);
  }

  @Override
  public void warn(String message, Throwable throwable) {
      _slf4jLogger.warn(message, throwable);
  }

  @Override
  public void warn(String format, Object... arguments) {
      _slf4jLogger.warn(convertFormat(format), arguments);
  }

  @Override
  public void error(String message) {
      _slf4jLogger.error(message);
  }

  @Override
  public void error(String message, Throwable throwable) {
      _slf4jLogger.error(message, throwable);
  }

  @Override
  public void error(String format, Object... arguments) {
      _slf4jLogger.error(convertFormat(format), arguments);
  }

  @Override
  public void bulk(Consumer<Log> consumer) {
    synchronized (_lock) {
      consumer.accept(this);
    }
  }

  private String convertFormat(String format) {
    return format.replace("%s", "{}");
  }
}
