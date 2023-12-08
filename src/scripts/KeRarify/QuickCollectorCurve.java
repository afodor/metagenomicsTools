package scripts.KeRarify;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.OtuWrapper;

public class QuickCollectorCurve
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\keRarify\\amr_transposed.txt");
		
		LinkedHashMap<String, HashMap<Integer, Integer>> collapsedMap = 
				getCollapsedMap(wrapper);
		
		System.out.println(collapsedMap);
		
		
		
		for(String s : collapsedMap.keySet())
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\keRarify\\curve" + s + ".txt")));
			
			writer.write("sampleDepth\tgenesSeen\n");
			
			HashMap<Integer, Integer> innerMap = collapsedMap.get(s);
			
			for( Integer i : innerMap.keySet())
				writer.write((i+1) + "\t" + innerMap.get(i) + "\n");
			
			writer.flush(); writer.close();
					
				
		}
	}
	
	//outer key is the sample name
			// inner map - key is rarefication depth; value is the taxa count at that depth
	private static LinkedHashMap<String, HashMap<Integer, Integer>> getCollapsedMap(OtuWrapper wrapper) 
		throws Exception
	{
		LinkedHashMap<String, HashMap<Integer, Integer>> map = new LinkedHashMap<>();
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName =wrapper.getSampleNames().get(x);
			
			if( map.containsKey(sampleName))
				throw new Exception("Duplicate key");
			
			HashMap<Integer, Integer> innerMap = new LinkedHashMap<>();
			map.put(sampleName, innerMap);
			
			List<String> countList = getCountsList(wrapper, x);
			
			System.out.println(countList);
			
			Collections.shuffle(countList);
			System.out.println(countList);
			HashSet<String> taxaSeen = new HashSet<>();
			
			for( int y=0; y < countList.size(); y++ )
			{
				taxaSeen.add(countList.get(y));
				innerMap.put(y, taxaSeen.size());
			}
			
			return map;
			
		}
		
		return map;
	}
	
	private static List<String> getCountsList(OtuWrapper wrapper, int taxaIndex) throws Exception
	{
		List<String> countList = new ArrayList<>();
		
		for( int y=0; y < wrapper.getDataPointsUnnormalized().get(taxaIndex).size() ; y++)
		{
			int countCal = wrapper.getDataPointsUnnormalized().get(taxaIndex).get(y).intValue();
			//System.out.println(sampleName + " " +  y + " " +  countCal);
			
			for( int z=0; z < countCal; z++)
				countList.add(wrapper.getOtuNames().get(y));
		}
		
		return countList;
		
	}

}
