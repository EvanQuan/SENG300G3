package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;

/**
 * @author Evan Quan
 * @version 2.0.0
 * @since 25 March 2018
 *
 */
public class TypeFinderTest {

	protected static String OUTPUT_FILE = "Output.txt";
	protected static PrintStream out = System.out;
	protected static PrintStream err = System.err;
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
		System.setOut(out);
		System.setErr(err);
	}

	/**
	 * outContent tracks standard output
	 */
	@Before
	public void setUpStream() {
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}
}
