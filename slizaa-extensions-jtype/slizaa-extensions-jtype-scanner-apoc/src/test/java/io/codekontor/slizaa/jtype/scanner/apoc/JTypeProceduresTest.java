/**
 * slizaa-extensions-jtype-scanner-apoc - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.scanner.apoc;

import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.multipleBinaryMvnArtifacts;
import static org.assertj.core.api.Assertions.assertThat;

import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import org.neo4j.driver.v1.StatementResult;

public class JTypeProceduresTest {

    @ClassRule
    public static JTypeTestServerRule _server = new JTypeTestServerRule(
            multipleBinaryMvnArtifacts(new String[]{"com.netflix.eureka", "eureka-core", "1.8.2"},
                    new String[]{"com.netflix.eureka", "eureka-client", "1.8.2"}));
            //.withExtensionClass(JTypeProcedures.class);

    @Rule
    public BoltClientConnectionRule _client = new BoltClientConnectionRule();

    /**
     * <p>
     * </p>
     */
    @Test
    public void testJTypeProcedures() {


        StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery("CALL slizaa.jtype.createMissingTypes()");

        statementResult = this._client.getBoltClient().syncExecCypherQuery("Match(t:MissingType) return count(t) as count");
        assertThat(statementResult.single().get("count").asInt()).isEqualTo(904);

        statementResult = this._client.getBoltClient().syncExecCypherQuery("Match(t:MissingType) return t.name, t.fqn");
        statementResult.forEachRemaining(r -> {
                    assertThat(r.get("t.name").asString()).isNotEmpty();
                    assertThat(r.get("t.fqn").asString()).isNotEmpty();
                }
        );
    }
}
