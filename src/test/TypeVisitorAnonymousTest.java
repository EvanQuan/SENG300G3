package test;

import org.junit.Test;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for anonymous declarations.
 *
 * @author Evan Quan
 * @version 3.3.0
 * @since 3 April 2018
 *
 */
public class TypeVisitorAnonymousTest extends TypeVisitorTest {

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclaration_Dec_0_Ref_1_AnonDec_1_LocalDec_0_NestedDec_0() {
		configureParser("public class Other { Bar bar = new Foo() { public void method(){} }; } ", "Foo", 0, 1, 1, 0,
				0);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclarationPackage_Dec_0_AnonDec_1_LocalDec_0_NestedDec_0_Ref_1_LocalRef_0_NestedRef_0() {
		configureParser("package bar; public class Other { Bar bar = new Foo() { public void method(){} }; } ",
				"bar.Foo", 0, 1, 0, 0, 1, 0, 0);
	}

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationInMethodDeclaration_Dec_0_Ref_1_AnonDec_1_LocalDec_0_NestedDec_0() {
		configureParser(
				"public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"Foo", 0, 1, 1, 0, 0);
	}

	/**
	 * Check that declaring an anonymous class of a a local class references the
	 * local class (through the constructor) and does not itself count as a local
	 * class
	 */
	@Test
	public void test_ClassDeclarationInLocalMethodDeclaration_Dec_1_AnonDec_0_LocalDec_0_Ref_0_LocalRef_0_NestedRef_0() {
		configureParser(true, "public class Other { public void method() { class Foo {} Foo foo = new Foo() {}; } }", "Foo",
				1, 1, 1, 0, 2, 2, 0);
	}

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationInMethodPackage_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser(
				"package bar; public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"bar.Foo", 0, 1, 1, 0, 0);
	}

	/**
	 * Check that declaring an anonymous class inside an anonymous class
	 * declaration, both reference the shared parent, and each count as an anonymous
	 * class declaration
	 */
	@Test
	public void test_ClassDeclarationInAnonymousClassDeclaration_Dec_0_AnonDec_2_LocalDec_0_NestedDec_0_Ref_4_LocalRef_0_NestedDec_0() {
		configureParser("package bar; public class Other { Foo foo = new Foo() { Foo foo = new Foo() {}; }; }",
				"bar.Foo", 0, 2, 0, 0, 4, 0, 0);
	}
}
