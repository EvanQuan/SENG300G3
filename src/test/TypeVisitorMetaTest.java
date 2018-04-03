package test;

import org.junit.Test;

/**
 * Meta test case. Used to test test cases.
 *
 * @author Evan Quqn
 * @version 1.0.0
 * @since 28 March 2018
 *
 */
public class TypeVisitorMetaTest extends TypeVisitorTest {

	/**
	 * Check that anonymous class declarations can be tracked inside anonymous class
	 * declarations. Since anonymous class declarations are tracked by parent
	 * constructor, two anonymous class declarations that share the same parent are
	 * counted as the "same" declaration (so multiple of the same count)
	 */
	// @Test
	// public void
	// test_ClassDeclarationAnonymousInAnonymousClassDeclaration_Dec_0_AnonDec_2_LocalDec_0_NestedDec_0_Ref_4_LocalRef_0_NestedDec_0()
	// {
	// configureParser("package bar; public class Other { Foo foo; public void
	// method() {foo = new Foo() {} } }",
	// "bar.Foo", 0, 2, 0, 0, 4, 0, 0);
	// }

	@Test
	public void test_ClassDeclarationAnonymousInMethod_Dec_0_Ref_1_Anon_1_Local_0_Nested_0() {
		configureParser(
				"public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo() {public void fooMethod(){}} ); } } ",
				"Foo", 0, 1, 1, 0, 0);
	}
}
