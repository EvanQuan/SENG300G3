package main;

import java.io.PrintStream;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.ast.TypeVisitor;
import main.file.FileManager;
import main.file.JavaFile;
import main.file.JavaRetriever;

/**
 * Takes a pathname to indicate a directory or .jar file of interest and a
 * string to indicate a fully qualified name of a Java type. Counts the number
 * of declarations of that Java type and references of each occurrence of that
 * type with that directory (recursively) or .jar file.
 *
 * @author Evan Quan
 * @version 3.4.0
 * @since 4 April 2018
 *
 */
public class TypeFinder {

	/**
	 * Command line argument index for the directory/jar path of interest
	 */
	public static final int SOURCE_PATH = 0;
	/**
	 * The number of command line arguments the user needs to input in order for the
	 * program to properly work.
	 */
	public static final int VALID_ARGUMENT_COUNT = 1;
	/**
	 * The program will normally throw an error if the argument count is over
	 * VALID_ARGUMENT_COUNT, but a debug argument 1 over the valid argument count is
	 * allowed
	 */
	private static final int DEBUG_ARGUMENT_COUNT = VALID_ARGUMENT_COUNT + 1;
	/**
	 * Index of debug argument
	 */
	private static final int DEBUG_ARGUMENT = VALID_ARGUMENT_COUNT;
	/**
	 * Error message when the user inputs a directory or jar file that TypeFinder
	 * cannot recognize. This may be because the directory or jar file does not
	 * exist, or is not accessible.
	 */
	public static final String INVALID_PATH_ERROR_MESSAGE = "Error: Invalid path.";
	/**
	 * An IOException should never run (as opposed to a NotDirectoryException)
	 * because files that cannot be accessed or do not exist are not considered when
	 * looking for Java files in a directory anyways.
	 */
	public static final String YOU_DUN_GOOFED_UP_MESSAGE = "Error: This should never run.";
	/**
	 * Prompts the user on how to use the program properly.
	 */
	public static final String USAGE_MESSAGE = "Usage: java TypeFinder <path>";
	/**
	 * Error message when the user inputs an incorrect number of command line
	 * arguments when running the program.
	 */
	public static final String INVALID_ARGUMENT_ERROR_MESSAGE = "Error: Invalid number of arguments.\n" + USAGE_MESSAGE;

	private static String sourcePath;
	/**
	 * Debug alters how the TypeVisitor is set up
	 */
	private static boolean debug;
	/**
	 * Defaults to System.out but can be changed to something else for debuggging
	 */
	private static PrintStream out = System.out;
	private static PrintStream err = System.err;

	private static ArrayList<JavaFile> javaFiles;
	private static TypeVisitor visitor;

	/**
	 * Iterate through all Java files and find the declarations and references of
	 * all Java types
	 */
	private static TypeVisitor findDeclarationsAndReferences() {
		// Initialize visitor once, so types, declarations, and references accumulate
		// over all files
		visitor = new TypeVisitor(debug);

		// For every file iterated through, types, declarations, and references are
		// updated.
		for (JavaFile file : javaFiles) {
			ASTParser parser = getConfiguredASTParser(file);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			visitor.resetToNewFile();

			if (debug) {
				System.out.println("===START VISIT===");
				System.out.println("Path: " + file.getPath());
			}
			cu.accept(visitor);
			if (debug) {
				System.out.println("===END VISIT===" + FileManager.lineSeparator + FileManager.lineSeparator);
			}
		}

		return visitor;
	}

	/**
	 *
	 * @param file
	 *            to set as source
	 * @return ASTParser configured to parse CompilationUnits for JLS8
	 */
	private static ASTParser getConfiguredASTParser(JavaFile file) {
		@SuppressWarnings("deprecation") // JLS9 is most updated
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		// Given source is char[], these are required to resolve binding
		parser.setEnvironment(null, null, null, true);
		parser.setUnitName(file.getPath());

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		parser.setSource(file.getContents().toCharArray());
		return parser;
	}

