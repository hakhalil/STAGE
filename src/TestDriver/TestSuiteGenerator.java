package TestDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSuiteGenerator {
	protected static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    FileInputStream fileInputStream = null;
	    FileOutputStream fileOutputStream = null;

	    try {
	        fileInputStream = new FileInputStream(sourceFile);
			source = fileInputStream.getChannel();
	        fileOutputStream = new FileOutputStream(destFile);
			destination = fileOutputStream.getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	    	
	    	
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	        
	        if(fileInputStream != null) fileInputStream.close();
	    	if(fileOutputStream != null) fileOutputStream.close();
	    }
	}

	public void loopOnTestSuites(String dirName, String tetJUnitFile) throws IOException
	{
		File dir = new File(dirName);
		File[] directoryListing = dir.listFiles();
		File testSuiteFile = null;
		String unitTestFileName = new String("src/"+dirName.substring(dirName.lastIndexOf("TestDriver"))+"/"+tetJUnitFile);
		File unitTestFile = new File(unitTestFileName);
		if (!unitTestFile.exists())
			System.out.println("No such file exists ENUM");
		if (directoryListing != null) {
			for (File testSuite : directoryListing) {

				String fileName = testSuite.getName();
				String extension = fileName.substring(fileName.lastIndexOf('.'));
				if ( extension.equals(".txt")){
					testSuiteFile = new File("src/"+dirName.substring(dirName.lastIndexOf("TestDriver"))+"/"+fileName.substring(0,fileName.lastIndexOf('.'))+".java");
					//copyFile(unitTestFile, testSuiteFile);
					Path path = Paths.get(unitTestFile.getAbsolutePath());
					Charset charset = StandardCharsets.UTF_8;

					String content = new String(Files.readAllBytes(path), charset);
					String testSuiteName = testSuite.getName().substring(0,fileName.lastIndexOf('.'));
					content = content.replaceAll("ReplaceThis", testSuiteName);
					path = Paths.get(testSuiteFile.getAbsolutePath());
					Files.write(path, content.getBytes(charset));
					

					//RunTestCases(testSuite.getAbsolutePath());
			
					
				}
			}
		} else {
			//TBD: handle errors
		}
	}
}