package scripts.vickie_2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import utils.ConfigReader;

public class InitialPivots
{
	public static final String[] LEVELS = 
	{
				"phylum", "class", "order", "family", "genus", "species"
	};
	
	public static void main(String[] args) throws Exception
	{
		for(String s : LEVELS)
		{
			System.out.println(s);
			pivot(s);
		}
	}
	
	private static void pivot( String level ) throws Exception
	{
		File inFile = new File(ConfigReader.getVicki2019Directory() + File.separator + 
				"processed_results" + File.separator + 
				"taxonomy_matrices_classified_only" + File.separator + 
				"bracken_" + level + "_reads.txt");

		File countOutFile = new File(ConfigReader.getVicki2019Directory() + File.separator + 
				"spreadsheets" + File.separator + 
				"bracken_" + level + "_counts.txt");
		
		HashMap<String, HashMap<String,Long>> countMap = 
				getMap(inFile);
		
		writeCountFile(countOutFile, countMap);
	}
	
	private static void writeCountFile(File outFile,HashMap<String, HashMap<String,Long>> countMap )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		List<String> samples = new ArrayList<>(countMap.keySet());
		
		HashSet<String> taxa = new LinkedHashSet<>();
		
		for(String s : countMap.keySet())
			taxa.addAll(countMap.get(s).keySet());
		
		List<String> taxaList = new ArrayList<>(taxa);
		
		writer.write("sample");
		
		for(String s : taxaList)
			writer.write("\t\"" + s + "\"");
		
		writer.write("\n");
		
		for(String sample : samples)
		{
			writer.write(sample);
			
			HashMap<String, Long> innerMap = countMap.get(sample);
			
			for(String aTaxa : taxaList)
			{
				Long aVal = innerMap.get(aTaxa);
				
				if( aVal == null)
					aVal =0l;
				
				writer.write("\t" + aVal);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	/*
	 * outer key is sample 
	 */
	private static HashMap<String, HashMap<String,Long>> getMap(File inFile ) throws Exception
	{
		HashMap<String, HashMap<String,Long>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			map.put(s, new LinkedHashMap<>());
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			
			String[] splits = s.split("\t");
			
			if( splits.length -1 != topSplits.length)
				throw new Exception("Mismatch " + splits.length  + " " + topSplits.length);
			
			String taxa = splits[0];
			
			for( int x=1;x < splits.length; x++)
			{
				HashMap<String, Long> innerMap = map.get(topSplits[x-1]);
				
				if( innerMap.containsKey(taxa))
					throw new Exception("Duplicate " + taxa );
				
				innerMap.put(taxa, Long.parseLong(splits[x]) );
			}
		}
		
		
		return map;
	}
}
