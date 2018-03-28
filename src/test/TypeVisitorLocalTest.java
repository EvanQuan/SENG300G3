package test;

import org.junit.Test;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for local declarations.
 *
 * @author Evan Quan
 * @version 3.0.0
 * @since 28 March 2018
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
		configureParser("package bar; public class Other { public void method() { class Foo{} } }", "Foo", 1, 0, 0, 1, 0);
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
		configureParser("package bar; public class Other { public void method(int x) { class Foo{} }", "Foo", 1, 0, 0, 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with
	 * parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2_Dec_1_Ref_0_Anon_0_Local_1_Nested_0() {
		configureParser("public class Other { public void method(int x, long y) { class Foo{} } }", "Foo", 1, 0, 0, 1, 0);
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
}
