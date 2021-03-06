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

import static io.codekontor.slizaa.scanner.testfwk.ContentDefinitionProviderFactory.simpleBinaryFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.function.Consumer;

import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.jtype.scanner.JTypeTestServerRule;
import io.codekontor.slizaa.jtype.scanner.itest.examplecode.ExampleInterface;
import io.codekontor.slizaa.jtype.scanner.model.ITypeNode;

public class ExampleCodeTest {

  /** - */
  @ClassRule
  public static JTypeTestServerRule      _server = new JTypeTestServerRule(simpleBinaryFile("jtype", "1.2.3",
      ExampleCodeTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  /** - */
  @ClassRule
  public static BoltClientConnectionRule _client = new BoltClientConnectionRule();

  @Test
  public void test_AnonymousInnerClass() {

    String cypherQuery = "MATCH (inner)-[rel:IS_INNER_CLASS_DEFINED_BY]->(outer:TypeReference) WHERE outer.fqn = 'io.codekontor.slizaa.jtype.scanner.itest.examplecode.ClassWithAnonymousInnerClass' RETURN outer.fqn, inner.fqn, inner";

    _client.getBoltClient().syncExecAndConsume(cypherQuery, result -> {

      // get the single result line
      Record record = result.single();

      assertThat(record.get("outer.fqn").asString())
          .isEqualTo("io.codekontor.slizaa.jtype.scanner.itest.examplecode.ClassWithAnonymousInnerClass");
      assertThat(record.get("inner.fqn").asString())
          .isEqualTo("io.codekontor.slizaa.jtype.scanner.itest.examplecode.ClassWithAnonymousInnerClass$1");
    });
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test_Interface() {
    this.assertTypeNode(ExampleInterface.class, node -> {
      
      // asserts
      assertThat(node.labels()).containsExactly("Type", "Interface");
      assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT, ITypeNode.SOURCE_FILE_NAME,
          ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY);

      assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleInterface.class.getName());
      assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleInterface.class.getSimpleName());
      assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
      assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleInterface.java");
      assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, true);
    });
  }