	/**
	 * Initiates the program
	 *
	 * @param args
	 *            command line arguments args[0] path of directory/jar file of
	 *            interest
	 */
	public static void main(String[] args) {
		// Check if user input is valid and set up
		boolean validInput = processInput(args);
		if (!validInput) {
			return; // End program
		}

		// Find declaration and reference counts of java files
		findDeclarationsAndReferences();

		// Final output
		printDeclarationsAndReferences();
	}

	/**
	 * Sorts types and prints all types, and declaration and reference counts of
	 * each type. Also prints the kind of declarations and their relative usage.
	 */
	public static void printDeclarationsAndReferences() {
		ArrayList<String> types = visitor.getTypes();
		Collections.sort(types);

		for (String type : types) {
			int declarationCount = visitor.getNamedDeclarations().count(type);
			int referenceCount = visitor.getReferences().count(type);
			out.println(
					type + ". Declarations found: " + declarationCount + "; references found: " + referenceCount + ".");
		}

		int totalDeclarations = visitor.getDeclarationCount();
		int anonymousDeclarations = visitor.getAnonymousDeclarations().getElementCount();
		int localDeclarations = visitor.getLocalDeclarations().getElementCount();
		int nestedDeclarations = visitor.getNestedDeclarations().getElementCount();

		int totalReferences = visitor.getReferences().getElementCount();
		int localReferences = visitor.getLocalReferences().getElementCount();
		int nestedReferences = visitor.getNestedReferences().getElementCount();

		// Iteration 3
		out.println("Total declarations found: " + totalDeclarations);
		out.println("Anonymous declarations found: " + anonymousDeclarations);
		out.println("Local declarations found: " + localDeclarations);
		out.println("Nested declarations found: " + nestedDeclarations);
		out.println("Total references found: " + totalReferences);
		out.println("Local references found: " + localReferences);
		out.println("Nested references found: " + nestedReferences);

		if (debug) { // Ignores setOut and setErr to prevent test conflict
			System.out.println("VISIT RESULTS");
			System.out.println("Declarations:");
			System.out.println("\tNamed: " + visitor.getNamedDeclarations());
			System.out.println("\tAnonymous: " + visitor.getAnonymousDeclarations());
			System.out.println("\tLocal: " + visitor.getLocalDeclarations());
			System.out.println("\tNested: " + visitor.getNestedDeclarations());
			System.out.println("References:");
			System.out.println("\tNamed: " + visitor.getReferences());
			System.out.println("\tLocal: " + visitor.getLocalReferences());
			System.out.println("\tNested: " + visitor.getNestedReferences());
		}

	}

	/**
	 * Check that user input is valid and initialize TypeFinder based on input
	 *
	 * @param args
	 *            command line arguments
	 * @return true if user input is valid, else false
	 */
	private static boolean processInput(String[] args) {

		// Default debug is off
		debug = false;
		// Check if user has inputed a valid number arguments.
		if (args.length != VALID_ARGUMENT_COUNT) {
			// Check for debug mode
			if (args.length == DEBUG_ARGUMENT_COUNT && args[DEBUG_ARGUMENT].equals("debug")) {
				debug = true;
			} else {
				err.println(INVALID_ARGUMENT_ERROR_MESSAGE);
				return false;
			}
		}

		// Get input from command line arguments
		sourcePath = args[SOURCE_PATH];

		// Get all all Java files from directory or jar file
		try {
			javaFiles = JavaRetriever.getJavaContents(sourcePath);
		} catch (NotDirectoryException e) {
			err.println(INVALID_PATH_ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/**
	 *
	 * @param newErr
	 */
	public static void setErr(PrintStream newErr) {
		err = newErr;
	}

	/**
	 *
	 * @param newOut
	 */
	public static void setOut(PrintStream newOut) {
		out = newOut;
	}
}
