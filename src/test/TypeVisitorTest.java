package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.ast.TypeCounterI2G4;
import main.ast.TypeCounterI2G4.TargetType;
import main.ast.TypeVisitor;
import main.ast.TypeVisitorI1G11;
import main.ast.TypeVisitorI1G12;
import main.ast.TypeVisitorI1G2;
import main.ast.TypeVisitorI1G7;
import main.ast.TypeVisitorI1G8;
import main.ast.TypeVisitorI2G1;
import main.ast.TypeVisitorI2G2;
import main.ast.TypeVisitorI2G3;
import main.ast.TypeVisitorI2G7;
import main.ast.TypeVisitorI2G8;
import main.ast.TypeVisitorI2G9;
import main.file.FileManager;

/**
 *
 * @author Evan Quan
 * @version 3.2.0
 * @since 2 April 2018
 *
 */
public abstract class TypeVisitorTest {

	// Scores
	// Success : Error : Failure

	// Current visitor
	public static final int MAIN = 302; // 121:0:0
	// Iteration 1 visitors
	public static final int I1G2 = 102; // 31:0:90
	public static final int I1G7 = 107; // 53:0:68
	public static final int I1G8 = 108; // 89:0:32
	public static final int I1G11 = 111; // 85:0:36
	public static final int I1G12 = 112; // 84:0:37
	// Iteration 2
	public static final int I2G1 = 201; // 95:2:27
	public static final int I2G2 = 202; // 124:0:0
	public static final int I2G3 = 203; //
	public static final int I2G4 = 204; // 96:0:27
	public static final int I2G7 = 207; // 95:0:29
	public static final int I2G8 = 208; // 89:0:35
	public static final int I2G9 = 209; // 10:0:111
	protected static String ls = FileManager.lineSeparator;
	protected static boolean debug = true;

	/**
	 * Change this to the visitor that you want to test
	 */
	public static final int CURRENT_VISITOR_TO_TEST = MAIN;

	/**
	 * Iteration 3 tests. Only check Main visitor.
	 * 
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedAnonymousDeclarations
	 * @param expectedLocalDeclarations
	 * @param expectedNestedDeclarations
	 * @param expectedReferenceCount
	 * @param expectedLocalReferences
	 * @param expectedNestedReferences
	 */
	protected static void configureParser(String source, String type, int expectedDeclarationCount,
			int expectedAnonymousDeclarations, int expectedLocalDeclarations, int expectedNestedDeclarations,
			int expectedReferenceCount, int expectedLocalReferences, int expectedNestedReferences) {
		switch (CURRENT_VISITOR_TO_TEST) {
		case MAIN:
			configureParserMain(source, type, expectedDeclarationCount, expectedAnonymousDeclarations,
					expectedLocalDeclarations, expectedNestedDeclarations, expectedReferenceCount,
					expectedLocalReferences, expectedNestedReferences);
			break;
		default:
			configureParser(source, type, expectedDeclarationCount, expectedReferenceCount);
		}
	}

	/**
	 * Iteration 3 tests. Only checks Main visitor.
	 * 
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 * @param expectedAnonymousCount
	 * @param expectedLocalCount
	 * @param expectedNestedCount
	 */
	protected static void configureParser(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount, int expectedAnonymousCount, int expectedLocalCount, int expectedNestedCount) {
		switch (CURRENT_VISITOR_TO_TEST) {
		case MAIN:
			configureParserMain(source, type, expectedDeclarationCount, expectedAnonymousCount, expectedLocalCount,
					expectedNestedCount, expectedReferenceCount, 0, 0);
			break;
		default:
			configureParser(source, type, expectedDeclarationCount, expectedReferenceCount);
		}
	}

