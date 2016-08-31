package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import utils.ConfigReader;
import utils.Translate;

public class SampleMap
{
	/*
	 * The key is the reverse transcription of the 4th column + "@" + the 9th column
	 * 
	 */
	public static HashMap<String, String> getPrimersToSampleMap() throws Exception
	{
		HashMap<String, String> map= new HashMap<String, String>();

		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + 
				"Copy of DHSV Illumina Sets 1 and 2 Metadata.txt")));
		
		reader.readLine();
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = Translate.reverseTranscribe(splits[4]) + "@" + 
								splits[9];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key");
			
			map.put(key, new String( splits[0]) + "_" + key);
			
		}
		
		reader.close();
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s : map.values())
		{
			if( set.contains(s))
				throw new Exception("Duplicate value " + s);
			
			set.add(s);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getPrimersToSampleMap();
		
		for(String s : map.keySet())
			System.out.println(s +  " " + map.get(s));
	}
	
}
