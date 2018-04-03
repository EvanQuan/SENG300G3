package main.file;

/**
 * Represents a .java file. Contains file name, path, and source code.
 *
 * @author Evan Quan
 * @version 1.1.0
 * @since 2 April, 2018
 *
 */
public class JavaFile extends File {

	/**
	 * File extension of a Java file
	 */
	public static final String EXTENSION = ".java";
	/**
	 * File extension of a Jar file
	 */
	public static final String JAR_EXTENSION = ".jar";

	public static final String ZIP_EXTENSION = ".zip";

	/**
	 * Complete constructor for JavaFile
	 *
	 * @param name
	 * @param path
	 * @param contents
	 */
	public JavaFile(String name, String path, String contents) {
		super(name, path, contents, EXTENSION);
	}
}
