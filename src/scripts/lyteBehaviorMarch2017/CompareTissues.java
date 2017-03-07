package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import utils.ConfigReader;

public class CompareTissues
{
	public static void main(String[] args) throws Exception
	{
		String[] sources = {"Cecal_Content", "feces", "jej", "ileum","duo" };
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
						+ File.separator + "tissueComparison.txt")));
		
		writer.write("taxaID");
		
		for(String s : sources)
		{
			writeAllButFirstTokenFromHeader(writer, s);	
		}
		writer.write("\n");
		
		List<HashMap<String, String>> lineMaps = new ArrayList<HashMap<String, String>>();
		
		for(String s : sources)
		{
			lineMaps.add(getLineMapForTissue(s));
		}
		
		HashSet<String> includeSet = new HashSet<String>();
		
		Integer numColumns = null;
		
		for(HashMap<String, String> map : lineMaps)
		{
			includeSet.addAll(map.keySet());
			
			for(String s : map.keySet())
			{
				String[] splits = map.get(s).split("\t");
				
				int length = splits.length - 1;
				
				if( numColumns == null)
					numColumns = length;
				
				if( numColumns != length ) 
					throw new Exception("No");
			}
		}
		
		for(String s : includeSet)
		{
			writer.write(s);
			
			
			for(HashMap<String, String> map : lineMaps)
			{
				writeAllButFirstTokenFromString(writer, map.get(s), numColumns);
			}
			
			writer.write("\n");
				
		}
		
		writer.flush();  writer.close();
	}
	

	private static void writeAllButFirstTokenFromString( BufferedWriter writer, String s, int numColumns) 
				throws Exception
	{
		if( s == null)
		{
			for(int x=0; x < numColumns; x++)
				writer.write("\t");
			
			return;
		}
		
		String[] splits = s.split("\t");
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x]);
	}
	
	private static void writeAllButFirstTokenFromHeader( BufferedWriter writer, String t) throws Exception
	{
		String[] splits =getFirstLine(t).split("\t");
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + t.charAt(0) + "_"+  splits[x]);
	}
	
	private static String getFirstLine(String t) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
						+ File.separator + 
						 "mainEffectsWithTaxaCalls_" + t+ ".txt"));
				
		String s= reader.readLine();
		
		reader.close();
		
		return s;
				
	}
	
	private static HashMap<String, String> getLineMapForTissue(String t) throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader =new BufferedReader(new FileReader(
		ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
				+ File.separator + 
				 "mainEffectsWithTaxaCalls_" + t+ ".txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String [] splits = s.split("\t");

			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			
			map.put(key,s);
		}
		
		reader.close();
		
		return map;
	}
}
