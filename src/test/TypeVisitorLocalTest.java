package test;

import org.junit.Test;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for local declarations.
 *
 * @author Evan Quan
 * @version 3.2.0
 * @since 2 April 2018
 *
 */
public class TypeVisitorLocalTest extends TypeVisitorTest {

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodNoParameters_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("public class Other { public void method() { class Foo{} } }", "Foo", 1, 0, 0, 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix. Package has no impact on name since it is always
	 * simple.
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodNoParametersPackage_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("package bar; public class Other { public void method() { class Foo{} } }", "Foo", 1, 0, 0, 1,
				0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters1_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("public class Other { public void method(int x) { class Foo{} }", "Foo", 1, 0, 0, 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters1Package_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("package bar; public class Other { public void method(int x) { class Foo{} }", "Foo", 1, 0, 0,
				1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("public class Other { public void method(int x, long y) { class Foo{} } }", "Foo", 1, 0, 0, 1,
				0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix. Package has no impact on name.
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2Package_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("package bar; public class Other { public void method(int x, long y) { class Foo{} } }", "Foo",
				1, 0, 0, 1, 0);
	}

	/**
	 * Check that instantiating an instance of local Foo counts for local references
	 */
	@Test
	public void test_LocalReferenceArray_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0() {
		configureParser(
				"package bar; public class Other { public void method() { class Foo{} Foo[] foo = new Foo[1]; } }",
				"Foo", 1, 0, 1, 0, 2, 2, 0);
	}

	/**
	 * Check that instantiating an instance of local Foo counts for local references
	 */
	@Test
	public void test_LocalReferenceArray2_Dec_0_AnonDec_0_LocalDec_0_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0() {
		configureParser(
				"package bar; public class Other { public void method() { class Foo{} Foo[] foo = new Foo[1]; } }",
				"Foo[]", 0, 0, 0, 0, 2, 2, 0);
	}

	/**
	 * Check that instantiating an instance of local Foo counts for local references
	 */
	@Test
	public void test_LocalReferenceConstructor_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0() {
		configureParser("package bar; public class Other { public void method() { class Foo{} Foo foo = new Foo(); } }",
				"Foo", 1, 0, 1, 0, 2, 2, 0);
	}

	/**
	 * Check that referencing local Foo in generic paramters counts for local
	 * references
	 */
	@Test
	public void test_LocalReferenceInGeneric_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0() {
		configureParser(
				"package bar; public class Other { public void method() { class Foo{} Bar<Foo> bar = new Bar<Foo>(); } }",
				"Foo", 1, 0, 1, 0, 2, 2, 0);
	}
}
