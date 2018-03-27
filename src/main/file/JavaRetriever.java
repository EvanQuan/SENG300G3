package main.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;



/**
 * Retrieves Java file contents from a path. Path can be a directory, .jar, or .zip file.
 *
 * @author Evan Quan
 * @author Marcello Di Benedetto
 * @version 2.0.0
 * @since March 22, 2018
 *
 */
public class JavaRetriever {

	private static final int OTHER = 0;
	private static final int DIRECTORY = 1;
	private static final int JAR = 2;
	private static final int JAVA = 3;
	
	/**
	 * Cannot instantiate... for now.
	 * NOTE: Uninstantiable class with static methods or Singleton better?
	 * If JavaRetriever had fields, then Singleton...?
	 * 
	 *
	 */
	private JavaRetriever() {
	}

	/**
	 * Determine the type of path
	 * @param path
	 * @return the type of path
	 */
	private static int checkPathType(String path) {
		if (FileManager.isValidDirectory(path)) {
			return DIRECTORY;
		} else if (FileManager.isValidJarFile(path)) {
			return JAR;
		} else if (FileManager.isValidJavaFile(path)) {
			return JAVA;
		}	
		else {
			return OTHER;
		}
	}
	/**
	 * Get Java contents from a path of a directory, .jar, or .zip file. works for directories containing different filetypes
	 * like folders, zip files and .java files. if the input is a path directly to a jar file, the method handles this special case.
	 *
	 * @param path
	 *            where Java contents are located. Can be a directory, .jar, or .zip file.
	 * @return 
	 */
	public static ArrayList<JavaFile> getJavaContents(String path) throws NotDirectoryException{
		ArrayList<JavaFile> allJavaContents = new ArrayList<JavaFile>();
		File directory = new File(path);
		File[] dirContents= directory.listFiles();
		
		if(checkPathType(path) == JAR) {
			
			allJavaContents = getJavaContentsFromJar(path);  //path is directly to a jar or zip file
			
		}else if(checkPathType(path) == DIRECTORY){
			
			try {
			
				for(java.io.File file: dirContents) {	//goes through all files in directory, handles the searching accordingly
					
					String currPath = file.getAbsolutePath();
					int pathType = checkPathType(currPath);
					
					if(pathType == DIRECTORY) {
							
						ArrayList<JavaFile> javaDirContents= getJavaContentsFromDirectory(currPath);
						allJavaContents.addAll(javaDirContents);
					
					}else if(pathType == JAR) {
						
						ArrayList<JavaFile> jarContents = getJavaContentsFromJar(currPath);
						allJavaContents.addAll(jarContents);
					
					}else if(pathType == JAVA) {
						
						String fileContents = FileManager.getFileContents(currPath);
						String fileName = file.getName();
						JavaFile javaFile = new JavaFile(fileName, currPath, fileContents);
						allJavaContents.add(javaFile);
					}	
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}else {
			throw new NotDirectoryException(path);
		}
		return allJavaContents;
	}
		

	
	
	
	
	/**
	 * Finds all Java files in a given .jar file and returns its contents as
	 * {@link JarFile}s
	 *
	 * @param path
	 *            of .jar file of interest
	 * @return contents of all .java files within the given jar
	 */
	private static ArrayList<JavaFile> getJavaContentsFromJar(String path) {
		
		ArrayList<JavaFile> filesInJar = new ArrayList<JavaFile>();
		JarInputStream jarStream;
		JarFile jar;
		JarEntry currFile;
		
		try {
			
			jarStream = new JarInputStream(new FileInputStream(path));
			jar = new JarFile(path);
			
			while((currFile = jarStream.getNextJarEntry()) != null) {
				
				if(currFile.getName().endsWith(".java")){
					
					String fileContents = FileManager.getFileContents(jar, currFile);
					String fileName = currFile.getName();
					String filePath = path;
					JavaFile javaFile = new JavaFile(fileName, filePath, fileContents);
					filesInJar.add(javaFile);
				}
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		return filesInJar;
	}
	

	/**
	 * Finds all Java files in a given directory and sub-directories and returns
	 * their contents as {@link JavaFile}s
	 *
	 * @param path
	 *            of directory of interest
	 * @return contents of all Java files as Strings, null if path is not valid
	 * @throws NotDirectoryException
	 *             if directory cannot be found
	 */
	private static ArrayList<JavaFile> getJavaContentsFromDirectory(String path) {
		ArrayList<JavaFile> javaFiles = new ArrayList<JavaFile>();
		try {
			getAllJavaFilesFromDirectoryRecursiveHelper(path, javaFiles);
			// Sort Java files alphabetically by name and then path (for predictability)
			// Cast to IJavaFile
			//Collections.sort(javaFiles);
			return new ArrayList<JavaFile>(javaFiles);
		} catch (NotDirectoryException e) {
			return null;
		}
	}
	
	/**
	 * Recursively finds and all Java files and appends them to javaFiles ArrayList.
	 * Helper method to getAllJavaFiles.
	 *
	 * @param path
	 *            of directory of interest
	 * @param javaFiles
	 *            to append Java files to
	 * @return contents of all Java files as Strings
	 * @throws NotDirectoryException
	 *             if directory cannot be found
	 */
	private static void getAllJavaFilesFromDirectoryRecursiveHelper(String path, ArrayList<JavaFile> javaFiles) throws NotDirectoryException {
		// Get all the files in the directory
		File directory = new File(path);
		File[] files = directory.listFiles();
		// If this pathname does not denote a directory, then listFiles() returns null.
		if (files == null) {
			throw new NotDirectoryException(path);
		}

		// Iterate through all files in directory
		try {
			for (File file : files) {
				if (file.isDirectory()) {
					// Recursively search through inner directory
					String innerPath = file.getAbsolutePath();
					getAllJavaFilesFromDirectoryRecursiveHelper(innerPath, javaFiles);
					
				} else if (file.isFile() && file.getName().endsWith(JavaFile.EXTENSION)) {
					// Only accept Java files
					String javaName = file.getName();
					String javaPath = file.getAbsolutePath();
					String javaSource = FileManager.getFileContents(javaPath);
					JavaFile javaFile = new JavaFile(javaName, javaPath, javaSource);
					javaFiles.add(javaFile);
					
				}else if (FileManager.isValidJarFile(file.getAbsolutePath())) {
					
					//System.out.println("found valid jar in nested directory");
					String innerJarPath = file.getAbsolutePath();
					ArrayList<JavaFile> javaFilesInJar = getJavaContentsFromJar(innerJarPath);
					javaFiles.addAll(javaFilesInJar);
					
				}
				// else ignore file
			}
		} catch (IOException e) {
			e.printStackTrace();
			// This is already dealt with with NotDirectoryException
		}
	}

}
