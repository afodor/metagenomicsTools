package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class CompareTissues
{
	public static void main(String[] args) throws Exception
	{
		String s1 ="Cecal_Content";
		String s2 = "feces";
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
						+ File.separator + s1 + "vs" + s2 + ".txt")));
		
		writer.write("taxaID");
		writeAllButFirstTokenFromHeader(writer, s1);
		writeAllButFirstTokenFromHeader(writer, s2);
		writer.write("\n");
		
		HashMap<String, String> map1 = getLineMapForTissue(s1);
		HashMap<String, String> map2 = getLineMapForTissue(s2);
		
		for(String key1 : map1.keySet())
		{
			if( map2.containsKey(key1))
			{
				writer.write(key1);
				writeAllButFirstTokenFromString(writer, map1.get(key1));
				writeAllButFirstTokenFromString(writer, map2.get(key1));
				writer.write("\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	

	private static void writeAllButFirstTokenFromString( BufferedWriter writer, String s) throws Exception
	{
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
