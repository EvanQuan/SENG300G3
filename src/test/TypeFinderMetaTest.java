package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import main.TypeFinder;
import main.file.FileManager;

/**
 * Meta test case. Used to test test cases.
 *
 * @author Evan Quqn
 */
public class TypeFinderMetaTest extends TypeFinderTest {

	@Test
	public void test_Directory_typeFinder_existImport() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("existImport/");
		testOutput(directory);
	}

	@Test
	public void test_Directory_typeFinder_wildcardImport() {
		String directory = _TestSuite.TYPE_FINDER_TEST_DIR.concat("wildcardImport/");
		testOutput(directory);
	}

	public void testOutput(String path) {
		String[] args = { path };
		TypeFinder.main(args);
		String expectedErr = "";
		// Check that there is no error
		String actualErr = errContent.toString();
		assertEquals(expectedErr, actualErr);
		// Check standard output matches Output.txt file
		String expectedOut;
		try {
			expectedOut = FileManager.getFileContents(path.concat(OUTPUT_FILE));
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
}
