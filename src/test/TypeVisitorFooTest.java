package test;

import org.junit.Test;

import main.ast.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Foo
 *
 * @author Evan Quan
 * @version 2.1.0
 * @since 25 March 2018
 *
 */
public class TypeVisitorFooTest extends TypeVisitorTest {

	private static final String type = "Foo";

	/**
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter counts as a reference
	 */
	@Test
	public void test_In1ParamterizedType_Dec_0_Ref_1() {
		configureParser("public class Other{ Bar<Foo> bar;}", type, 0, 1);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter twice counts as 2 references
	 */
	@Test
	public void test_In1ParameterizedTypes_Dec_0_Ref_2() {
		configureParser("public class Other{ Bar<Foo, Foo> bar;}", type, 0, 2);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter twice counts as 2 references with another third generic
	 * parameter of another class
	 */
	@Test
	public void test_In3ParameterizedTypesMixed_Dec_0_Ref_2() {
		configureParser("public class Other{ Bar<Foo, String, Foo> bar;}", type, 0, 2);
	}

	/**
	 * Check if an annotation declaration is counted as a declaration
	 */
	@Test
	public void test_AnnotationDeclaration_Dec_1_Ref_0() {
		configureParser("@interface Foo {}", type, 1, 0);
	}

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

	/**
	 * Check if declaring a meta class of Foo counts as a reference
	 */
	@Test
	public void test_ClassClass_Dec_0_Ref_1() {
		configureParser("public class Other{Class<Foo> foo;}", type, 0, 1);
	}

	/**
	 * Check if declaring a meta class of Foo counts as a reference TODO return here
	 */
	@Test
	public void test_ClassClass_Dec_0_Ref_2() {
		configureParser("public class Other{Class<Foo> foo = Foo.class;}", type, 0, 2);
	}

	/**
	 * Check if a class declaration is counted as a declaration
	 */
	@Test
	public void test_ClassDeclaration_Dec_1_Ref_0() {
		configureParser("class Foo {}", type, 1, 0);
	}

	/**
	 * Check that declaring an variable inside a method counts as a reference
	 */
	@Test
	public void test_DeclareAndInstantiateInsideMethod_Dec_0_Ref_2() {
		configureParser("public class Other{ public void bar() {Foo f = new Foo();}", type, 0, 2);
	}

	/**
	 * Check if a variable declaration and instantiating it with a constructor is
	 * counted as 2 references
	 */
	@Test
	public void test_DeclareAndInstantiateVariable_Dec_0_Ref_2() {
		configureParser("public class Other { Foo foo = new Foo();}", type, 0, 2);
	}

	/**
	 * Check that declaring an variable inside a method counts as a reference
	 */
	@Test
	public void test_DeclareInsideMethod_Dec_0_Ref_1() {
		configureParser("public class Other{ public void bar() {Foo f;}", type, 0, 1);
	}

	/**
	 * Check if a variable declaration is counted as a reference
	 */
	@Test
	public void test_DeclareVariable_Dec_0_Ref_1() {
		configureParser("public class Other{ Foo foo;}", type, 0, 1);
	}

	/**
	 * Check if an enum declaration is counted as a declaration
	 */
	@Test
	public void test_EnumDeclaration_Dec_1_Ref_0() {
		configureParser("enum Foo {}", type, 1, 0);
	}

	@Test
	public void test_ForLoopInitialization_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { for (Foo f;;){}}}", type, 0, 1);
	}

	/**
	 * Check that illegal Java syntax (cannot be compiled) results in no references
	 * or declarations
	 */
	@Test
	public void test_IllegalSyntax_Dec_0_Ref_0() {
		configureParser("This is invalid Java syntax; Foo foo; Foo foo2 = new Foo();", type, 0, 0);
	}

	/**
	 * Check that a reference of Foo instantiated inside a parameter of another
	 * constructor is counted
	 */
	@Test
	public void test_InstantiateInsideOtherConstructor_Dec_0_Ref_1() {
		configureParser("public class Other{ Other2 other = new Other2(new Foo());}", type, 0, 1);
	}

	/**
	 * Check that an instantiation of another class inside a Foo constructor counts
	 * the reference
	 */
	@Test
	public void test_InstantiateOtherInsideConstructor_Dec_0_Ref_2() {
		configureParser("public class Other{ Foo foo = new Foo(new Other2());}", type, 0, 2);
	}

	/**
	 * Check that instantiating an instance of a Parent of Foo with the constructor
	 * of Foo counts 1 reference
	 */
	@Test
	public void test_InstantiateVariableOfParent_Dec_1_Ref_1() {
		configureParser("public class Other{ FooParent foo = new Foo();}", type, 0, 1);
	}

