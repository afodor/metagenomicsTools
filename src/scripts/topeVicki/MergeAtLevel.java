package scripts.topeVicki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeAtLevel
{
	public static void main(String[] args) throws Exception
	{
		String[] levels = { "p", "c","o","f","g" };
		
		for(String level : levels)
		{
			Map<String, Map<String,Integer>> map = mergeAtLevel(level);
			writeFile(map, level);
		}
	}
	
	private static Map<String, Map<String,Integer>> mergeAtLevel(String level ) throws Exception
	{
		System.out.println(level);
		Map<String, Map<String,Integer>> map = new HashMap<>();
		
		File file1 = new File(ConfigReader.getTopeVickiDir() + File.separator + "raw1.txt");
		File file2 = new File(ConfigReader.getTopeVickiDir() + File.separator + "raw2.txt");
		
		addToMap(map, file1, level);
		addToMap(map, file2, level);
		
		return map;
	}
	 
	private static void writeFile( Map<String, Map<String,Integer>>  map, String level)
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getTopeVickiDir() + File.separator + level + "_mergedRaw.txxt"	)));
		
		writer.write("sample");
		
		HashSet<String> taxa = new HashSet<>();
		for(String s : map.keySet())
		{
			taxa.addAll(map.get(s).keySet());
		}
		
		List<String> taxaList =new ArrayList<>(taxa);
		Collections.sort(taxaList);
		
		for(String s : taxaList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String s : map.keySet())
		{
			writer.write(s);
			
			Map<String, Integer> innerMap = map.get(s);
			
			for(String t : taxaList)
			{
				Integer count = innerMap.get(t);
				
				if( count == null)
					count =0;
				
				writer.write("\t" + count);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addToMap(Map<String, Map<String,Integer>>  map, File file, String level) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		String[] topSplits =reader.readLine().split("\t");
		
		for(String s= reader.readLine(); s  != null; s= reader.readLine())
		{
			String taxa = getTaxa(s, level);
			
			if( taxa != null)
			{
				String[] splits = s.split("\t");
				
				if( splits.length != topSplits.length)
					throw new Exception("No");
				
				for( int x=1; x < splits.length-1; x++)
				{
					Map<String,Integer> innerMap = map.get(topSplits[x]);
					
					if( innerMap == null)
					{
						innerMap = new HashMap<>();
						map.put(topSplits[x], innerMap);						
					}
					
					innerMap.put(taxa, Integer.parseInt(splits[x]));
				}
			}
		}
		
		reader.close();
	}
	
	private static String getTaxa(String s, String level) throws Exception
	{
		String[] splits = s.split("\t");
		String last = splits[splits.length -1];
		
		StringTokenizer sToken = new StringTokenizer(last,";");
		
		while(sToken.hasMoreTokens())
		{
			String taxa =sToken.nextToken().trim();
			
			if( taxa.equals("Unassigned"))
				return null;
			
			if( taxa.startsWith(level + "__"))
			{
				taxa = taxa.replace(level + "__", "" ).trim();
				
				if( taxa.length() == 0 || taxa.equals("Unassigned") )
					return null;
				
				return taxa;
			}
		}
		
		return null;
	}
}
