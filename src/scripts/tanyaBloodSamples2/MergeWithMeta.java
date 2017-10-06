package scripts.tanyaBloodSamples2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MergeWithMeta
{
	private final static String[] TAXA_LEVELS = 
		{"Phylum", "Class", "Order", "Family", "Genus", "Species", "OTU" };
	
	public static void main(String[] args) throws Exception
	{
		for(String taxa :TAXA_LEVELS)
		{
			HashMap<String, HashMap<String,Double>>  map = new HashMap<>();
			
			File metaFile = new File(ConfigReader.getTanyaBloodDir2() + File.separator + 
						"Blood " + taxa +" META 18July16.txt");
			File proFile = new File(ConfigReader.getTanyaBloodDir2() + File.separator + 
						"Blood " + taxa +" PRO 18July16.txt");
			
			parseAFile(metaFile, map);
			parseAFile(proFile , map);
			
			for(String s : map.keySet())
				System.out.println(s +  " " + map.get(s));
		}
	}
	
	private static void parseAFile(File f,HashMap<String, HashMap<String,Double>>  map) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s= reader.readLine(); s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != topSplits.length)
				throw new Exception("No");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			HashMap<String,Double> innerMap = new HashMap<>();
			
			map.put(key, innerMap);
			
			for( int x=1; x< topSplits.length; x++)
			{
				String taxa = topSplits[x];
				
				if(innerMap.containsKey(taxa))
					throw new Exception("No");
				
				innerMap.put(taxa, Double.parseDouble(splits[x]));
			}
		}
		
		reader.close();
	}
}
