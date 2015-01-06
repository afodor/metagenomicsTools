package scripts.ratSach2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class GreengenesOtuLookup
{
	public static HashMap<String, String> getLookupMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"otu_table.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate");
			
			map.put(key, splits[splits.length-1]);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getLookupMap();
		
		for(String s : map.keySet())
			System.out.println(s  + " " + map.get(s));
	}
}
