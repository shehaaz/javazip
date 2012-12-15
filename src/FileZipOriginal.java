
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author asaif
 *This command line class takes one argument, a date, in this format yymmdd. e.g: 121130 (November 30th, 2012) 
 */

public class FileZipOriginal {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat Date_format = new SimpleDateFormat("yyMMdd");
	private static int currentDate = Integer.parseInt(Date_format.format(cal.getTime())); //current date in Int format
	private static int date; //date inputed


	public static void main(String[] args) throws Exception {

		System.out.println("Today's Date: " + currentDate);
		try 
		{	
			date = Integer.parseInt(args[0]);
			new FileZipOriginal("C:/Users/Lenovo/Dropbox/javazip/src/INPUTLog", "C:/Users/Lenovo/Dropbox/javazip/src/OUTPUTLog.zip", true);
//			new FileZipOriginal("Z:/Windchill/logs", "Z:/Windchill/logs/TechPack/ServerLogs.zip", true, true);
//			new FileZipOriginal("Z:/Windchill/codebase", "Z:/Windchill/logs/TechPack/PropertyFiles.zip", true, false);
//			new FileZipOriginal("Z:/Windchill/logs/TechPack", "D:/Users/asaif/Desktop/TechPack.zip", true, false);
		}
		catch (ArrayIndexOutOfBoundsException e) //When no arguments are given ArrayIndexOutofBoundsException is thrown.
		{
			System.out.println("Please specify a date in this format: yymmdd");
		}
	}

	public FileZipOriginal(String fileToZip, String zipFile, boolean excludeContainingFolder, boolean isServerLogs)
			throws IOException {        
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    

		File srcFile = new File(fileToZip);
		
		if(excludeContainingFolder && srcFile.isDirectory()&& isServerLogs) {
			//System.out.println("In the directory");
			for(String fileName : srcFile.list()) {
				for(int i = date; i<=currentDate; i++){		//date entered is always smaller than the current date
					System.out.println("Loop to get file: "+ fileName);
					/*Matches any filename that contains the date*/
					boolean b = Pattern.matches(".*"+i+".*", fileName); 
					if(b){
						addToZip("", fileToZip + "/" + fileName, zipOut);
					}
				}
			}
		}
		
		if(excludeContainingFolder && srcFile.isDirectory()&& !isServerLogs) {
			//System.out.println("In the directory");
			for(String fileName : srcFile.list()) {
				for(int i = date; i<=currentDate; i++){		//date entered is always smaller than the current date
					System.out.println("Loop to get file: "+ fileName);
					/*Matches any filename that contains the date*/
					boolean b = Pattern.matches(".*"+i+".*", fileName); //This needs to be changed to check for all the files inside Locations.docx
					if(b){
						addToZip("", fileToZip + "/" + fileName, zipOut);
					}
				}
			}
		}
		

		zipOut.flush();
		zipOut.close();

		System.out.println("Successfully created " + zipFile);
	}

	private void addToZip(String path, String srcFile, ZipOutputStream zipOut)
			throws IOException {        
		File file = new File(srcFile);
		String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();

		if (file.isDirectory()) {
			//System.out.println("file is a directory");
			for (String fileName : file.list()) { 

				addToZip(filePath, srcFile + "/" + fileName, zipOut);
			}
		} else {
			System.out.println("Adding File: " + srcFile);
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