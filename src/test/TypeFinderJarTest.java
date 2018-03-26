package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import main.TypeFinder;
import main.file.FileManager;
import main.file.JavaFile;

public class TypeFinderJarTest extends TypeFinderTest {

	@Test
	public void test_Jar_typeFinder_SENG300W18Iter1() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR;
		String jarName = "SENG300W18Iter1.jar";
		testOutput(directory, jarName);
	}

	/**
	 * Tests that TypeFinder finds and output the correct declaration and reference counts of the given input path.
	 * Assumes that path exists and is valid.
	 * Output is checked with Output.txt file in the input directory.
	 * @param path of directory or .jar
	 * @throws IOException if path is invalid
	 */
	public void testOutput(String path, String jarName) {
		String[] args = {path.concat(jarName)};
		TypeFinder.main(args);
		String expectedErr = "";
		// Check that there is no error
		String actualErr = errContent.toString();
		assertEquals(expectedErr, actualErr);
		// Check standard output matches Output.txt file
		String expectedOut;
		try {
			String outputFileName = jarName.substring(0, jarName.length() - JavaFile.JAR_EXTENSION.length()).concat(OUTPUT_FILE);

//			assertEquals("", outputFileName);
			expectedOut = FileManager.getFileContents(path.concat(outputFileName));
			String actualOut = outContent.toString();
			assertEquals(expectedOut, actualOut);
		} catch (IOException e) {
			fail("Invalid path");
		}
	}
}
