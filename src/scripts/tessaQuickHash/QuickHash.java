package scripts.tessaQuickHash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class QuickHash
{
	public static void main(String[] args) throws Exception
	{
		parseFile();
	}
	
	// key is the patient identifier; values are lists of antibiotics
	private static HashMap<String, List<String>> parseFile() throws Exception
	{
		HashMap<String, List<String>>  map = new HashMap<String, List<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader("c:\\Temp\\apple.txt"));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			System.out.println(s);
		}
		
		reader.close();
		return map;
	}
}
