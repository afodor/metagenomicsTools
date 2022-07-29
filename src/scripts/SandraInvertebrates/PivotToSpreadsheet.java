package scripts.SandraInvertebrates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import utils.TabReader;

public class PivotToSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = getMap();
		System.out.println(map.size());
		
	//	for(String s : map.keySet())
		//	System.out.println(s + " " + map.get(s));
		writeOTUTable(map);
	}
	
	private static File writeOTUTable(HashMap<String, HashMap<String, Integer>> map ) throws Exception
	{
		File aFile = new File("C:\\\\SandraMacroinvetebrates\\\\otuGenus.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(aFile));
		
		HashSet<String> allGenus = new HashSet<>();
		
		for(String s : map.keySet())
			allGenus.addAll(map.get(s).keySet());
		
		List<String> allGenusList = new ArrayList<>();
		
		for(String s : allGenus)
			allGenusList.add(s);
		
		Collections.sort(allGenusList);
		
		writer.write("sample");
		
		for(String s : allGenusList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String s : map.keySet())
		{
			writer.write(s);
			
			HashMap<String, Integer> innerMap = map.get(s);
			
			for(String s2 : allGenusList)
			{
				Integer aVal = innerMap.get(s2);
				
				if( aVal == null)
					aVal = 0;
				
				writer.write("\t" + aVal);
				
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		return aFile;
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
				innerMap = new LinkedHashMap<>();
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
