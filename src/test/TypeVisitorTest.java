package test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.ast.TypeVisitor;
import main.file.FileManager;

/**
 * 
 * @author Evan Quan
 * @version 2.0.0
 * @since 25 March 2018
 *
 */
public abstract class TypeVisitorTest {

	// Current visitor
	public static final int MAIN = 0;
	// Iteration 1 visitors
	public static final int I1G7 = 107;
	public static final int I1G8 = 108;
	public static final int I1G11 = 111;
	public static final int I1G12 = 112;
	// Iteration 2
	public static final int I2G7 = 207;
	public static final int I2G9 = 209;
	protected static String ls = FileManager.lineSeparator;
	protected static boolean debug = true;
	
	
	/**
	 * Change this to the visitor that you want to test
	 */
	public static final int CURRENT_VISITOR_TO_TEST = MAIN;
	
	/**
	 * Determines which Visitor to use
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	protected static void configureParser(String source, String type, int expectedDeclarationCount, int expectedReferenceCount) {
		switch (CURRENT_VISITOR_TO_TEST) {
		case MAIN:
			configureParserMain(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
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
		case I2G7:
			configureParser_2_7(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		case I2G9:
			configureParser_2_9(source, type, expectedDeclarationCount, expectedReferenceCount);
			break;
		default:
			configureParserMain(source, type, expectedDeclarationCount, expectedReferenceCount);
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

		TypeVisitor visitor = new TypeVisitor(debug);
		cu.accept(visitor);

		int declarationCount = 0;
		int referenceCount = 0;
		try {
			declarationCount = visitor.getDeclarations().get(type);
		} catch (Exception e) {

		}
		try {
			referenceCount = visitor.getReferences().get(type);
		} catch (Exception e) {

		}

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 2 GROUP 7
	 * Configures ASTParser and visitor for source file
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

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);

	}

	/**
	 * ITERATION 2 GROUP 9
	 * Configures ASTParser and visitor for source file
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

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);

	}
	
	/**
	 * ITERATION 1 GROUP 8
	 * Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser_1_8(String source, String type, int expectedDeclarationCount, int expectedReferenceCount) {
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

		assertEquals(expectedDeclarationCount, decl_count);
		assertEquals(expectedReferenceCount, ref_count);

	}
	/**
	 * ITERATION 1 GROUP 11
	 * Configures ASTParser and visitor for source file
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

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);

	}

	/**
	 * GROUP 12
	 * Configures ASTParser and visitor for source file
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

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);

	}

	/**
	 * Iteration 1 GROUP 7
	 * Configures ASTParser and visitor for source file
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

		assertEquals(expectedDeclarationCount, declarationCount);
		assertEquals(expectedReferenceCount, referenceCount);
	}
}
