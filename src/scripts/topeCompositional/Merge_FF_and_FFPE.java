package scripts.topeCompositional;

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
import java.util.StringTokenizer;

public class Merge_FF_and_FFPE
{
	public static void main(String[] args) throws Exception
	{
		
		 HashMap<String, Integer> countMap = getFFCounts();
		 add_FFPE_counts(countMap);
		 writeMerged(countMap);
		 
	}
	
	private static void writeMerged(HashMap<String, Integer> countMap ) throws Exception
	{
		HashSet<String> sampleSet = new HashSet<>();
		HashSet<String> taxaSet = new HashSet<>();
		
		for(String s : countMap.keySet())
		{
			StringTokenizer sToken = new StringTokenizer(s, "@");
			sampleSet.add(sToken.nextToken());
			taxaSet.add(sToken.nextToken());
		}
		
		List<String> sampleList = new ArrayList<>(sampleSet);
		List<String> taxaList = new ArrayList<>(taxaSet);
		Collections.sort(sampleList);
		Collections.sort(taxaList);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\topeComparisonData\\merged.txt")));
		
		writer.write("taxa");
		
		for(String s: sampleList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String taxa : taxaList)
		{
			writer.write(taxa);
			
			for(String sample : sampleList)
			{
				String key = sample + "@" + taxa;
				Integer aVal = countMap.get(key);
				
				if( aVal == null)
					aVal = 0;
				
				writer.write("\t" + aVal);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	//"Key is Sample_x_ffpe@taxa ; value is count"
	// samples ending in _2 are ignored
	@SuppressWarnings("resource")
	private static void add_FFPE_counts( HashMap<String, Integer> map ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\topeComparisonData\\FFPE_OTU_metaRemoved.txt")));
		
		String[] topSplits =  reader.readLine().split("\t");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("Parsing error " + splits[0] + " " + topSplits.length + " " + splits.length);
			
			String sample = splits[0];
			
			if( sample.endsWith("-1"))
			{
				StringTokenizer sToken = new StringTokenizer(sample, "-");
				Integer topeIndex = Integer.parseInt(sToken.nextToken());
				
				String keyPrefix = "Sample_" + topeIndex +"_ffpe@";
				
				for( int x= 1; x < splits.length; x++)
				{
					String key = keyPrefix + topSplits[x];
					
					if( map.containsKey(key))
						throw new Exception("Parsing error " + key);
					
					map.put(key, Integer.parseInt(splits[x]));
				}
			}
			else
			{
				System.out.println("Ignoring " + sample);
			}
		}
	
	}
	
	//"Key is Sample_x_ff@taxa ; value is count"
	@SuppressWarnings("resource")
	private static HashMap<String, Integer> getFFCounts() throws Exception
	{
		HashMap<String, Integer> ffMetaMap = getIntegerToKeyMap();
		
		HashMap<String, Integer> countMap = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\topeComparisonData\\FF_OTU_metaRemoved.txt")));
		
		String[] topSplits =  reader.readLine().split("\t");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("Parsing error");
			
			Integer topeIndex = ffMetaMap.get(splits[0]);
			
			if( topeIndex == null)
				throw new Exception("Could not map " + splits[0]);
			
			String keyPrefix = "Sample_" + topeIndex +"_ff@";
			
			for( int x= 1; x < splits.length; x++)
			{
				String key = keyPrefix + topSplits[x];
				
				if( countMap.containsKey(key))
					throw new Exception("Parsing error " + key);
				
				countMap.put(key, Integer.parseInt(splits[x]));
			}
		}
		
		return countMap;
	}
	
	@SuppressWarnings({  "resource" })
	private static HashMap<String, Integer> getIntegerToKeyMap() throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\topeComparisonData\\FF_Metadata.txt")));
		
		reader.readLine();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("No");
			
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			
			sToken.nextToken(); sToken.nextToken();
			
			int aValue = Integer.parseInt(sToken.nextToken().replace("U", ""));
			
			map.put(splits[1], aValue);
			
			
		}
		
		return map;
	}
}
