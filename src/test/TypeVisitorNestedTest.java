package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for nested declarations.
 *
 * @author Evan Quan
 * @version 3.1.0
 * @since 2 April 2018
 *
 */
public class TypeVisitorNestedTest extends TypeVisitorTest {

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_NestedDeclaration_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("public class Other { public class Foo {} }", "Other.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_NestedDeclaration2_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("public class Other { public class Bar { public class Foo{} } }", "Other.Bar.Foo", 1, 0, 0, 0,
				1);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_NestedDeclaration2Package_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("package bar; public class Other { public class Bar { public class Foo{} } }",
				"bar.Other.Bar.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_NestedDeclarationPackage_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("package bar; public class Other { public class Foo {} }", "bar.Other.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that instantiating an array of nested Foo through it's constructor
	 * counts for nested references
	 */
	@Test
	public void test_NestedReferenceArray_Dec_1_AnonDec_0_LocalDec_0_NestedDec_1_Ref_1_AnonRef_0_LocalRef_0_NestedRef_2() {
		configureParser(
				"package bar; public class Other { public class Foo {} public void method() { Foo[] foo = new Foo[1]; } }",
				"bar.Other.Foo", 1, 0, 0, 1, 1, 0, 2);
	}

	/**
	 * Check that instantiating an array of nested Foo through it's constructor
	 * counts for nested references
	 */
	@Test
	public void test_NestedReferenceArray2_Dec_1_AnonDec_0_LocalDec_0_NestedDec_1_Ref_1_AnonRef_0_LocalRef_0_NestedRef_2() {
		configureParser(
				"package bar; public class Other { public class Foo {} public void method() { Foo[] foo = new Foo[1]; } }",
				"bar.Other.Foo[]", 1, 0, 0, 1, 1, 0, 2);
	}

	/**
	 * Check that instantiating an instance of nested Foo through it's constructor
	 * counts for nested references
	 */
	@Test
	public void test_NestedReferenceConstructor_Dec_1_AnonDec_0_LocalDec_0_NestedDec_1_Ref_1_AnonRef_0_LocalRef_0_NestedRef_2() {
		configureParser(
				"package bar; public class Other { public class Foo {} public void method() { Foo foo = new Foo(); } }",
				"bar.Other.Foo", 1, 0, 0, 1, 1, 0, 2);
	}

	/**
	 * Check that referencing nested Foo in generic parameters counts for nested
	 * references
	 */
	@Test
	public void test_NestedReferenceInGeneric_Dec_1_AnonDec_0_LocalDec_0_NestedDec_1_Ref_1_AnonRef_0_LocalRef_0_NestedRef_2() {
		configureParser(
				"package bar; public class Other { public class Foo {} public void method() { Bar<Foo> bar = new Bar<Foo>(); } }",
				"bar.Other.Foo", 1, 0, 0, 1, 1, 0, 2);
	}
}
