package scripts.LyteNov2016.mattFiles.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class AddExperimentToSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = sampleToExp();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
	
	private static HashMap<String, String> sampleToExp() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\lyte_Nov10_2016\\Lyte_seqs_11102016\\mattFiles\\ArgonneMetadata.txt")));
		
		reader.readLine();
		
		HashMap<String, String> map = new HashMap<String,String>();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[3]);
		}
		
		return map;
	}
}
