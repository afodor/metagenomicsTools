package scripts.KeTessaLookup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

import utils.TabReader;

public class CheckOverlap
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> vSearchToDrugMap = getVSearchToDrugClassMap();
		HashSet<String> vSearchNames = getVsearchNames();
		
		/*
		for(String s : vSearchNames)
		{
			if( ! vSearchToDrugMap.containsKey(s) )
			{
				System.out.println("Could not find key " + s);
			}
			else
			{
				System.out.println("found " + s + " " + vSearchToDrugMap.get(s));
			}
		}
		*/
		
		
		/*
		int x=0;
		
		
		for(String s : map.keySet())
		{
			System.out.println(s + " " + map.get(s));
			x++;
			
			if( x==100)
				System.exit(1);
		}
		*/
			
		
		TreeSet<String> allValues = getAllValues(vSearchToDrugMap);
		
		for(String s : allValues)
			System.out.println(s);
	}
	
	private static TreeSet<String> getAllValues(HashMap<String, String> map ) throws Exception
	{
		TreeSet<String> set = new TreeSet<String>();
		
		for(String s : map.values())
		{
			StringTokenizer sToken = new StringTokenizer(s, ";");
			
			while(sToken.hasMoreTokens())
				set.add(sToken.nextToken().trim());
		}
		
		return set;
	}
	
	@SuppressWarnings("resource")
	private static HashSet<String> getVsearchNames() throws Exception
	{
		HashSet<String> set = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\ke_Tessa_lookup\\vsearchFiltered.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String key = sToken.nextToken();
			
			if( set.contains(key))
				throw new Exception("Duplicate key " + key);
			
			set.add(key);
		}
		
		reader.close();
		return set;
	}
	
	@SuppressWarnings("resource")
	private static HashMap<String, String> getVSearchToDrugClassMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\ke_Tessa_lookup\\MASTER_AMRlist_2023_06_29.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null ; s =reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			for( int x=1; x <= 6; x++)
				tReader.nextToken();
		
			String firstKey = tReader.nextToken().trim();
			String secondKey = tReader.nextToken().trim();
			
			for( int x=1; x <= 16; x++)
				tReader.nextToken();
			
			if( firstKey.length() == 0 )
				throw new Exception();
			
			String value = tReader.nextToken();
			addToMap(map, firstKey, value);
			
			if( secondKey.length() > 0 )
				addToMap(map, secondKey, value);
		}
		
		reader.close();
		
		return map;
	}
	
	private static void addToMap( HashMap<String, String> map, String key, String value ) throws Exception
	{
		if( map.containsKey(key))
		{
			String oldValue = map.get(key);
			
			if( ! oldValue.equals(value))
				throw new Exception("Duplicate with different value " + key  + " " + oldValue + " "+ value);
			
		}
			
		map.put(key, value);
	}
}