	/**
	 * Check that instantiating an instance of a Child of Foo in a variable of type
	 * Foo counts as 1 reference
	 */
	@Test
	public void test_InstantiatingVariableOfChild_Dec_0_Ref_1() {
		configureParser("public class Other{ Foo foo = new FooChild();}", type, 0, 1);
	}

	/**
	 * Check if an interface declaration is counted as a declaration
	 */
	@Test
	public void test_InterfaceDeclaration_Dec_1_Ref_0() {
		configureParser("interface Foo {}", type, 1, 0);
	}

	/**
	 * Check if an annotation reference is counted as a reference
	 */
	@Test
	public void test_MarkerAnnotationReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Foo public void method() {}}", type, 0, 1);
	}

	/**
	 * Check if a single member annotation reference is counted as a reference
	 */
	@Test
	public void test_SingleMemberAnnotationReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Foo(3, 4, 5) public void method() {}}", type, 0, 1);
	}

	/**
	 * Check if a normal annotation referenced is counted
	 */
	@Test
	public void test_NormalAnnotationReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Foo(expected = Bar.class) public void method() {}}", type, 0, 1);
	}

	/**
	 * Check if a return type reference in a method declaration is counted as a
	 * reference
	 */
	@Test
	public void test_MethodReturn_Dec_0_Ref_1() {
		configureParser("public class Other{ public Foo methodName() {}}", type, 0, 1);
	}

	/**
	 * Check if a marker annotation that references Foo as a parameter is counted as
	 * a reference
	 */
	@Test
	public void test_NormalAnnotationParameterReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Test(expected = Foo.class) public void test() {}}", type, 0, 1);
	}

	/**
	 * Check if a class declaration of another type does not affect declaration
	 * count
	 */
	@Test
	public void test_OtherClassDeclaration_Dec_0_Ref_0() {
		configureParser("class Other {}", type, 0, 0);
	}

	/**
	 * Check that returning an instance directly from constructor counts as a
	 * reference
	 */
	@Test
	public void test_ReturnConstructor_Dec_0_Ref_2() {
		configureParser("public class Other{public Foo bar() {return new Foo();}", type, 0, 2);
	}

	/**
	 * Check if a a variable declaration and setting as another variable's value is
	 * counted as a reference
	 */
	@Test
	public void test_SetVariable_Dec_0_Ref_1() {
		configureParser("public class Other { Foo foo = anotherFoo;}", type, 0, 1);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as
	 * a generic parameter and instantiates it counts as 2 references
	 */
	@Test
	public void test_In1ParamterizedTypeAndInstantiated_Dec_0_Ref_2() {
		configureParser("public class Other{ Bar<Foo> bar = new Bar<Foo>();}", type, 0, 2);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as
	 * a generic parameter twice and instantiates it counts as 4 references
	 */
	@Test
	public void test_In2ParameterizedTypesAndInstantiated_Dec_0_Ref_4() {
		configureParser("public class Other{ Bar<Foo, Foo> bar = new Bar<Foo, Foo>();}", type, 0, 4);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as
	 * a generic parameter twice and instantiates it counts as 4 references, with
	 * another third generic parameter of another class
	 */
	@Test
	public void test_In3ParameterizedTypesAndInstantiatedMixed_dec_0_Ref_4() {
		configureParser("public class Other{ Bar<Foo, String, Foo> bar = new Bar<Foo, String, Foo>();}", type, 0, 4);
	}

	/**
	 * Check if a variable declaration within a switch statement counts as a
	 * reference
	 */
	@Test
	public void test_SwitchStatementVariableDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other{ public void method() { int x = 1; switch(x){" + "case 1:" + "Foo foo;"
				+ "break;" + "case 2:" + "break;" + "default:" + "} }}", type, 0, 1);
	}

	/**
	 * Check that calling a static field which returns and stores a value
	 * counts as a reference
	 */
	@Test
	public void test_ReturnStaticField_Dec_0_Ref_1() {
		configureParser("public class Other { Bar bar = Foo.staticField;}", type, 0, 1);
	}

	/**
	 * Check that calling a static method which returns and stores a value
	 * counts as a reference
	 */
	@Test
	public void test_ReturnStaticMethod_Dec_0_Ref_1() {
		configureParser("public class Other { Bar bar = Foo.staticMethod();}", type, 0, 1);
	}

	/**
	 * Check that retrieving and setting a static field counts as a reference
	 */
	@Test
	public void test_SetStaticField_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { Foo.staticField = 3;} }", type, 0, 1);
	}

	/**
	 * Check that calling a void static method with a parameter counts as a
	 * reference
	 */
	@Test
	public void test_SetStaticMethod_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { Foo.staticMethod(3);} }", type, 0, 1);
	}

	/**
	 * Check that declaring variable of parameterized Foo counts as a reference
	 */
	@Test
	public void test_ParameterizedTypeVariableDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other { Foo<Bar> foo; }", type, 0, 1);
	}

	/**
	 * Check that declaring and instantiating variable  of parameterized Foo counts as 2 references
	 */
	@Test
	public void test_ParameterizedTypeVariableInstantiation_Dec_0_Ref_2() {
		configureParser("public class Other { Foo<Bar> foo = new Foo<Bar>(); }", type, 0, 2);
	}

	/**
	 * Check that casting a variable to Foo counts as a reference
	 */
	@Test
	public void test_Casting_Dec_0_Ref_1() {
		configureParser("public class Other { Bar bar = (Foo) foo;}", type, 0, 1);
	}

	/**
	 * Check that a foreach loop counts as a reference
	 */
	@Test
	public void test_ForEachLoop_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { for (Foo f : foos){}}}", type, 0, 1);
	}

	/**
	 * Check that checking if a variable is instanceof Foo counts as a reference
	 */
	@Test
	public void test_Instanceof_Dec_0_Ref_3() {
		configureParser(
				"public class Other { public void method() { Foo foo = new Foo(); if (foo instanceof Foo) {} }}", type,
				0, 3);
	}

	/**
	 * Check that declaring a method that throws a Foo exception counts as a reference
	 */
	@Test
	public void test_MethodDeclarationThrowsException_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() throws Foo {}}", type, 0, 1);
	}

	/**
	 * Check that a method throwing a new Foo exception counts as a reference
	 */
	@Test
	public void test_MethodBodyThrowException_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() {throw new Foo();}}", type, 0, 1);
	}

	/**
	 * Check that having a Foo exception in a catch guard counts as a reference
	 */
	@Test
	public void test_CatchGuard_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() {try {} catch (Foo foo){} }}", type, 0, 1);
	}

	/**
	 * Check that declaring a class that directly inherits from Foo counts as a reference
	 */
	@Test
	public void test_ExtendsDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other extends Foo {}", type, 0, 1);
	}

	/**
	 * Check that declaring a variable of wildcard parameterized Foo counts as a reference
	 */
	@Test
	public void test_WildcardParamterFieldDeclaration_Dec_0_Ref_1() {
		configureParser("public class Other {Foo<?> foo;}", type, 0, 1);
	}

	/**
	 * Check that declaring and instantiating a variable of wildcard parameterized Foo counts as 2 references
	 */
	@Test
	public void test_CaseSensitive_Dec_0_Ref_2() {
		configureParser("public class FoO { FOo foo = new FoO(); Foo foO = new Foo();}", type, 0, 2);
	}

	/**
	 * Check that instantiating an instance of Foo as an argument of another method call counts as a reference
	 */
	@Test
	public void test_InstantiateInMethod_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { Bar bar = new Bar(); bar.accept(new Foo()); } }", type, 0, 1);
	}
	
	/**
	 * Check that declaring Foo with generic parameters counts as a declaration of Foo
	 */
	@Test
	public void test_ClassDeclarationGeneric_Dec_1_Ref_0() {
		configureParser("public class Foo<T> {}", type, 1, 0);
	}
	
	/**
	 * Check that declaring Foo with generic parameters that must inherit from Bar counts as a declaration of Foo
	 */
	@Test
	public void test_ClassDeclarationGenericExtends_Dec_1_Ref_0() {
		configureParser("public class Foo<T extends Bar> {}", type, 1, 0);
	}

	/**
	 * Check that declaring a class that has a generic parameter that inherits from Foo counts as a reference of Foo
	 */
	@Test
	public void test_InClassDeclarationGenericExtends_Dec_0_Ref_1() {
		configureParser("public class Bar<T extends Foo> {}", type, 0, 1);
	}
	
	/**
	 * Check that declaring a class that has a generic parameter that inherits from Foo with generic parameters counts as a reference of Foo
	 */
	@Test
	public void test_ParameterizedInClassDeclarationGenericExtends_Dec_0_Ref_1() {
		configureParser("public class Bar<T extends Foo<String>> {}", type, 0, 1);
	}
	
	/*
	 * Check that importing Foo from the Default package, which is not valid Java syntax, still counts as a reference to Foo
	 */
	@Test
	public void test_ImportFromDefaultPackage_Dec_0_Ref_1() {
		configureParser("package bar; import Foo; public class Other {}", type, 0, 1);
	}
	
	/**
	 * Check that importing Foo from the Default package, which is not valid Java syntax, now sets all simple names of Foo as default Package Foo even with a package declared
	 */
	@Test
	public void test_ImportFromDefaultPackageFieldDeclaration_Dec_0_Ref_2() {
		configureParser("package bar; import Foo; public class Other { private Foo foo; }", type, 0, 2);
	}
	
	@Test
	public void test_ImportFromDefaultPackage2MethodDeclaration_Dec_0_Ref_2() {
		configureParser("package bar; import Foo; public class Other { public void method(Foo foo) {} }", type, 0, 2);
	}
}
