package ASTNodeExamples;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;

public class TestMultipleCatchBlock{
	
	public static void main(String args[]){
		try{
			int a[]=new int[5];  
			a[5]=30/0;
			HashMap<Integer,Boolean> test = new HashMap<Integer,Boolean>();
			test.clear();
		}  
		catch(ArithmeticException e){
			System.out.println("task1 is completed");
		}  
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("task 2 completed");
		}  
		catch(Exception e){
			System.out.println("common task completed");
		}
		System.out.println("rest of the code..."); 
	}  
	
	public static void writeToFileZipFileContents(String zipFileName, String outputFileName) throws java.io.IOException {
		java.nio.charset.Charset charset = java.nio.charset.StandardCharsets.US_ASCII;
		java.nio.file.Path outputFilePath = java.nio.file.Paths.get(outputFileName);

		try (java.util.zip.ZipFile zf = new java.util.zip.ZipFile(zipFileName);
			 java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(outputFilePath, charset)) {

			for (Enumeration<? extends ZipEntry> entries = zf.entries(); entries.hasMoreElements();) {
				String newLine = System.getProperty("line.separator");
				String zipEntryName =((java.util.zip.ZipEntry)entries.nextElement()).getName() + newLine;
				writer.write(zipEntryName, 0, zipEntryName.length());
			}
		}
	}
}  