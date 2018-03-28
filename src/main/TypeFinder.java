package main;

import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.ast.TypeVisitor;
import main.file.ClassFile;
import main.file.File;
import main.file.JavaFile;
import main.file.JavaRetriever;

/**
 * Takes a pathname to indicate a directory or .jar file of interest and a
 * string to indicate a fully qualified name of a Java type. Counts the number
 * of declarations of that Java type and references of each occurrence of that
 * type with that directory (recursively) or .jar file.
 *
 * @author Evan Quan
 * @version 1.1.0
 * @since March 18, 2018
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

	private static ArrayList<JavaFile> javaFiles;
	private static TypeVisitor visitor;

	/**
	 * Iterate through all Java files and find the declarations and references of
	 * all Java types
	 */
	private static TypeVisitor findDeclarationsAndReferences() {
		// Initialize visitor once, so types, declarations, and references accumulate
		// over all files
		visitor = new TypeVisitor();

		// For every file iterated through, types, declarations, and references are
		// updated.
		for (JavaFile file : javaFiles) {
			ASTParser parser = getConfiguredASTParser(file);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			visitor.resetToNewFile();
			cu.accept(visitor);
		}

		return visitor;
	}

	/**
	 * NOTE: Unused currently. May be used for iteration 3.
	 * 
	 * @param file
	 *            to set as source
	 * @return ASTParser configured to parse CompilationUnits for JLS8
	 */
	private static ASTParser getConfiguredASTParser(ClassFile file) {
		// TODO
		// 2. How to get the ASTParser to accept a .class file
		// - Turn the contents of a .class file into a IClassFile/ICompilationUnit
		// - Something that may involve IFile, File class?

		// .class -> File
		// File classFile = new File("class path??");
		//
		// File -> IFile
		// https://stackoverflow.com/questions/960746/how-to-convert-from-file-to-ifile-in-java-for-files-outside-the-project
		// http://exploreeclipse.blogspot.ca/2014/08/converting-java-io-file-to-eclipse.html
		// https://stackoverflow.com/questions/17421590/iterate-over-folders-in-jar-file-directly
		// <--- THIS
		//
		// IFile -> IClassFile
		// IClassFile ifile = JavaCore.createClassFileFrom(IFile file)
		// https://help.eclipse.org/luna/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2FJavaCore.html
		//
		// Figure out what to set as source.
		// IClassFile?
		// What about other settings? Are they the same?
		// setKind still CompilationUnit?

		return null;
	}

	/**
	 * This method is overridden by JavaFile and ClassFile implementations
	 * 
	 * @param file
	 *            to set as source
	 * @return ASTParser configured to parse CompilationUnits for JLS8
	 */
	private static ASTParser getConfiguredASTParser(File file) {
		throw new IllegalArgumentException();
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
		parser.setUnitName("SENG300GrpIt1");

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
	 * Sorts types and prints all types, and declaration and reference counts of
	 * each type.
	 */
	public static void printDeclarationsAndReferences() {
		ArrayList<String> types = visitor.getTypes();
		Collections.sort(types);

		for (String type : types) {
			int declarationCount = visitor.getDeclarations().count(type);
			int referenceCount = visitor.getReferences().count(type);
			System.out.println(
					type + ". Declarations found: " + declarationCount + "; references found: " + referenceCount + ".");
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

		// Check if user has inputed a valid number arguments.
		if (args.length != VALID_ARGUMENT_COUNT) {
			System.err.println(INVALID_ARGUMENT_ERROR_MESSAGE);
			return false;
		}

		// Get input from command line arguments
		sourcePath = args[SOURCE_PATH];

		// Get all all Java files from directory or jar file

		try {

			javaFiles = JavaRetriever.getJavaContents(sourcePath);

		} catch (NotDirectoryException e) {
			System.err.println(INVALID_PATH_ERROR_MESSAGE);
		}

		return true;
	}

	/**
	 * Initiates the program
	 *
	 * @param args
	 *            command line arguments args[0] path of directory/jar file of
	 *            interest args[1] fully qualified name of Java type to search for
	 *            declarations and references
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
}
