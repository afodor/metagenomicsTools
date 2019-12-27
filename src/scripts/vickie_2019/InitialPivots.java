package scripts.vickie_2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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
	}
	
	/*
	 * outer key is sample 
	 */
	private static HashMap<String, HashMap<String,Long>> getMap(File inFile ) throws Exception
	{
		HashMap<String, HashMap<String,Long>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			map.put(s, new HashMap<>());
		
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
