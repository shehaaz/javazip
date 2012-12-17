
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** 
 * This command line tool takes two arguments
 * 1. The Root of the Windchill folder. e.g: "Z:/Windchill" (exactly as shown. NO forward slash (/) after "Windchill")
 * 2. a date, in this format yymmdd. e.g: 121130 (November 30th, 2012) 
 *  @author asaif
 * 

 */

public class FileZipOriginal {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat Date_format = new SimpleDateFormat("yyMMdd");
	private static int currentDate = Integer.parseInt(Date_format.format(cal.getTime())); //current date in Int format
	/**
	 * date inputed 
	 */
	private static int date; 
	private static String windchillFolder;


	public static void main(String[] args) throws Exception {

		System.out.println("Today's Date: " + currentDate);
		try 
		{	
			windchillFolder = args[0];
			date = Integer.parseInt(args[1]);
			//			new FileZipOriginal("C:/Users/Lenovo/Dropbox/javazip/src/INPUTLog", "C:/Users/Lenovo/Dropbox/javazip/src/OUTPUTLog.zip", true);
			new FileZipOriginal(windchillFolder+"/logs", "Z:/Windchill/logs/TechPack/ServerLogs.zip", true, true);
			new FileZipOriginal(windchillFolder+"/codebase", "Z:/Windchill/logs/TechPack/CodebaseFiles.zip", true,"codebaseFiles.txt");
			new FileZipOriginal(windchillFolder+"/logs/TechPack", "D:/Users/asaif/Desktop/TechPack.zip", true);
		}
		catch (ArrayIndexOutOfBoundsException e) //When no arguments are given ArrayIndexOutofBoundsException is thrown.
		{
			System.out.println("Please specify a date in this format: yymmdd");
		}
	}

	/**
	 * ZIP the server files (MethodServer and ServerManager)
	 * @param fileToZip
	 * @param zipFile
	 * @param excludeContainingFolder
	 * @param isServerLogs
	 * @throws IOException
	 */
	public FileZipOriginal(String fileToZip, String zipFile, boolean excludeContainingFolder, boolean isServerLogs)
			throws IOException {        
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    

		File srcFile = new File(fileToZip);

		if(excludeContainingFolder && srcFile.isDirectory()) {
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

		zipOut.flush();
		zipOut.close();
		System.out.println("Successfully created " + zipFile);
	}

	/**
	 * ZIP Folders containing property Files
	 * @param fileToZip
	 * @param zipFile
	 * @param excludeContainingFolder
	 * @param fileList
	 * @throws IOException
	 */
	public FileZipOriginal(String fileToZip, String zipFile, boolean excludeContainingFolder, String fileList)
			throws IOException {  

		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    

		File srcFile = new File(fileToZip);

		if(excludeContainingFolder && srcFile.isDirectory()) {
			for(String fileName : srcFile.list()) {	
				System.out.println("Loop to get file: "+ fileName);

				/*Reading text File*/
				FileReader file = null;
				BufferedReader reader = null;
				try {
					file = new FileReader("src/parsable/"+fileList); //This only works for codebase files...need to find the name by fileToZip
					reader = new BufferedReader(file);
					String line = "";
					while ((line = reader.readLine()) != null) { //Loop through the files and get the files names
						boolean b = Pattern.matches(line, fileName);
						if(b){
							addToZip("", fileToZip + "/" + fileName, zipOut); //Adding individual files to the addToZip
						}
					}
				} catch (FileNotFoundException e) {
					throw new RuntimeException("File not found");
				} catch (IOException e) {
					throw new RuntimeException("IO Error occured");
				} finally {
					if (file != null) {
						try {
							file.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				reader.close();
			} 
			/*Done Reading text File*/
		}
		zipOut.flush();
		zipOut.close();
		System.out.println("Successfully created " + zipFile);	
	}

	/**
	 * ZIP the FINAL TechPack
	 * @param fileToZip
	 * @param zipFile
	 * @param excludeContainingFolder
	 * @throws IOException
	 */

	public FileZipOriginal(String fileToZip, String zipFile, boolean excludeContainingFolder)
			throws IOException {        
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    

		File srcFile = new File(fileToZip);

		if(excludeContainingFolder && srcFile.isDirectory()) {
			for(String fileName : srcFile.list()) {
						addToZip("", fileToZip + "/" + fileName, zipOut);	
			}
		}
		zipOut.flush();
		zipOut.close();
		System.out.println("Successfully created " + zipFile);
	}

	/**
	 * This private method that adds File to the Zip
	 * @param path
	 * @param srcFile
	 * @param zipOut
	 * @throws IOException
	 */
	private void addToZip(String path, String srcFile, ZipOutputStream zipOut)
			throws IOException {        
		File file = new File(srcFile);
		String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();

		if (file.isDirectory()) {
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