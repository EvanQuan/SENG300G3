package main.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Reads and retrieves contents of files and directories
 *
 * @author Evan Quan
 * @version 1.1.0
 * @since 25 March 2018
 *
 */
public class FileManager {

	/**
	 * Line separator changes depending on operating system. JUnitTests dealing with
	 * file contents should consider this.
	 */
	public static final String lineSeparator = System.getProperty("line.separator");

	/**
	 * Reads the contents of a file with a given path and returns the contents of
	 * that file as a String.
	 *
	 * @param path
	 *            of file to read
	 * @return contents of file
	 * @throws IOException
	 *             if file is not able to be read
	 */
	public static String getFileContents(String path) throws IOException {
		FileReader fileReader = new FileReader(path);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();

		// Keep reading line by line (and ending with a line separator)
		// Until no more lines can be read
		// Each line is appended to the output stringBuffer
		try {
			String line = bufferedReader.readLine();
			while (line != null) {
				stringBuffer.append(line);
				stringBuffer.append(lineSeparator);
				line = bufferedReader.readLine();
			}
			return stringBuffer.toString();
		} finally {
			bufferedReader.close();
		}
	}
	
	
	
	/**
	 * Reads the contents of a given entry in a jar file and converts its contents to a String
	 * @param jar - the containing Jar file for the given jar entry
	 * @param entry - the jar entry to be converted to String
	 * @return contents to the jar entry as a String
	 * @throws IOException if the file cannot be accessed or properly read
	 */
	public static String getFileContents(JarFile jar, JarEntry entry) throws IOException{
		
		InputStream inStream = jar.getInputStream(entry);
		Reader reader = new InputStreamReader(inStream);
		BufferedReader bufReader = new BufferedReader(reader);
		StringBuffer stringBuffer = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		
		try {
			String line = bufReader.readLine();
			while (line != null) {
				stringBuffer.append(line);
				stringBuffer.append(lineSeparator);
				line = bufReader.readLine();
			}
			return stringBuffer.toString();
			
		} finally {
			
			bufReader.close();	
		}
	}
	
	
	/**
	 *
	 * @param path
	 * @return true is path is a directory that exists, else false
	 */
	public static boolean isValidDirectory(String path) {
		return new File(path).isDirectory();
	}

	/**
	 *
	 * @param path
	 * @return true if path is a jar file that exists, else false
	 */
	public static boolean isValidJarFile(String path) {
		
		return new File(path).isFile() && (path.endsWith(JavaFile.JAR_EXTENSION)|| path.endsWith(".zip"));
	}

	/**
	 *
	 * @param path
	 * @return true if path is a jar file that exists, else false
	 */
	public static boolean isValidJavaFile(String path) {
		return new File(path).isFile() && path.endsWith(JavaFile.EXTENSION);
	}
}
