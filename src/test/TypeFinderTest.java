package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;

import main.TypeFinder;

/**
 * @author Evan Quan
 * @version 3.0.0
 * @since 2 April 2018
 *
 */
public class TypeFinderTest {

	protected static String OUTPUT_FILE = "Output.txt";
	/**
	 * Contents of standard output
	 */
	protected static ByteArrayOutputStream outContent;
	protected static ByteArrayOutputStream errContent;

	/**
	 * Restore standard output and error to original streams
	 */
	@After
	public void restoreStream() {
		TypeFinder.setOut(System.out);
		TypeFinder.setErr(System.err);
	}

	/**
	 * outContent tracks standard output
	 */
	@Before
	public void setUpStream() {
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		TypeFinder.setOut(new PrintStream(outContent));
		TypeFinder.setErr(new PrintStream(errContent));
	}
}
