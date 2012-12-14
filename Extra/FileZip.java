//Plan is to run the old version of the code and see what path it takes when a directory is given
//once we have the path...filter out the files that have the date using REGEX

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZip {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static String directory = "C:/Users/Lenovo/Dropbox/javazip/src/ServerManager121130";

	public static void main(String[] args) throws Exception {
		System.out.println("Initializing");
		new FileZip(directory, "MethodServer-" + args[0], "MethodServer" + args[0] + ".zip");
		new FileZip(directory, "ServerManager-" + args[0], "ServerManager" + args[0] + ".zip");
		System.out.println("End Initializing");
	}

	public FileZip(String directory, String fileToZip, String zipFile)
			throws IOException {        
		System.out.println("Making new Zip Container");
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    
		System.out.println("Adding to Zip");
		addToZip("", fileToZip, zipOut);
		System.out.println("Done Adding to Zip");

		zipOut.flush();
		zipOut.close();

		System.out.println("Successfully created " + zipFile);
	}

	private void addToZip(String path, String srcFile, ZipOutputStream zipOut)
			throws IOException {        
		File file = new File(directory);
		String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();
		
		if (file.isDirectory()) {
			System.out.println("File is a directory");
			for (String fileName : file.list()) { 
				boolean b = Pattern.matches(srcFile+"*", "fileName");
				if(b){
				System.out.println("Matches Reg Ex");
				addToZip(filePath, srcFile + "/" + fileName, zipOut);
				}
			}
		} else {
			zipOut.putNextEntry(new ZipEntry(filePath));
			FileInputStream in = new FileInputStream(srcFile);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}

			in.close();
		}
	}
}