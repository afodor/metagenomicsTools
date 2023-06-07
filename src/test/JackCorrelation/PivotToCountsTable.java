package test.JackCorrelation;

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
import java.util.StringTokenizer;

public class PivotToCountsTable
{
	public static void main(String[] args) throws Exception
	{
		File directoryToParse = new File("C:\\Jack_correlation\\standard_kraken2_db_with_fungi_reports");
		HashMap<String, HashMap<String, Integer>> map = getMapFromKraken(directoryToParse);
		
		File outFile = new File("C:\\Jack_correlation\\pivotedGenus.txt");
		
		System.out.println("Writing to " + outFile.getAbsolutePath());
		writeResults(map, outFile);
	}
	 
	private static void writeResults( HashMap<String, HashMap<String, Integer>> map , File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		List<String> samples = new ArrayList<>(map.keySet());
		Collections.sort(samples);
		
		HashSet<String> taxaSet = new HashSet<>();
		
		for(String s : map.keySet())
			for(String s2: map.get(s).keySet())
				taxaSet.add(s2);
		
		List<String> taxa = new ArrayList<>(taxaSet);
		Collections.sort(taxa);
		
		writer.write("sample");
		
		for(String s : taxa)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String s : samples)
		{
			writer.write(s);
			
			HashMap<String,Integer> innerMap = map.get(s);
			
			for(String s2 : taxa)
			{
				Integer aVal = innerMap.get(s2);
				
				if( aVal == null)
					aVal = 0;
				
				writer.write("\t" + aVal);
			}
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
	}
	
	// outer map is sampleID
	private static HashMap<String, HashMap<String, Integer>> getMapFromKraken(File directoryToParse) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
		
		String[] files = directoryToParse.list();
		
		for(String s : files)
		{
			File f= new File( directoryToParse.getAbsolutePath() + File.separator + s );
			HashMap<String, Integer> innerMap = parseAFile(f);
			String key = new StringTokenizer(s, "_").nextToken();
			System.out.println(key);
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			map.put(key, innerMap);
			
		}
		
		return map;
	}
	
	private static HashMap<String, Integer> parseAFile(File file) throws Exception
	{
		//System.out.println(file.getAbsolutePath());
		HashMap<String, Integer> map = new HashMap<>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 2)
				throw new Exception("parsing error");
			
			String genus = getAGenusOrNull(splits[0]);
			
			if( genus != null)
			{
				if( map.containsKey(genus))
					throw new Exception("Parsing error " + genus);
				
				map.put(genus, Integer.parseInt(splits[1]));
				
			}
		}
		
		reader.close();
		
		return map;
	}
	
	private static String getAGenusOrNull( String firstToken ) 
	{
		String[] splits = firstToken.split("\\|");
		
		if( splits[splits.length-1].startsWith("g__"))
		{
			String genus = splits[splits.length-1].substring(3);
			return genus;
		}
		
		return null;
	}
}
