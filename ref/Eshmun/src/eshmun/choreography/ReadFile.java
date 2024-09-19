package eshmun.choreography;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ReadFile {
	String fileName;
	public ReadFile (String fileName) {
		this.fileName = fileName;
	}
	
	public LinkedList<String> read() throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		LinkedList<String> fileByLine = new LinkedList<String>();
		String line = reader.readLine();
		while(line != null) {
			
			if(line.startsWith("--"))
			{
				System.out.println("hello");
				line = reader.readLine();
				continue;
			}
			
			fileByLine.add(line);
			line = reader.readLine();
		}
		reader.close();
		return fileByLine;
	}
}
