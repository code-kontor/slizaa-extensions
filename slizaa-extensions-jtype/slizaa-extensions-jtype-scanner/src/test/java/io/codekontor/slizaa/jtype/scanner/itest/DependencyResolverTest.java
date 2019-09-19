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
package io.codekontor.slizaa.jtype.scanner.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

public class DependencyResolverTest {

  @ClassRule
  public static JTypeTestServerRule _server = new JTypeTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.netflix.eureka", "eureka-core", "1.8.2" },
          new String[] { "com.netflix.eureka", "eureka-client", "1.8.2" }));

  @Rule
  public BoltClientConnectionRule         _client = new BoltClientConnectionRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testDependencyResolver() {

    // check type references
    StatementResult statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (tref:TypeReference)-[rel:BOUND_TO {derived:true}]->(t:Type) RETURN count(rel)");
    assertThat(statementResult.single().get("count(rel)").asInt()).isEqualTo(2378);

    statementResult = this._client.getBoltClient().syncExecCypherQuery(
        "MATCH p=(t1:Type)-[:DEPENDS_ON]->(tref:TypeReference)-[:BOUND_TO {derived:true}]->(t2:Type) RETURN count(p)");
    assertThat(statementResult.single().get("count(p)").asInt()).isEqualTo(2032);

    statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH p=(sourceNode)-[rel]->(tref:TypeReference)-[:BOUND_TO]->(t:Type) RETURN count(p)");
    assertThat(statementResult.single().get("count(p)").asInt()).isEqualTo(31620);

    // check method references
    statementResult = this._client.getBoltClient().syncExecCypherQuery(
        "MATCH (mref:MethodReference)-[rel:BOUND_TO {derived:true}]->(m:Method) RETURN count(rel)");
    assertThat(statementResult.single().get("count(rel)").asInt()).isEqualTo(2540);

    statementResult = this._client.getBoltClient().syncExecCypherQuery(
        "MATCH p=(sourceNode)-[rel]->(mref:MethodReference)-[:BOUND_TO]->(method:Method) RETURN count(p)");
    assertThat(statementResult.single().get("count(p)").asInt()).isEqualTo(4129);

    // check field references
    statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (fref:FieldReference)-[rel:BOUND_TO {derived:true}]->(f:Field) RETURN count(rel)");
    assertThat(statementResult.single().get("count(rel)").asInt()).isEqualTo(1492);

    statementResult = this._client.getBoltClient().syncExecCypherQuery(
        "MATCH p=(sourceNode)-[rel]->(fref:FieldReference)-[:BOUND_TO]->(f:Field) RETURN count(p)");
    assertThat(statementResult.single().get("count(p)").asInt()).isEqualTo(5333);

    // unbound type references (3514)
    statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (tref:TypeReference) WHERE NOT (tref)-[:BOUND_TO]->(:Type) RETURN count(tref)");
    assertThat(statementResult.single().get("count(tref)").asInt()).isEqualTo(3477);

    // unbound method references (3549)
    statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (mref:MethodReference) WHERE NOT (mref)-[:BOUND_TO]->(:Method) RETURN count(mref)");
    assertThat(statementResult.single().get("count(mref)").asInt()).isEqualTo(3549);

    // unbound field references (150)
    statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (fref:FieldReference) WHERE NOT (fref)-[:BOUND_TO]->(:Field) RETURN count(fref)");
    assertThat(statementResult.single().get("count(fref)").asInt()).isEqualTo(150);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testPrimitiveDataType() {

    // check type references
    StatementResult statementResult = this._client.getBoltClient()
        .syncExecCypherQuery("MATCH (p:PrimitiveDataType) RETURN count(p)");
    assertThat(statementResult.single().get("count(p)").asInt()).isEqualTo(8);
  }
}
