package test;

import java.io.File;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

/**
 * All Test suites go here.
 */
//@Suite.SuiteClasses({ FileManagerTest.class, JavaFileTest.class, JavaRetrieverTest.class, PassTest.class, TypeFinderDirectoryTest.class, TypeFinderJarTest.class, TypeVisitorBuiltInTest.class, TypeVisitorInnerFooTest.class, TypeVisitorFooTest.class,
//		TypeVisitorPrimitiveTest.class, TypeVisitorFooArrayTest.class, TypeVisitorPackageFooTest.class })

// Just visitor
@Suite.SuiteClasses({ TypeVisitorBuiltInTest.class, TypeVisitorInnerFooTest.class, TypeVisitorFooTest.class,
		TypeVisitorPrimitiveTest.class, TypeVisitorFooArrayTest.class, TypeVisitorPackageFooTest.class })
/**
 * Runs all test classes
 *
 * @author Evan Quan
 * @version 2.3.0
 * @since 26 March 2018
 *
 */
public class _TestSuite {
	/**
	 * Base directory is always root of the entire eclipse project (which is the PARENT
	 * of the src folder).
	 */
	public static final String BASEDIR = new File("").getAbsolutePath().concat("/");
	public static final String SOURCE_DIR = BASEDIR.concat("src/");
	public static final String TEST_DIR = SOURCE_DIR.concat("test/");
	public static final String BIN_DIR = BASEDIR.concat("bin/");
	/**
	 * test.javaRetriever package directory
	 */
	public static final String JAVA_RETRIEVER_TEST_DIR = TEST_DIR.concat("javaRetriever/");
	public static final String JAVA_RETRIEVERMISC_TEST_DIR = TEST_DIR.concat("javaRetrieverMisc/");
	/**
	 * test.typeFinder package directory
	 */
	public static final String TYPE_FINDER_TEST_DIR = TEST_DIR.concat("typeFinder/");
}