package scripts.jamesEOE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.TabReader;

public class QuickNormalizationCheck
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(new File("C:\\JamesEOE\\genus.tsv"));
		
		HashMap<String, HashMap<String,Double>>  map = getNormalizedMap();
	}
	
	// outer key is sample; inner key is taxa
	private static HashMap<String, HashMap<String,Double>> getNormalizedMap() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\JamesEOE\\genus_Normalized.tsv")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			String key = tReader.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
		}
		
		return map;
	}
}
