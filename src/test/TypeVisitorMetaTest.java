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
	private static final String type = "bar.Foo";

	@Test
	public void test_ClassDeclarationLocalToMethodWithParameters2Package_Dec_1_Ref_0() {
		configureParser("package bar; public class Other { public void method(int x, long y) { class Foo{} } }", "Foo",
				1, 0);
	}

}
