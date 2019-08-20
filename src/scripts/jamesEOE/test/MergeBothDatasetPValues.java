package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MergeBothDatasetPValues
{
	private static class Holder
	{
		Double evanPValue = null;
		Double benitezPValue = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		addToMap(map, new File("C:\\JamesEOE\\test\\genusaf_FirstModelsEvan.txt"), true);
		addToMap(map, new File("C:\\JamesEOE\\test\\genusaf_FirstModelsBenitez.txt"), false);
		writeResults(map);
	}
	
	private static void writeResults( HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File("C:\\JamesEOE\\test\\genusMerged.txt")));
		
		writer.write("taxa\tevanP\tBenitezP\n");
		
		for(String s: map.keySet())
		{
			Holder h = map.get(s);
			
			writer.write(s);
			
			writer.write( ( h.evanPValue == null ? "NA" :h.evanPValue )  + "\t" );
			writer.write( ( h.benitezPValue== null ? "NA" :h.benitezPValue )  + "\t" );
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
			
			Holder h = map.get(splits[1]);
			
			double pValue = Math.log10( Double.parseDouble(splits[0]));
			
			if( Double.parseDouble(splits[5]) > Double.parseDouble(splits[5]) )
				pValue = -pValue;
			
			if( h== null)
			{
				h = new Holder();
				map.put(splits[1], h);
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
