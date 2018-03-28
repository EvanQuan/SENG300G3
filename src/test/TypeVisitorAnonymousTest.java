package test;

import org.junit.Test;

public class TypeVisitorAnonymousTest extends TypeVisitorTest {

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethod_Dec_0_Ref_1() {
		configureParser(
				"public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"Foo", 0, 1);
	}

	/**
	 * Check that instantiating an anonymous class as an argument in for a method
	 * call counts as a reference to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousInMethodPackage_Dec_0_Ref_1() {
		configureParser(
				"package bar; public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"bar.Foo", 0, 1);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other { Bar bar = new Foo() { public void method(){} }; } ", "Foo", 0, 1);
	}

	/**
	 * Check that instantiating an anonymous class for a field counts as a reference
	 * to the Anonymous class's parent
	 */
	@Test
	public void test_ClassDeclarationAnonymousFieldDeclarationPackage_Dec_0_Ref_1() {
		configureParser("package bar; public class Other { Bar bar = new Foo() { public void method(){} }; } ",
				"bar.Foo", 0, 1);
	}

}
