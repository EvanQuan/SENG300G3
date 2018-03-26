package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Foo[]
 *
 * @author Evan Quan
 * @version 2.0.0
 * @since 21 March 2018
 *
 */
public class TypeVisitorFooArrayTest extends TypeVisitorTest {

	private static final String type = "Foo[]";

	/**
	 * Check that instantiating an array of Foo counts a 1 reference
	 */
	@Test
	public void test_ArrayDeclarableVariableAndAllocate_Dec_0_Ref_1() {
		configureParser("public class Other {Bar[] bar = new Foo[1];}", type, 0, 1);
	}

	/**
	 * Check that declaring and instantiating an array of Foo counts as 2 references
	 */
	@Test
	public void test_ArrayDeclarableVariableAndAllocate_Dec_0_Ref_2() {
		configureParser("public class Other {Foo[] foo = new Foo[1];}", type, 0, 2);
	}

	/**
	 * Check that creating a variable of an array of Foo counts as a reference
	 */
	@Test
	public void test_ArrayDeclareVariable_Dec_0_Ref_1() {
		configureParser("public class Other {Foo[] foo;}", type, 0, 1);
	}
}
