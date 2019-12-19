package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class MergeDada2PValues
{
	private static class Holder
	{
		Double evanPValue = null;
		Double benitezPValue = null;
		Double evanDada2 = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		addToMap(map, new File("C:\\JamesEOE\\test\\genusaf_FirstModelsEvan.txt"), true);
		addToMap(map, new File("C:\\JamesEOE\\test\\genusaf_FirstModelsBenitez.txt"), false);
		addDada2(map, new File("C:\\JamesEOE\\DADA2_genus_case_simple_models.txt"));
		writeResults(map);
	}
	
	private static void addDada2(HashMap<String, Holder> map , File f) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "").trim();
			Holder h = map.get(key);
			
			double pValue =Double.parseDouble(splits[1]);
			
			if( h== null)
			{
				h = new Holder();
				map.put(key, h);
			}
			
			if( h.evanDada2!= null)
					throw new Exception("No");
			
			h.evanDada2 = pValue;
		}
		
		
		reader.close();
	}
	
	private static String getValOrNA( Double d  )
	{
		if( d == null)
			return "NA";
		
		return "" + d;
		
	}
	
	private static void writeResults( HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File("C:\\JamesEOE\\test\\genusMergedDada2.txt")));
		
		writer.write("taxa\tevanP\tBenitezP\tevanDada2\n");
		
		for(String s: map.keySet())
		{
			Holder h = map.get(s);
			
			writer.write(s + "\t");
			
			writer.write( getValOrNA(h.evanPValue) + "\t" );
			writer.write( getValOrNA(h.benitezPValue) + "\t" );
			writer.write( getValOrNA(h.evanDada2) + "\n");
		}
		
		
		writer.flush();  writer.close();
	}
	
	private static void addToMap(HashMap<String, Holder> map, File f, boolean isEvan)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[1].replaceAll("\"", "").trim();
			Holder h = map.get(key);
			
			double pValue =Double.parseDouble(splits[0]);
			
			if( h== null)
			{
				h = new Holder();
				map.put(key, h);
			}
			
			if( isEvan )
			{
				if( h.evanPValue != null)
					throw new Exception("No");
				
				h.evanPValue = pValue;
			}
			else
			{
				if( h.benitezPValue != null)
					throw new Exception("No");
				
				h.benitezPValue = pValue;
			}
		}
		
		
		reader.close();
	}
}
