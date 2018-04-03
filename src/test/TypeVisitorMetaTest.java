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

	@Test
	public void test_NestedReferenceArray2_Dec_0_AnonDec_0_LocalDec_0_NestedDec_0_Ref_2_AnonRef_0_LocalRef_0_NestedRef_2() {
		configureParser(
				"package bar; public class Other { public class Foo {} public void method() { Foo[] foo = new Foo[1]; } }",
				"bar.Other.Foo[]", 0, 0, 0, 0, 2, 0, 2);
	}
}
