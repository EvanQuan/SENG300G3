package test;

import org.junit.Test;

/**
 * Meta test case. Used to test test cases.
 *
 * @author Evan Quqn
 */
public class TypeVisitorMetaTest extends TypeVisitorTest {

	/**
	 * What is the expected result?
	 */
	@Test
	public void test_NestedDeclarationInAnonymousDeclaration_Dec_0_AnonDec_0_LocalDec_0_NestedDec0_Ref_0_LocalRef_0_NestedRef_0() {
		configureParser("package bar; public class Other { Bar bar = new Bar() { class Foo{} } }", "Foo", 1, 0, 0, 1, 0,
				0, 0);
	}

	/**
	 * What is the expected result?
	 */
	@Test
	public void test_NestedDeclarationInLocalDeclaration_Dec_0_AnonDec_0_LocalDec_0_NestedDec0_Ref_0_LocalRef_0_NestedRef_0() {
		configureParser("package bar; public class Other { public void method() { class Bar{ class Foo{} } } }",
				"Bar.Foo", 1, 0, 0, 1, 0, 0, 0);
	}
}
