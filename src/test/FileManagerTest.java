package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.file.FileManager;

/**
 * JUnit 4 Test for {@link FileManager} class
 *
 * @author Evan Quan
 * @version 2.0.0
 * @since 25 March 2018
 *
 */
public class FileManagerTest {

	/**
	 * Check that trying to read from an invalid file throws a FileNotFoundException
	 *
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	@Test(expected = FileNotFoundException.class)
	public void getFileContents_invalidFilePath_IOException() throws IOException {
		String invalidFilePath = "";
		String result = FileManager.getFileContents(invalidFilePath);
	}
	
	/**
	 * Check that trying to read from a valid file retrieves the file contents correctly
	 * @throws IOException
	 */
	@Test
	public void getFileContents_validFilePath_Success() throws IOException {
		String validFilePath = _TestSuite.JAVA_RETRIEVER_TEST_DIR.concat("Test.java");
		String expectedResult = "package test.javaRetriever;" + FileManager.lineSeparator + FileManager.lineSeparator + "public class Test {" + FileManager.lineSeparator + FileManager.lineSeparator + "}" + FileManager.lineSeparator;
		String actualResult = FileManager.getFileContents(validFilePath);
		assertEquals(expectedResult, actualResult);
	}
}
