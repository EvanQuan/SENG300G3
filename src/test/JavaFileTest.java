package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.file.JavaFile;

/**
 * JUnit 4 tests for JavaFile class.
 *
 * @author Evan Quan
 * @version 1.0.0
 * @since 2 April 2018
 *
 */
public class JavaFileTest {

	private static JavaFile file;
	private static String simpleNameInitial = "InitialName";
	private static String nameInitial = "InitialName.java";
	private static String pathInitial = "/home/InitialName.java";
	private static String sourceInitial = "public class InitialName {}";
	private static String simpleNameNew = "NewName";
	private static String nameNew = "NewName.java";
	private static String pathNew = "/home/NewName.java";
	private static String sourceNew = "public class NewName {}";

	@Test
	public void getExtension_JAVA_EXTENTION() {
		assertEquals(JavaFile.EXTENSION, file.getExtension());
	}

	@Test
	public void getName_nameInitial_nameInitial() {
		assertEquals(nameInitial, file.getName());
	}

	@Test
	public void getPath_pathInitial_pathInitial() {
		assertEquals(pathInitial, file.getPath());
	}

	@Test
	public void getSimpleName_simpleNameInitial_simpleNameInitial() {
		assertEquals(simpleNameInitial, file.getSimpleName());
	}

	@Test
	public void getSource_pathInitial_pathInitial() {
		assertEquals(sourceInitial, file.getContents());
	}

	@Test
	public void setName_newName_newName() {
		file.setName(nameNew);
		assertEquals(nameNew, file.getName());
	}

	@Test
	public void setName_newSimpleName_newName() {
		file.setName(simpleNameNew);
		assertEquals(nameNew, file.getName());
	}

	@Test
	public void setPath_pathNew_pathNew() {
		file.setPath(pathNew);
		assertEquals(pathNew, file.getPath());
	}

	@Test
	public void setSource_sourceNew_sourceNew() {
		file.setContents(sourceNew);
		assertEquals(sourceNew, file.getContents());
	}

	@Before
	public void setUp() throws Exception {
		file = new JavaFile(nameInitial, pathInitial, sourceInitial);
	}

	@Test
	public void test_toString() {
		assertEquals("[name: " + nameInitial + " | path: " + pathInitial + "]\n" + sourceInitial, file.toString());
	}

}
