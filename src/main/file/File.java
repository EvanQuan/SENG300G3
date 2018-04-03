package main.file;

/**
 * Represents a file. Contains file name, path, and contents.
 *
 * @author Evan Quan
 * @version 1.1.0
 * @since 2 April, 2018
 *
 */
public class File implements Comparable<JavaFile> {

	private String name;
	private String path;
	private String contents;
	private String extension;

	/**
	 * Complete constructor for File
	 *
	 * @param name
	 * @param path
	 * @param contents
	 */
	public File(String name, String path, String contents, String extension) {
		setExtension(extension);
		setName(name);
		setPath(path);
		setContents(contents);
	}

	/**
	 * File objects are sorted alphabetically by file path. If file names are the
	 * same, sort by file name.
	 */
	@Override
	public int compareTo(JavaFile o) {
		int comparePath = this.path.compareTo(o.getPath());
		if (comparePath == 0) {
			return this.name.compareTo(o.getName());
		} else {
			return comparePath;
		}
	}

	/**
	 *
	 * @return contents of file
	 */
	public String getContents() {
		return contents;
	}

	/**
	 *
	 * @return extension of file
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 *
	 * @return name with file extension
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return path of file
	 */
	public String getPath() {
		return path;
	}

	/**
	 *
	 * @return name without file extension
	 */
	public String getSimpleName() {
		return name.substring(0, name.length() - extension.length());
	}

	/**
	 *
	 * @param contents
	 *            of file
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 *
	 * @param extension
	 *            of file
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 *
	 * @param name
	 *            of file
	 */
	public void setName(String name) {
		if (!name.endsWith(extension)) {
			// Add extension if it doesn't have it
			name = name.concat(extension);
		}
		this.name = name;
	}

	/**
	 *
	 * @param path
	 *            to file
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * String representation of file
	 */
	@Override
	public String toString() {
		return "[name: " + name + " | path: " + path + "]\n" + contents;
	}

}
