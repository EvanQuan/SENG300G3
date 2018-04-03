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
	 * Check that instantiating an instance of local Foo counts for local references
	 */
	// @Test
	// public void
	// test_LocalReferenceArray_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0()
	// {
	// configureParser(
	// "package bar; public class Other { public void method() { class Foo{} Foo[]
	// foo = new Foo[]; } }",
	// "Foo", 1, 0, 1, 0, 2, 2, 0);
	// }

	// /**
	// * Check that instantiating an instance of local Foo counts for local
	// references
	// */
	@Test
	public void test_LocalReferenceArray2_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0() {
		configureParser(
				"package bar; public class Other { public void method() { class Foo{} Foo[] foo = new Foo[]; } }",
				"Foo[]", 0, 0, 0, 0, 2, 2, 0);
	}
	//
	//
	// /**
	// * Check that referencing local Foo in generic paramters counts for local
	// * references
	// */
	// @Test
	// public void
	// test_LocalReferenceInGeneric_Dec_1_AnonDec_0_LocalDec_1_NestedDec_0_Ref_1_AnonRef_0_LocalRef_1_NestedRef_0()
	// {
	// configureParser(
	// "package bar; public class Other { public void method() { class Foo{}
	// Bar<Foo> bar = new Bar<Foo>(); } }",
	// "Foo", 1, 0, 1, 0, 2, 2, 0);
	// }

}
