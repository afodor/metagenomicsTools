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
import java.util.Formatter.BigDecimalLayoutForm;

import utils.TabReader;

public class PivotToSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = getMap();
		System.out.println(map.size());
		
	//	for(String s : map.keySet())
		//	System.out.println(s + " " + map.get(s));
		File otuFile = writeOTUTable(map);
		
		HashMap<String, Holder> metaMap = getDateMeta();
		System.out.println(metaMap);
		
		addMeta(otuFile, metaMap);
	}
	
	private static void addMeta( File inFile, HashMap<String, Holder> metaMap ) throws Exception
	{
		File aFile = new File("C:\\SandraMacroinvetebrates\\otuFamilyPlusMeta.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(aFile));
		
		BufferedReader reader = new BufferedReader( new FileReader(inFile));
		
		writer.write("sample\tseason\tprePost");
		
		String[] topSplits =reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
					
			String sample = splits[0].trim();
			String date = sample.substring(sample.indexOf("@") +1, sample.length()).trim();
			Holder h = metaMap.get(date);
			
			if( h== null)
				throw new Exception("Could not find date " + date);
			
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static class Holder
	{
		String season;
		String prepost;
		
		@Override
		public String toString()
		{
			return season +  " " + prepost;
		}
	}
	
	@SuppressWarnings("resource")
	public static HashMap<String, Holder> getDateMeta() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\SandraMacroinvetebrates\\Restoration dates.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			System.out.println(s);
			String[] splits =s.split("\t");
			
			String date = splits[0];
			
			if( map.containsKey(date))
				System.out.println("duplicate " + date);
			
			Holder h = new Holder();
			h.prepost = splits[2];
			h.season = splits[1];
			map.put(date, h);
		}
		
		reader.close();
		return map;
	}
	
	private static File writeOTUTable(HashMap<String, HashMap<String, Integer>> map ) throws Exception
	{
		File aFile = new File("C:\\SandraMacroinvetebrates\\otuFamily.txt");
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
			
			tReader.nextToken();
			/*
			String genus = tReader.nextToken().trim();
			
			if( genus.length() == 0 )
				genus = "unclassified_" + family;
			*/
			
			String key = site + "@" + date;
			
			System.out.println(key + " " + site);
			
			HashMap<String, Integer> innerMap = map.get(key);
			
			if( innerMap == null)
			{
				innerMap = new LinkedHashMap<>();
				map.put(key, innerMap);
			}
			
			Integer aVal = innerMap.get(family);
			
			if( aVal == null)
				aVal = 0;
			
			tReader.nextToken();
			aVal = aVal + Integer.parseInt(tReader.nextToken());
			innerMap.put(family, aVal);
		}
		
		reader.close();
		return map;
	}
}
