package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

import org.junit.Test;

import main.file.FileManager;
import main.file.JavaFile;
import main.file.JavaRetriever;

/**
 * JUnit 4 Test for {@link JavaRetriever} class
 *
 * @author Evan Quan
 * @version 2.0.0
 * @since 25 March 2018
 *
 */
public class JavaRetrieverTest {

	/**
	 * The expected string representation of TestClass
	 */
	private static final String TestClassString = "package test;" + FileManager.lineSeparator + FileManager.lineSeparator
			+ "public class TestClass {" + FileManager.lineSeparator + FileManager.lineSeparator + "}"
			+ FileManager.lineSeparator;

	/**
	 * Check that a NotDirectoryException is thrown if an invalid directory is
	 * searched
	 *
	 * @throws NotDirectoryException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	@Test(expected = NotDirectoryException.class)
	public void testGetAllJavaFilesToStringForInvalidDirectory() throws NotDirectoryException, IOException {
		String invalidDirectory = "";
		ArrayList<JavaFile> results = JavaRetriever.getJavaContents(invalidDirectory);
	}

	/**
	 * Check that all the contents of all Java files (and only Java files) can be
	 * retrieved as Strings from the javaFileReaderTestPackage directory
	 *
	 * @throws NotDirectoryException
	 * @throws IOException
	 */
	@Test
	public void testGetAllJavaFilesToStringForTestPackage() throws NotDirectoryException, IOException {
		ArrayList<JavaFile> results = JavaRetriever.getJavaContents(_TestSuite.JAVA_RETRIEVERMISC_TEST_DIR);
		String appleSource = "package test.javaRetrieverMisc;" + FileManager.lineSeparator
				+ FileManager.lineSeparator + "public class Apple {" + FileManager.lineSeparator + FileManager.lineSeparator
				+ "}" + FileManager.lineSeparator;
		String bananaSource = "package test.javaRetrieverMisc;" + FileManager.lineSeparator
				+ FileManager.lineSeparator + "public class Banana {" + FileManager.lineSeparator + FileManager.lineSeparator
				+ "}" + FileManager.lineSeparator;
		String zebraSource = "package test.javaRetrieverMisc;" + FileManager.lineSeparator
				+ FileManager.lineSeparator + "public class Zebra {" + FileManager.lineSeparator + FileManager.lineSeparator
				+ "}" + FileManager.lineSeparator;
		assertEquals(appleSource, results.get(0).getContents());
		assertEquals(bananaSource, results.get(1).getContents());
		assertEquals(zebraSource, results.get(2).getContents());
	}

}
