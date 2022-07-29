package scripts.SandraInvertebrates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class PivotToSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = getMap();
		System.out.println(map.size());
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
	
	// outer key is site@Date; inner key is genus
	private static HashMap<String, HashMap<String, Integer>> getMap() throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\SandraMacroinvetebrates\\Macroinverts species.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			tReader.nextToken();
			String date = tReader.nextToken();
			String site = tReader.nextToken();
			
			tReader.nextToken();
			
			String order = tReader.nextToken().trim();
			
			String family = tReader.nextToken().trim();
			
			if( family.length() == 0)
				family = "unclassified_" + order;
			
			String genus = tReader.nextToken().trim();
			
			if( genus.length() == 0 )
				genus = "unclassified_" + family;
			
			String key = site + "@" + date;
			
			System.out.println(key + " " + site);
			
			HashMap<String, Integer> innerMap = map.get(key);
			
			if( innerMap == null)
			{
				innerMap = new HashMap<>();
				map.put(key, innerMap);
			}
			
			Integer aVal = innerMap.get(genus);
			
			if( aVal == null)
				aVal = 0;
			
			tReader.nextToken();
			aVal = aVal + Integer.parseInt(tReader.nextToken());
			innerMap.put(genus, aVal);
		}
		
		reader.close();
		return map;
	}
}
