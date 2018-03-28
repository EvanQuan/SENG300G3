package test;

import org.junit.Test;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for anonymous declarations.
 *
 * @author Evan Quan
 * @version 3.0.0
 * @since 28 March 2018
 *
 */
public class TypeVisitorAnonymousTest extends TypeVisitorTest {

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethod_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser(
				"public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"Foo", 0, 1, 1, 0, 0);
	}

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethodPackage_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser(
				"package bar; public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"bar.Foo", 0, 1, 1, 0, 0);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclaration_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser("public class Other { Bar bar = new Foo() { public void method(){} }; } ", "Foo", 0, 1, 1, 0, 0);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclarationPackage_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser("package bar; public class Other { Bar bar = new Foo() { public void method(){} }; } ",
				"bar.Foo", 0, 1, 1, 0, 0);
	}

}