//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void test_Class() {
//
//    // get the type node
//    Node node = getTypeNode(ExampleClass.class);
//
//    // asserts
//    assertThat(node.labels()).containsExactly("Type", "Class");
//    assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT, ITypeNode.SOURCE_FILE_NAME,
//        ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY);
//
//    assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleClass.class.getName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleClass.class.getSimpleName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
//    assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleClass.java");
//    assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, false);
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void test_innerOuterClass() {
//
//    //
//    StatementResult statementResult = _client.getBoltClient()
//        .syncExecCypherQuery("MATCH (t:Type)-[rel:DEFINES_INNER_CLASS]->(target) RETURN t.fqn, target.fqn");
//
//    statementResult.forEachRemaining(rec -> System.out.println(rec.get(0) + " : " + rec.get(1)));
//
//    // //
//    // return statementResult.list(rec -> rec.get(0).asNode());
//    //
//    // // asserts
//    // assertThat(node.labels()).containsExactly("Type", "Class");
//    // assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT,
//    // ITypeNode.SOURCE_FILE_NAME,
//    // ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY);
//    //
//    // assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleClass.class.getName());
//    // assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleClass.class.getSimpleName());
//    // assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
//    // assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleClass.java");
//    // assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, false);
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void test_Enum() {
//
//    // get the type node
//    Node node = getTypeNode(ExampleEnum.class);
//
//    // asserts
//    assertThat(node.labels()).containsExactly("Type", "Enum");
//    assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT, ITypeNode.SOURCE_FILE_NAME,
//        ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY,
//        ITypeNode.SIGNATURE);
//
//    assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleEnum.class.getName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleEnum.class.getSimpleName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
//    assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleEnum.java");
//    assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, false);
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void test_Annotation() {
//
//    // get the type node
//    Node node = getTypeNode(ExampleAnnotation.class);
//
//    // asserts
//    assertThat(node.labels()).containsExactly("Type", "Annotation");
//    assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT, ITypeNode.SOURCE_FILE_NAME,
//        ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY);
//
//    assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleAnnotation.class.getName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleAnnotation.class.getSimpleName());
//    assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
//    assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleAnnotation.java");
//    assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, true);
//  }
//
//  @Test
//  public void test_Method() {
//
//    // get the type node
//    List<Node> nodes = getMethodNodes(ExampleClass.class);
//
//    for (Node node : nodes) {
//      System.out.println(node.labels());
//    }
//
//    // // asserts
//    // assertThat(node.labels()).containsExactly("Type", "Class");
//    // assertThat(node.asMap()).containsOnlyKeys(ITypeNode.CLASS_VERSION, ITypeNode.ABSTRACT,
//    // ITypeNode.SOURCE_FILE_NAME,
//    // ITypeNode.DEPRECATED, ITypeNode.FINAL, ITypeNode.FQN, ITypeNode.NAME, ITypeNode.STATIC, ITypeNode.VISIBILITY);
//    //
//    // assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleClass.class.getName());
//    // assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleClass.class.getSimpleName());
//    // assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
//    // assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleClass.java");
//    // assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, false);
//  }
//
//  /**
//   * <p>
//   *
//   * <pre>
//   * public class io.codekontor.slizaa.scanner.itest.jtype.simple.example.ExampleClass extends io.codekontor.slizaa.scanner.itest.jtype.simple.example.SuperClass implements io.codekontor.slizaa.scanner.itest.jtype.simple.example.SuperInterface
//   * </pre>
//   * </p>
//   */
//  @Test
//  public void testImplements() {
//
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name })-[rel:IMPLEMENTS]->(typeReference:TypeReference) return typeReference.fqn",
//        Collections.singletonMap("name", ExampleClass.class.getName()));
//
//    List<String> records = statementResult.list(rec -> rec.get(0).asString());
//
//    assertThat(records).containsOnly(SuperInterface.class.getName());
//  }
//
//  /**
//   * <p>
//   * Tests that the 'EXTENDS' relationship exists.
//   *
//   * <pre>
//   * public class io.codekontor.slizaa.scanner.itest.jtype.simple.example.ExampleClass extends io.codekontor.slizaa.scanner.itest.jtype.simple.example.SuperClass implements io.codekontor.slizaa.scanner.itest.jtype.simple.example.SuperInterface
//   * </pre>
//   * </p>
//   */
//  @Test
//  public void testExtends_1() {
//
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name })-[rel:EXTENDS]->(typeReference:TypeReference) return typeReference.fqn",
//        Collections.singletonMap("name", ExampleClass.class.getName()));
//
//    List<String> records = statementResult.list(rec -> rec.get(0).asString());
//
//    assertThat(records).containsOnly(SuperClass.class.getName());
//  }
//
//  /**
//   * <p>
//   * Tests that the 'EXTENDS' relationship exists.
//   *
//   * <pre>
//   * public class io.codekontor.slizaa.scanner.itest.jtype.simple.example.SuperClass
//   * </pre>
//   * </p>
//   */
//  @Test
//  public void testExtends_2() {
//
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name })-[rel:EXTENDS]->(typeReference:TypeReference) return typeReference.fqn",
//        Collections.singletonMap("name", SuperClass.class.getName()));
//
//    List<String> records = statementResult.list(rec -> rec.get(0).asString());
//
//    assertThat(records).containsOnly(Object.class.getName());
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void testAbstract_1() {
//
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name }) return t.abstract",
//        Collections.singletonMap("name", AbstractExampleClass.class.getName()));
//
//    assertThat(statementResult.single().get(0).asBoolean()).isTrue();
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void testAbstract_2() {
//
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name }) return t.abstract",
//        Collections.singletonMap("name", ExampleInterface.class.getName()));
//
//    assertThat(statementResult.single().get(0).asBoolean()).isTrue();
//  }
//
//  /**
//   * Tests that the METHOD_REFERENCE has 'IS_DEFINED_BY' and 'RETURNS' relationships:
//   *
//   * <pre>
//   * 4: invokeinterface #19,  1           // InterfaceMethod org/neo4j/graphdb/GraphDatabaseService.beginTx:()Lorg/neo4j/graphdb/Transaction;
//   * </pre>
//   */
//  @Test
//  public void testInvokesMethod() {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "MATCH (m:Method {name: 'exampleInvokesMethod'})-[:INVOKES]->(mref:MethodReference)-[rel]->(typeReference:TypeReference) return mref.name, type(rel), typeReference.fqn");
//
//    List<String[]> records = statementResult
//        .list(rec -> new String[] { rec.get(0).asString(), rec.get(1).asString(), rec.get(2).asString() });
//
//    assertThat(records).containsOnly(new String[] { "test", "IS_DEFINED_BY",
//        "io.codekontor.slizaa.jtype.scanner.itest.examplecode.ExampleClassWithArrays" });
//  }
//
//  /**
//   * <pre>
//   * private java.io.Serializable _serializable;
//   * descriptor: Ljava/io/Serializable;
//   * flags: ACC_PRIVATE
//   * </pre>
//   */
//  @Test
//  public void testField_1() {
//
//    //
//    List<Node> nodes = getFieldNodes(SimpleClassWithOneField.class);
//
//    //
//    assertThat(nodes).hasSize(1);
//    assertThat(nodes.get(0).labels()).containsExactly("Field");
//
//    assertThat(nodes.get(0).asMap()).containsOnlyKeys("accessFlags", "deprecated", "final", "fqn", "name", "static",
//        "transient", "visibility", "volatile");
//    assertThat(nodes.get(0).asMap()).containsEntry("accessFlags", "4");
//    assertThat(nodes.get(0).asMap()).containsEntry("deprecated", false);
//    assertThat(nodes.get(0).asMap()).containsEntry("final", false);
//    assertThat(nodes.get(0).asMap()).containsEntry("fqn",
//        "java.io.Serializable " + SimpleClassWithOneField.class.getName() + "._serializable");
//    assertThat(nodes.get(0).asMap()).containsEntry("name", "_serializable");
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void testField_2() {
//
//    //
//    List<Node> nodes = getFieldNodes(ExampleClass.class);
//
//    //
//    assertThat(nodes).hasSize(1);
//    assertThat(nodes.get(0).labels()).containsExactly("Field");
//    assertThat(nodes.get(0).asMap()).containsOnlyKeys("accessFlags", "deprecated", "final", "fqn", "name", "static",
//        "transient", "visibility", "volatile");
//    assertThat(nodes.get(0).asMap()).containsEntry("accessFlags", "2");
//    assertThat(nodes.get(0).asMap()).containsEntry("deprecated", false);
//    assertThat(nodes.get(0).asMap()).containsEntry("final", false);
//    assertThat(nodes.get(0).asMap()).containsEntry("fqn",
//        "java.io.Serializable " + ExampleClass.class.getName() + "._serializable");
//    assertThat(nodes.get(0).asMap()).containsEntry("name", "_serializable");
//  }
//
//  @Test
//  public void testField_3() {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient()
//        .syncExecCypherQuery("MATCH p=(:Type {name: 'FieldInstance'})-[rel]->(target) return target.fqn");
//
//    List<String> names = statementResult.list(rec -> rec.get(0).asString());
//
//    //
//    for (String name : names) {
//      System.out.println(name);
//    }
//  }
//
//  @Test
//  public void testMethodReference_1() {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "MATCH (m:Method {name: 'exampleInvokesMethod'})-[:INVOKES]->(mref:MethodReference) return mref");
//
//    List<Node> nodes = statementResult.list(rec -> rec.get(0).asNode());
//
//    //
//    for (Node node : nodes) {
//      System.out.println(node.asMap());
//    }
//
//    // assertThat(nodes).hasSize(1);
//    // assertThat(nodes.get(0).labels()).containsExactly("FieldReference");
//    // assertThat(nodes.get(0).asMap()).containsOnlyKeys("name", "type", "ownerTypeFqn", "static");
//    // assertThat(nodes.get(0).asMap()).containsEntry("name", "_serializable");
//    // assertThat(nodes.get(0).asMap()).containsEntry("type", "java.io.Serializable");
//    // assertThat(nodes.get(0).asMap()).containsEntry("ownerTypeFqn",
//    // "io.codekontor.slizaa.scanner.jtype.itest.examplecode.SimpleClassWithOneField");
//    // assertThat(nodes.get(0).asMap()).containsEntry("static", false);
//  }
//
//  /**
//   * <p>
//   * </p>
//   */
//  @Test
//  public void testFieldReference_1() {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (m:Method {name: $name})-[:READS]->(fref:FieldReference) return fref",
//        Collections.singletonMap("name", "fieldRef"));
//
//    List<Node> nodes = statementResult.list(rec -> rec.get(0).asNode());
//
//    //
//    for (Node node : nodes) {
//      System.out.println(node.asMap());
//    }
//
//    assertThat(nodes).hasSize(1);
//    assertThat(nodes.get(0).labels()).containsExactly("FieldReference");
//    assertThat(nodes.get(0).asMap()).containsOnlyKeys("fqn", "name", "type", "ownerTypeFqn", "static");
//    assertThat(nodes.get(0).asMap()).containsEntry("name", "_serializable");
//    assertThat(nodes.get(0).asMap()).containsEntry("type", "java.io.Serializable");
//    assertThat(nodes.get(0).asMap()).containsEntry("ownerTypeFqn", SimpleClassWithOneField.class.getName());
//    assertThat(nodes.get(0).asMap()).containsEntry("static", false);
//    assertThat(nodes.get(0).asMap()).containsEntry("fqn",
//        "java.io.Serializable " + SimpleClassWithOneField.class.getName() + "._serializable");
//  }
//
//  //
//  // @Test
//  // public void testArrayField() {
//  //
//  // Node node = getTypeNode(ExampleClassWithArrays.class.getName());
//  //
//  // List<Node> fields = getFields(node, "field_1");
//  // assertThat(fields.size(), is(1));
//  // assertDataTypeReference(fields.get(0), convert(JTypeModelRelationshipType.IS_OF_TYPE), "int");
//  //
//  // fields = getFields(node, "field_2");
//  // assertThat(fields.size(), is(1));
//  // assertDataTypeReference(fields.get(0), convert(JTypeModelRelationshipType.IS_OF_TYPE), "boolean");
//  // }
//  //
//  // @Test
//  // public void testArrayMethod() {
//  //
//  // Node node = getTypeNode(ExampleClassWithArrays.class.getName());
//  //
//  // List<Node> methods = getMethods(node, "test");
//  // assertThat(methods.size(), is(1));
//  // assertDataTypeReference(methods.get(0), convert(JTypeModelRelationshipType.HAS_PARAMETER), "int");
//  // }
//  //
//  // @Test
//  // public void testConstructor() {
//  //
//  // Node node = getTypeNode(ExampleClass.class.getName());
//  // assertThat(getMethods(node, "<init>").size(), is(2));
//  //
//  // for (Node methodNode : getMethods(node, "<init>")) {
//  //
//  // System.out.println(
//  // " - " + methodNode.getSingleRelationship(convert(JTypeModelRelationshipType.RETURNS), Direction.OUTGOING));
//  // }
//  // }
//
  /**
   * <p>
   * </p>
   *
   * @param clazz
   * @return
   */
  private void assertTypeNode(Class<?> clazz, Consumer<Node> consumer) {

    //
    ExampleCodeTest._client.getBoltClient().syncExecAndConsume("Match (t:Type {fqn: $name }) return t",
        Collections.singletonMap("name", clazz.getName()), result -> {
          consumer.accept(result.single().get(0).asNode());
        });
  }

//  /**
//   * <p>
//   * </p>
//   *
//   * @param clazz
//   * @return
//   */
//  private List<Node> getFieldNodes(Class<?> clazz) {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name})-[:CONTAINS]->(f:Field) return f",
//        Collections.singletonMap("name", checkNotNull(clazz).getName()));
//
//    //
//    return statementResult.list(rec -> rec.get(0).asNode());
//  }
//
//  /**
//   * <p>
//   * </p>
//   *
//   * @param clazz
//   * @return
//   */
//  private List<Node> getMethodNodes(Class<?> clazz) {
//
//    //
//    StatementResult statementResult = this._client.getBoltClient().syncExecCypherQuery(
//        "Match (t:Type {fqn: $name})-[:CONTAINS]->(m:Method) return m",
//        Collections.singletonMap("name", checkNotNull(clazz).getName()));
//
//    //
//    return statementResult.list(rec -> rec.get(0).asNode());
//  }
}
