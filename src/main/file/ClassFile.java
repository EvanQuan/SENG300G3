package main.file;

/**
 * Represents a .class file. Contains file name, path, and source binary.
 *
 * @author Evan Quan
 * @version 1.0.0
 * @since March 18, 2018
 *
 */
public class ClassFile extends File {

	/**
	 * File extension of a Class file
	 */
	public static final String EXTENSION = ".class";

	/**
	 * Clone constructor for ClassFile
	 *
	 * @param file
	 *            to clone
	 */
	public ClassFile(ClassFile file) {
		super(file);
	}

	/**
	 * Complete constructor for ClassFile
	 *
	 * @param name
	 * @param path
	 * @param contents
	 */
	public ClassFile(String name, String path, String contents) {
		super(name, path, contents, EXTENSION);
	}
}
