package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Java built-in classes
 *
 * @author Evan Quan
 * @version 2.0.0
 * @since 12 March 2018
 *
 */
public class TypeVisitorBuiltInTest extends TypeVisitorTest {

	/**
	 * Check that allocating an array of String counts as a reference to java.lang.String 
	 * */
	@Test
	public void testArrayDeclarableVariableAndAllocate_Dec_0_Ref_1() {
		configureParser("public class Other {Bar[] bar = new String[1];}", "java.lang.String", 0, 1);
	}


	/**
	 * Check that declaring and allocating arrays of Strings count as references to java.lang.String
	 */
	@Test
	public void testArrayDeclarableVariableAndAllocate_Dec_0_Ref_2() {
		configureParser("public class Other {String[] str = new String[1];}", "java.lang.String", 0, 2);
	}

	/**
	 * Check that creating a variable of an array of String counts as a reference
	 */
	@Test
	public void testArrayDeclareVariable_Dec_0_Ref_1() {
		configureParser("public class Other {String[] str;}", "java.lang.String", 0, 1);
	}

	/**
	 * Check if initializing a variable of String within a for-loop counts as a
	 * reference
	 */
	@Test
	public void testForLoopInitialization_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { for (String s;;){}}}", "java.lang.String", 0, 1);
	}

	/**
	 * Check that a reference to Hash Map defaults to local HashMap with no imports
	 */
	@Test
	public void testHashMap_Dec_0_Ref_1() {
		configureParser("class Foo { HashMap map;}", "HashMap", 0, 1);
	}

	/**
	 * Check that a reference to HashMap does not default to local HashMap with
	 * java.util.HashMap import
	 */
	@Test
	public void testHashMapImported_Dec_0_Ref_0() {
		configureParser("import java.util.HashMap; class Foo { HashMap map;}", "HashMap", 0, 0);
	}

	/**
	 * Check that a reference to String defaults to java.lang.String
	 */
	@Test
	public void testJavaLangString_Dec_0_Ref_1() {
		configureParser("class Foo { String str; }", "java.lang.String", 0, 1);
	}

	/**
	 * Check that a reference to String as a generic parameter of java.util.HashMap
	 * defaults to java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedAndDeclaredHashMap_Dec_0_Ref_2() {
		configureParser(
				"import java.util.HashMap; class Foo { HashMap<String, Integer> map = new HashMap<String, Integer>();}",
				"java.lang.String", 0, 2);
	}

	/**
	 * Check that a reference to String as both generic parameters of
	 * java.util.HashMap defaults to java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedAndDeclaredHashMap_Dec_0_Ref_4() {
		configureParser(
				"import java.util.HashMap; class Foo { HashMap<String, String> map = new HashMap<String, String>();}",
				"java.lang.String", 0, 4);
	}

	/**
	 * Check that a reference to String as a generic parameter of Foo defaults to
	 * java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedAndDeclaredList_Dec_0_Ref_2() {
		configureParser("import java.util.List; class Other { List<String> list = new List<String>();}",
				"java.lang.String", 0, 2);
	}

	/**
	 * Check that a reference to String as a generic parameter of Foo defaults to
	 * java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedFoo_Dec_0_Ref_1() {
		configureParser("class Other { Foo<String> foo;}", "java.lang.String", 0, 1);
	}

	/**
	 * Check that a reference to String as a generic parameter of java.util.HashMap
	 * defaults to java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedHashMap_Dec_0_Ref_1() {
		configureParser("import java.util.HashMap; class Foo { HashMap<String, Integer> map;}", "java.lang.String", 0,
				1);
	}

	/**
	 * Check that a reference to String as a generic parameter of Foo defaults to
	 * java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedList_Dec_0_Ref_1() {
		configureParser("import java.util.List; class Other { List<String> list;}", "java.lang.String", 0, 1);
	}

	/**
	 * Check that a reference to HashMap does not default to local HashMap with
	 * java.util.* import
	 */
	@Test
	public void testJavaUtilAllImported_Dec_0_Ref_0() {
		configureParser("import java.util.*; class Foo { HashMap map;}", "HashMap", 0, 0);
	}

	/**
	 * Check that a reference to HashMap defaults to java.util.HashMap with
	 * java.util.* import
	 */
	@Test
	public void testJavaUtilAllImported_Dec_0_Ref_1() {
		configureParser("import java.util.*; class Foo { HashMap map;}", "java.util.HashMap", 0, 1);
	}

	/**
	 * Check that a reference to HashMap defaults to java.util.HashMap with
	 * java.util.HashMap import
	 */
	@Test
	public void testJavaUtilHashMapImported_Dec_0_Ref_2() {
		configureParser("import java.util.HashMap; class Foo { HashMap map;}", "java.util.HashMap", 0, 2);
	}

	/**
	 * Check that a reference to HashMap<String,Integer> defaults to
	 * java.util.HashMap with java.util.HashMap import
	 */
	@Test
	public void testJavaUtilHashMapImportedParameterized_Dec_0_Ref_2() {
		configureParser("import java.util.HashMap; class Foo { HashMap<String, Integer> map;}", "java.util.HashMap", 0,
				2);
	}

	/**
	 * Check that a reference to HashMap<String, Integer> defaults to
	 * java.util.HashMap with java.util.HashMap import
	 */
	@Test
	public void testJavaUtilHashMapImportedParameterizedAndDeclared_Dec_0_Ref_3() {
		configureParser(
				"import java.util.HashMap; class Foo { HashMap<String, Integer> map = new HashMap<String, Integer>();}",
				"java.util.HashMap", 0, 3);
	}

	/**
	 * Check if returning a static field of an array of String counts as a reference
	 */
	@Test
	public void testReturnStaticField_Dec_0_Ref_1() {
		configureParser("class Other { int length = new String[].length;}", "java.lang.String", 0, 1);
	}

	/**
	 * Check that a reference to String does not default to local String class
	 */
	@Test
	public void testString_Dec_0_Ref_0() {
		configureParser("class Foo { String str; }", "String", 0, 0);
	}

	/**
	 * Check that a reference to String as a generic parameter of Foo defaults
	 * to java.lang.String
	 */
	@Test
	public void testJavaLangStringParameterizedAndDeclaredFoo_Dec_0_Ref_2() {
		configureParser("class Other { Foo<String> foo = new Foo<String>();}", "java.lang.String", 0, 2);
	}

	/**
	 * Check that explicitly using fully qualified names works
	 */
	@Test
	public void test_ArrayList_FullyQualified_Dec_0_Ref_2() {
		configureParser("class Other {java.util.ArrayList<String> list = new java.util.ArrayList<String>():", "java.util.ArrayList", 0, 2);
	}
}
