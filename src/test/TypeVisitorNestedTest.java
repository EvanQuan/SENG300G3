package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for nested declarations.
 *
 * @author Evan Quan
 * @version 3.0.0
 * @since 28 March 2018
 *
 */
public class TypeVisitorNestedTest extends TypeVisitorTest {

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_ClassDeclarationNested_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("public class Other { public class Foo {} }", "Other.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_ClassDeclarationNestedPackage_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("package bar; public class Other { public class Foo {} }", "bar.Other.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_ClassDeclarationNested2_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("public class Other { public class Bar { public class Foo{} } }", "Other.Bar.Foo", 1, 0, 0, 0, 1);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_ClassDeclarationNested2Package_Dec_1_Ref_0_Anon_0_Local_0_Nested_1() {
		configureParser("package bar; public class Other { public class Bar { public class Foo{} } }",
				"bar.Other.Bar.Foo", 1, 0, 0, 0, 1);
	}

}
