package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Foo declared inside another class
 *
 * @author Evan Quan
 * @version 2.0.3
 * @since 22 March 2018
 *
 */
public class TypeVisitorInnerFooTest extends TypeVisitorTest {

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_ClassDeclarationNested_Dec_1_Ref_0() {
		configureParser("public class Other { public class Foo {} }", "Other.Foo", 1, 0);
	}

	/**
	 * Check that declaring a nested class has the outer class prefix
	 */
	@Test
	public void test_ClassDeclarationNestedPackage_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public class Foo {} }", "bar.Other.Foo", 1, 0);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_ClassDeclarationNested2_Dec_1_Ref_0() {
		configureParser("public class Other { public class Bar { public class Foo{} } }", "Other.Bar.Foo", 1, 0);
	}

	/**
	 * Check that declaring a double nested class has both out class prefixes
	 */
	@Test
	public void test_ClassDeclarationNested2Package_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public class Bar { public class Foo{} } }", "bar.Other.Bar.Foo", 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodNoParameters_Dec_1_Ref_0() {
		configureParser("public class Other { public void method() { class Foo{} } }", "Foo", 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix. Package has no impact on name since it is always simple.
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodNoParametersPackage_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public void method() { class Foo{} } }", "Foo", 1, 0);
	}
	

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters1_Dec_1_Ref_0() {
		configureParser("public class Other { public void method(int x) { class Foo{} }", "Foo", 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters1Package_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public void method(int x) { class Foo{} }", "Foo", 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2_Dec_1_Ref_0() {
		configureParser("public class Other { public void method(int x, long y) { class Foo{} } }", "Foo", 1, 0);
	}

	/**
	 * Check that declaring a local class to a method has the method name with parenthesizes as a prefix.
	 * Package has no impact on name.
	 */
	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2Package_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public void method(int x, long y) { class Foo{} } }", "Foo", 1, 0);
	}
	
	/**
	 * Check that instantiating an anonymous class as an argument in for a method call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethod_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ", "Foo", 0, 1);
	}

	/**
	 * Check that instantiating an anonymous class as an argument in for a method call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethodPackage_Dec_0_Ref_1() {
		configureParser("package bar; public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ", "bar.Foo", 0, 1);
	}
	
	/**
	 * Check that instantiating an anonymous class for a field counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other { Bar bar = new Foo() { public void method(){} }; } ", "Foo", 0, 1);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclarationPackage_Dec_0_Ref_1() {
		configureParser("package bar; public class Other { Bar bar = new Foo() { public void method(){} }; } ", "bar.Foo", 0, 1);
	}

}
