package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.TypeFinder;
import main.file.FileManager;

/**
 * JUnit 4 Tests for {@link TypeFinder} class
 *
 * @author Evan Quan
 * @version 3.0.1
 * @since 5 April 2018
 *
 */
public class TypeFinderDirectoryTest extends TypeFinderTest {

	/**
	 * Check that inputting no command line arguments returns a prompt to the user
	 * explaining how to use the program
	 */
	@Test
	public void test_ArgumentCount_0_InvalidInput() {
		String[] args = {};
		TypeFinder.main(args);
		String expected = TypeFinder.INVALID_ARGUMENT_ERROR_MESSAGE + FileManager.lineSeparator;
		String results = errContent.toString();
		assertEquals(expected, results);
	}

	/**
	 * Check that inputting 2 command line arguments returns a prompt to the user
	 * explaining how to use the program
	 */
	@Test
	public void test_ArgumentCount_2_InvalidPath() {
		String[] args = { "", "" };
		TypeFinder.main(args);
		String expected = TypeFinder.INVALID_PATH_ERROR_MESSAGE + FileManager.lineSeparator;
		String results = errContent.toString();
		assertEquals(expected, results);
	}

	@Test
	public void test_ArgumentCount2() {
		String[] directory = new String[2];
		directory[0] = _TestSuite.TYPE_FINDER_TEST_DIR.concat("SENG300W18Iter1/");
		directory[1] = _TestSuite.TYPE_FINDER_TEST_DIR.concat("JarsInDirectory/");
		testOutputMultiArgs(directory, false);
	}

	/**
	 * Check that whether a file that is being imported exists does NOT determine
	 * whether the local reference refers to the imported class or a class of the
	 * same directory.
	 */
	@Test
	public void test_Directory_typeFinder_existImport() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("existImport/");
		testOutput(directory);
	}

	/**
	 * Check that .jars within directories can be checked recursively
	 */
	@Test
	public void test_Directory_typeFinder_JarsInDirectory() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("JarsInDirectory/");
		testOutput(directory);
	}

	/**
	 * Check that the correct number of declarations and references can be found
	 * from SENG300W18Iter1 files
	 */
	@Test
	public void test_Directory_typeFinder_SENG300W18Iter1() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("SENG300W18Iter1/");
		testOutput(directory);
	}

	/**
	 * Check that whether a file that is being imported via wildcard (*) exists
	 * determines whether the local reference refers to the imported class or a
	 * class of the same directory
	 */
	@Test
	public void test_Directory_typeFinder_wildcardImport() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("wildcardImport/");
		testOutput(directory);
	}

	/**
	 * Check that inputting an invalid directory prompts the user with an invalid
	 * path error message to screen
	 */
	@Test
	public void testInvalidDirectory() {
		String invalidDirectory = "";
		String[] args = { invalidDirectory, "debug" };
		TypeFinder.main(args);
		String expected = TypeFinder.INVALID_PATH_ERROR_MESSAGE + FileManager.lineSeparator;
		String results = errContent.toString();
		assertEquals(expected, results);
	}

	/**
	 * Check that inputting an invalid jar prompts the user with an invalid path
	 * error message to screen
	 */
	@Test
	public void testInvalidJar() {
		String invalidJar = _TestSuite.TYPE_FINDER_TEST_DIR.concat("jarThatDoesNotExist.jar");
		String[] args = { invalidJar };
		TypeFinder.main(args);
		String expected = TypeFinder.INVALID_PATH_ERROR_MESSAGE + FileManager.lineSeparator;
		String results = errContent.toString();
		assertEquals(expected, results);
	}

	/**
	 * Tests that TypeFinder finds and output the correct declaration and reference
	 * counts of the given input path. Assumes that path exists and is valid. Output
	 * is checked with Output.txt file in the input directory. Debug default off.
	 *
	 * @param path
	 */
	public void testOutput(String path) {
		testOutput(path, false);
	}

	/**
	 * Tests that TypeFinder finds and output the correct declaration and reference
	 * counts of the given input path. Assumes that path exists and is valid. Output
	 * is checked with Output.txt file in the input directory.
	 *
	 * @param path
	 *            of directory or .jar
	 * @throws IOException
	 *             if path is invalid
	 */
	public void testOutput(String path, boolean debug) {
		String[] args;
		if (debug) {
			args = new String[] { "debug", path };
		} else {
			args = new String[] { path };
		}
		TypeFinder.main(args);
		String expectedErr = "";
		// Check that there is no error
		String actualErr = errContent.toString();
		assertEquals(expectedErr, actualErr);
		// Check standard output matches Output.txt file
		String expectedOut;
		try {
			String header = FileManager.lineSeparator + "Directory: " + args[0] + FileManager.lineSeparator;
			expectedOut = header + FileManager.getFileContents(path.concat(OUTPUT_FILE));
			String actualOut = outContent.toString();
			assertEquals(expectedOut, actualOut);
		} catch (FileNotFoundException e) {
			fail("Output.txt cannot be found at " + path);
		} catch (IOException e) {
			restoreStream();
			e.printStackTrace();
			fail("You dun goofed.");
		}
	}

	/**
	 *
	 * Tests that TypeFinder finds and outputs correct declaration and reference
	 * counts for multiple paths. Assumes that path exists and is valid. Output is
	 * checked with the Output.txt files in the input directories.
	 *
	 * @param paths
	 *            of directories or jars
	 *
	 * @throws IOException
	 *             if path is invalid
	 *
	 */
	public void testOutputMultiArgs(String[] paths, boolean debug) {
		TypeFinder.main(paths);
		String expectedErr = "";
		String actualErr = errContent.toString();
		assertEquals(expectedErr, actualErr);

		String expectedOut = "";
		String actualOut = "";
		for (String path : paths) {
			try {
				String header = FileManager.lineSeparator + "Directory: " + path + FileManager.lineSeparator;
				// concatenates the Output.txt file to the expectedOut
				expectedOut = expectedOut.concat(header + FileManager.getFileContents(path.concat(OUTPUT_FILE)));
			} catch (FileNotFoundException e) {
				fail("Output.txt cannot be found at " + path);
			} catch (IOException e) {
				restoreStream();
				e.printStackTrace();
				fail("You dun goofed.");
			}
		}
		actualOut = outContent.toString();
		assertEquals(expectedOut, actualOut);

	}

}
