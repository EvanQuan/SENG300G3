package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for bar.Foo
 *
 * @author Evan Quan
 * @version 3.3.0
 * @since 5 April 2018
 *
 */
public class TypeVisitorPackageFooTest extends TypeVisitorTest {

	/**
	 * Foo class of package bar
	 */
	private static final String type = "other.bar.Foo";

	/**
	 * Check that declaring parameterized Foo works. More importantly, all following
	 * references to generic E do not throw a NullPointerException.
	 */
	@Test
	public void test_DeclarationParameterized_Dec_1_Ref_0() {
		configureParser("package other.bar; public class Foo<E> implements Iterable<E> { public E m(){} }", type, 1, 0);
	}

	/**
	 * Check that using a marker annotation fully qualified name accounts for
	 * package
	 */
	@Test
	public void test_MarkerAnnotation_CurrentPackage_Dec_0_Ref_1() {
		configureParser("package other.bar; public class Other { @Foo public void method{} }", type, 0, 1);
	}

	/**
	 * Check that importing a marker annotation keeps the fully qualified name and
	 * overrides package qualifier
	 */
	@Test
	public void test_ImportedMarkerAnnotationAndUse_Dec_0_Ref_2() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo public void method{} }", type,
				0, 2);
	}

	@Test
	public void test_ImportedMarkerAnnotationAndUse_FalsePositive_Dec_0_Ref_0() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo public void method{} }", "other.Foo",
				0, 0);
	}

	@Test
	public void test_ImportedSingleMemberAnnotationAndUse_Dec_0_Ref_2() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo(0) public void method{} }", type,
				0, 2);
	}

	@Test
	public void test_ImportedSingleMemberAnnotationAndUse_FalsePositive_Dec_0_Ref_0() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo(0) public void method{} }", "other.Foo",
				0, 0);
	}

	@Test
	public void test_ImportedNormalAnnotationAndUse_Dec_0_Ref_2() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo(expected = true) public void method{} }", type,
				0, 2);
	}

	@Test
	public void test_ImportedNormalAnnotationAndUse_FalsePositive_Dec_0_Ref_0() {
		configureParser("package other; import other.bar.Foo; public class Other { @Foo(expected = true) public void method{} }", "other.Foo",
				0, 0);
	}

	/**
	 * Check that explicitly using the fully qualified name for marker annotation
	 * works
	 */
	@Test
	public void test_MarkerAnnotation_Qualified_Dec_0_Ref_1() {
		configureParser("package other; public class Other { @other.bar.Foo public void method{} }", type, 0, 1);
	}

	/**
	 * Check that calling a static field which returns and stores a value counts as
	 * a reference
	 */
	@Test
	public void test_ReturnStaticField_Dec_0_Ref_1() {
		configureParser("package other.bar; public class Other { Bar bar = Foo.staticField;}", type, 0, 1);
	}

	/**
	 * Check that calling a static field which returns and stores a value counts as
	 * a reference
	 */
	@Test
	public void test_ReturnStaticFieldQualified_Dec_0_Ref_1() {
		configureParser("package other; public class Other { Bar2 bar2 = other.bar.Foo.staticField;}", type, 0, 1);
	}

	/**
	 * Check that setting the field of Foo does not count as a reference. This is
	 * relevant where field node checks may count a reference to Foo even if it does
	 * not show up in the code.
	 */
	@Test
	public void test_SetField_Dec_1_Ref_0() {
		configureParser(
				"package other.bar; public class Foo { public static int field; public void method() { field = 3;} }",
				type, 1, 0);
	}

	/**
	 * Check that retrieving and setting a static field counts as a reference
	 */
	@Test
	public void test_SetStaticField_Dec_0_Ref_1() {
		configureParser("package other.bar; public class Other { public void method() { Foo.staticField = 3;} }", type,
				0, 1);
	}

	/**
	 * Check that retrieving and setting a static field counts as a reference
	 */
	@Test
	public void test_SetStaticFieldQualified_Dec_0_Ref_1() {
		configureParser("package other; public class Other { public void method() { other.bar.Foo.staticField = 3;} }",
				type, 0, 1);
	}

	/**
	 * Check that declaring the Foo class in the bar package (correct package)
	 * counts as a declaration
	 */
	@Test
	public void testBarPackageDeclaration_Dec_1_Ref_0() {
		configureParser("package other.bar; class Foo {}", type, 1, 0);
	}

	/**
	 * Check that calling the Foo constructor in the bar package does count as a
	 * reference
	 */
	@Test
	public void testBarPackageFooConstructor_Dec_0_Ref_1() {
		configureParser("package other.bar; class Other { Bar bar = new Foo();}", type, 0, 1);
	}

	/**
	 * Check that calling the Foo constructor in a return statement in the bar
	 * package does count as a reference
	 */
	@Test
	public void testBarPackageFooConstructorReturnType_Dec_0_Ref_1() {
		configureParser("package other.bar; class Other { public Bar method() {return new Foo();}", type, 0, 1);
	}

	/**
	 * Check that calling the Foo constructor separately from the returned instance
	 * in the bar package does count as a reference
	 */
	@Test
	public void testBarPackageFooReturnType_Dec_0_Ref_1() {
		configureParser("package other.bar; class Other { public Bar method() { Bar foo = new Foo(); return foo;}",
				type, 0, 1);
	}

	/**
	 * Check that declaring an instance of Foo in the bar package does count as a
	 * reference
	 */
	@Test
	public void testBarPackageReference_Dec_0_Ref_1() {
		configureParser("package other.bar; class Other { Foo f; }", type, 0, 1);
	}

	/**
	 * Check that declaring the Foo class in the default package (so not bar), does
	 * not count as a declaration
	 */
	@Test
	public void testDefaultPackageDeclaration_Dec_0_Ref_0() {
		configureParser("class Foo {}", type, 0, 0);
	}

	/**
	 * Check that declaring an instance of Foo in the default package does not count
	 * as a reference
	 */
	@Test
	public void testDefaultPackageReference_Dec_0_Ref_0() {
		configureParser("class Other { Foo f; }", type, 0, 0);
	}

	/**
	 * TODO
	 */
	@Test
	public void test_ImportStatement_Dec_0_Ref_1() {
		configureParser("package other; import other.bar.Foo; class Other {}", type, 0, 1);
	}
	
	@Test
	public void test_ImportStatementClassAndUse_Dec_0_Ref_2() {
		configureParser("package other; import other.bar.Foo; class Other { Foo foo; }", type, 0, 2);
	}

	@Test
	public void test_ImportStatementClassAndUse_FalsePositive_Dec_0_Ref_0() {
		configureParser("package other; import other.bar.Foo; class Other { Foo foo; }", "other.Foo", 0, 0);
	}
	

	/**
	 * Check that declaring the Foo class in the other package (so not bar), does
	 * not count as a declaration
	 */
	@Test
	public void testOtherPackageDeclaration_Dec_0_Ref_0() {
		configureParser("package other; class Foo {}", type, 0, 0);
	}

	/**
	 * Check that declaring an instance of Foo in the other package does not count
	 * as a reference
	 */
	@Test
	public void testOtherPackageReference_Dec_0_Ref_0() {
		configureParser("package other; class Other { Foo f; }", type, 0, 0);
	}

}
