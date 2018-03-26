package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Foo declared inside another class
 *
 * @author Evan Quan
 * @version 2.1.0
 * @since 21 March 2018
 *
 */
public class TypeVisitorPrimitiveTest extends TypeVisitorTest {
	
	/**
	 * Check that declaring an int variable in hexidecimal counts as a reference
	 */
	@Test
	public void test_intHexidecimal_Dec_0_Ref_1() {
		configureParser("public class Other {int x = 0x1a;}", "int", 0, 1);
	}
	
	/**
	 * Check that being in package pack does not append package name to primitive type double
	 */
	@Test
	public void test_doubleWithPackage_Dec_0_Ref_1() {
		configureParser("package pack; public class Other {double x;}", "double", 0, 1);
	}
	
	/**
	 * Check that declaring short while importing class pack.short does not override the primitive
	 */
	@Test
	public void test_shortWithImport_Dec_0_Ref_1() {
		configureParser("import pack.short; public class Other {short x;}", "short", 0, 1);
	}
	
	/**
	 * Check that declaring a long variable and casting a short to a long counts as 2 references
	 */
	@Test
	public void test_longCast_Dec_0_Ref_2() {
		configureParser("public class Other { short s = 1; long l = (long) s;}", "long", 0, 2);
	}
	
	/**
	 * Check that declaring a float in scientific notation counts as a reference
	 */
	@Test
	public void test_floatScientificNotation_Dec_0_Ref_2() {
		configureParser("public class Other { float f = 1.23e4; }", "float", 0, 1); 
	}
	
	/**
	 * Check that declaring a byte counts as a reference
	 */
	@Test
	public void test_byteUnderscore_Dec_0_Ref_1() {
		configureParser("public class Other { byte b = 0b0010_0101; }", "byte", 0, 1); 
	}
	
	/**
	 * Check that declaring a boolean variable counts as a reference
	 */
	@Test
	public void test_booleanTrue_Dec_0_Ref_1() {
		configureParser("public class Other { boolean b = true }", "boolean", 0, 1);
	}
	
	/**
	 * Check that declaring a char variable counts as a reference
	 */
	@Test
	public void test_char_Dec_0_Ref_1() {
		configureParser("public class Other { char c = 'a'}", "char", 0, 1);
	}
	
	/**
	 * Check that attempting to declare int as a class is not successful and does not count as a declaration of int
	 */
	@Test
	public void test_declareInt_Dec_0_Ref_0() {
		configureParser("public class int {}", "int", 0, 0);
	}
}
