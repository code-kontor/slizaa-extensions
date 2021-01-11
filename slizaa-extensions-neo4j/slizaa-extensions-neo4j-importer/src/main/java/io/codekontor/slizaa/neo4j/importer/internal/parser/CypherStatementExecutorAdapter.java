/**
 * slizaa-extensions-neo4j-importer - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;

import io.codekontor.slizaa.scanner.spi.parser.ICypherStatementExecutor;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CypherStatementExecutorAdapter implements ICypherStatementExecutor {

  /** - */
  private GraphDatabaseService _graphDatabaseService;

  /**
   * <p>
   * Creates a new instance of type {@link CypherStatementExecutorAdapter}.
   * </p>
   *
   * @param graphDatabaseService
   */
  public CypherStatementExecutorAdapter(GraphDatabaseService graphDatabaseService) {
    this._graphDatabaseService = checkNotNull(graphDatabaseService);
  }

  @Override
  public <T> T executeCypherStatement(String cypherStatement, Function<IResult, T> mappingFunction) {

    checkNotNull(cypherStatement);

    try {

      if (mappingFunction != null) {
        return _graphDatabaseService.executeTransactionally(cypherStatement, Collections.emptyMap(),
            mappingFunction != null ? result -> mappingFunction.apply(new ResultAdapter(result)) : null);
      }

      _graphDatabaseService.executeTransactionally(cypherStatement);
      return null;

    } catch (QueryExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static class ResultAdapter implements IResult {

    /** - */
    private org.neo4j.graphdb.Result _result;

    /**
     * <p>
     * Creates a new instance of type {@link ResultAdapter}.
     * </p>
     *
     * @param result
     */
    public ResultAdapter(org.neo4j.graphdb.Result result) {
      this._result = checkNotNull(result);
    }

    @Override
    public List<String> keys() {
      return this._result.columns();
    }

    @Override
    public Map<String, Object> single() {

      //
      Map<String, Object> result = null;

      //
      if (this._result.hasNext()) {
        result = this._result.next();
      }
      //
      else {
        throw new RuntimeException("Expected single result, but result is empty.");
      }

      //
      if (this._result.hasNext()) {
        throw new RuntimeException("Expected single result, but result contains several entries.");
      }

      //
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> list() {
      return this._result.stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> list(Function<Map<String, Object>, T> mapFunction) {
      return this._result.stream().map(mapFunction).collect(Collectors.toList());
    }
  }
}