	/**
	 * Determines which Visitor to use. Accounts for to iteration 2 and below
	 * requirements. If iteration 3 visitor, sets anonymous, local and nested counts
	 * to 0.
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		switch (CURRENT_VISITOR_TO_TEST) {
		case MAIN:
			configureParserMain(source, type, expectedDeclarationCount, 0, 0, 0, expectedReferenceCount, 0, 0);
			break;
		case I1G2:
			configureParser_1_2(source, type, expectedDeclarationCount, expectedReferenceCount);
		case I1G7:
			configureParser_1_7(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I1G8:
			configureParser_1_8(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I1G11:
			configureParser_1_11(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I1G12:
			configureParser_1_12(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G1:
			configureParser_2_1(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G2:
			configureParser_2_2(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G3:
			configureParser_2_3(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G4:
			configureParser_2_4(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G7:
			configureParser_2_7(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G8:
			configureParser_2_8(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G9:
			configureParser_2_9(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		default:
			throw new IllegalArgumentException("Invalid visitor type");
		}
	}

	/**
	 * Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParserMain(String source, String type, int expectedDeclarationCount,
			int expectedAnonymousDeclarations, int expectedLocaDeclarations, int expectedNestedDeclarations,
			int expectedReferenceCount, int expectedLocalReferences, int expectedNestedReferences) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Hashtable<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitor visitor = new TypeVisitor();
		cu.accept(visitor);

		int declarationCount = visitor.getDeclarations().count(type);
		int referenceCount = visitor.getReferences().count(type);

		// Since anonymous class declarations don't have names, the only relevant thing
		// to check is the total quantity of anonymous class declarations
		int anonymousDeclarations = visitor.getAnonymousDeclarations().getElementCount();
		int localDeclarations = visitor.getLocalDeclarations().count(type);
		int nestedDeclarations = visitor.getNestedDeclarations().count(type);

		int localReferences = visitor.getLocalReferences().count(type);
		int nestedReferences = visitor.getNestedReferences().count(type);

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Anonymous declarations", expectedAnonymousDeclarations, anonymousDeclarations);
		assertEquals("Local declarations", expectedLocaDeclarations, localDeclarations);
		assertEquals("Nested declarations", expectedNestedDeclarations, nestedDeclarations);

		assertEquals("Reference count", expectedReferenceCount, referenceCount);
		assertEquals("Local references", expectedLocalReferences, localReferences);
		assertEquals("Nested reference", expectedNestedReferences, nestedReferences);

	}

	/**
	 * ITERATION 1 GROUP 11 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_1_11(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI1G11 visitor = new TypeVisitorI1G11(type);
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.declarationCount;
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.referenceCount;
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * GROUP 12 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_1_12(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI1G12 visitor = new TypeVisitorI1G12(type);
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.declarationCounter;
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.referenceCounter;
		} catch (Exception e) {

		}

		assertEquals("Delcaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 1 GROUP 2 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser_1_2(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI1G2 visitor = new TypeVisitorI1G2(type);
		cu.accept(visitor);

		int decl_count = 0;
		int ref_count = 0;
		try {
			decl_count = visitor.typeDecCount();
		} catch (Exception e) {

		}
		try {
			ref_count = visitor.typeRefCount();
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, decl_count);
		assertEquals("Reference count", expectedReferenceCount, ref_count);

	}

	/**
	 * Iteration 1 GROUP 7 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_1_7(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI1G7 visitor = new TypeVisitorI1G7(cu, type);
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.getDeclarationCount();
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.getReferenceCount();
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);
	}

	/**
	 * ITERATION 1 GROUP 8 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser_1_8(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI1G8 visitor = new TypeVisitorI1G8();
		cu.accept(visitor);

		int decl_count = 0;
		int ref_count = 0;
		try {
			decl_count = visitor.getDecCount().get(type);
		} catch (Exception e) {

		}
		try {
			ref_count = visitor.getRefCount().get(type);
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, decl_count);
		assertEquals("Reference count", expectedReferenceCount, ref_count);

	}

	/**
	 * Iteration 2 Group 2
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_1(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G1 visitor = new TypeVisitorI2G1();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.countMap.get(type).getDeclarationCount();
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.countMap.get(type).getReferenceCount();
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * Iteration 2 Group 2
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_2(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G2 visitor = new TypeVisitorI2G2();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.getDeclarations().count(type);
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.getReferences().count(type);
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * Iteration 2 Group 3
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_3(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G3 visitor = new TypeVisitorI2G3();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;

		ArrayList<String> types = visitor.getFoundTypes();

		for (int i = 0; i < types.size(); i++) {
			if (types.get(i).equals(type)) {
				declarationCount += visitor.getDeclarationsArray().get(i);
				referenceCount += visitor.getReferencesArray().get(i);
			}
		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * Iteration 2 Group 4
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_4(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeCounterI2G4 counter = new TypeCounterI2G4();

		int declarationCount = 0;
		int referenceCount = 0;

		ArrayList<TargetType> types = counter.count(cu);

		for (TargetType t : types) {
			if (t.getType().equals(type)) {
				declarationCount = t.getDec();
				referenceCount = t.getRef();
				break;
			}
		}

		assertEquals("Declaraction count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 2 GROUP 7 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_7(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		// String[] srcPath = { _TestSuite.SOURCE_DIR };
		// String[] classPath = { _TestSuite.BIN_DIR };
		// parser.setEnvironment(classPath, srcPath, null, true);
		parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G7 visitor = new TypeVisitorI2G7();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.getDecCount().get(type);
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.getRefCount().get(type);
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 2 GROUP 8 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_8(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		// ASTParser parser = ASTParser.newParser(AST.JLS9);
		// parser.setSource(source.toCharArray());
		// parser.setKind(ASTParser.K_COMPILATION_UNIT);
		// Hashtable<String, String> options = JavaCore.getOptions();
		// options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
		// parser.setCompilerOptions(options);
		// parser.setResolveBindings(true);
		// parser.setBindingsRecovery(true);
		// //
		// String tempClassPath = new File("").getAbsolutePath();
		// String[] tempClassPathArray = { tempClassPath };
		// //
		// parser.setEnvironment(tempClassPathArray, null, null, false);
		// parser.setUnitName("temp.java");

		// Adding bindings
		// CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G8 visitor = new TypeVisitorI2G8();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.logDeclarations.get(type);
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.logReferences.get(type);
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 2 GROUP 9 Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser_2_9(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { _TestSuite.SOURCE_DIR };
		String[] classPath = { _TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitorI2G9 visitor = new TypeVisitorI2G9();
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.typeMap.get(type).get(1);
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.typeMap.get(type).get(0);
		} catch (Exception e) {

		}

		assertEquals("Declaration count", expectedDeclarationCount, declarationCount);
		assertEquals("Reference count", expectedReferenceCount, referenceCount);

	}

}
