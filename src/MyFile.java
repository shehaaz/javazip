import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MyFile {


	public static void main (String[] args) throws IOException {

		String returnValue = "";
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader("src/parsable/codebaseFiles.txt");
			 reader = new BufferedReader(file);
			String line = "";
			while ((line = reader.readLine()) != null) {
				returnValue = line;
				System.out.println(returnValue);
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
}
