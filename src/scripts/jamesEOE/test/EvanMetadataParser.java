package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class EvanMetadataParser
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String,Integer> map = getEvanCaseControlMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
	
	private static HashMap<String,Integer> getEvanCaseControlMap( ) throws Exception
	{
		HashMap<String,Integer> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getJamesEoeDirectory() + File.separator + 
				"test" + File.separator + "Evan_Meta_data.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Integer.parseInt(splits[1]));
		}
		
		return map;

	}
}
