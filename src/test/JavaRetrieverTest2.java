package test;

import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

import main.file.File;
import main.file.JavaFile;
import main.file.JavaRetriever;

/**
 * Checks that JavaRetriever works.
 *
 * @author Evan Quan
 * @version 1.1.0
 * @since 19 March 2018
 *
 */
public class JavaRetrieverTest2 {
	public static void main(String[] args) {
		ArrayList<JavaFile> javaFiles;
		try {
			javaFiles = JavaRetriever.getJavaContents(_TestSuite.JAVA_RETRIEVER_TEST_DIR);
			for (File javaFile : javaFiles) {
				System.out.println(javaFile);
			}
		} catch (NotDirectoryException e) {
			e.printStackTrace();
		}


	}
}
