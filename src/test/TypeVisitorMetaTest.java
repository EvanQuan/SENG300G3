package test;

import org.junit.Test;

/**
 * Meta test case. Used to test test cases.
 *
 * @author Evan Quqn
 */
public class TypeVisitorMetaTest extends TypeVisitorTest {

	@Test
	public void test_ImportFromDefaultPackageFieldDeclaration_Dec_0_Ref_2() {
		configureParser("package bar; import Foo; public class Other { private Foo foo; }", "Foo", 0, 2);
	}
}
