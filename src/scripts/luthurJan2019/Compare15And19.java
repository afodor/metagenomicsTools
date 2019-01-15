package scripts.luthurJan2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

import utils.ConfigReader;

import javax.tools.JavaFileManager.Location;


public class Compare15And19
{
	private static class Holder
	{
		Double pValue15;
		Double pValue19;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new LinkedHashMap<String, Compare15And19.Holder>();
		add15(map);
		add19(map);
		writeResults(map);
	}
	
	
	private static void writeResults(HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLuthurJan2019Dir() + File.separator + "data" + File.separator + 
				"15Vs19Input.txt")));
		
		writer.write("taxa\tpVal15\tpVal19\n");
		
		for(String s : map.keySet())
		{
			writer.write(s + "\t");
			writer.write((map.get(s).pValue15 == null ? "NA" : map.get(s).pValue15) + "\t");
			writer.write((map.get(s).pValue19 == null ? "NA" : map.get(s).pValue19) + "\n");
			
		}
		
		
		writer.flush();  writer.close();
	
	}
	
	private static void add19(HashMap<String, Holder> map ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir()
				+ File.separator + "data" + File.separator +  "pValuesForTimeInput_GM19_genus.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits= s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( ! key.startsWith("MDS"))
			{
				Holder h = map.get(key);
				
				if( h == null)
				{
					h = new Holder();
					map.put(key, h);
				}
				
				h.pValue19 = Double.parseDouble(splits[1]);
			}
		}
		
		reader.close();
	}
	
	private static void add15(HashMap<String, Holder> map ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir()
				+ File.separator +  "data" + File.separator + "pValuesForTaxa_genus.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits= s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( ! key.startsWith("MDS"))
			{
				if( map.containsKey(key))
					throw new Exception("No");
				
				Holder h = new Holder();
				map.put(key, h);
				h.pValue15 = Double.parseDouble(splits[1]);
			}
			
		}
		
		reader.close();
	}
}
