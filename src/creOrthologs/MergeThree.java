package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeThree
{
	public static class Holder
	{
		public Double carVsSuc = null;
		public Double carVsRes = null;
		public Double resVsSuc = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		writeResults(getHolderMap());
	}
	
	private static void writeResults( HashMap<String, Holder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				ConfigReader.getCREOrthologsDir() + File.separator + 
						"pValuesMerged.txt"));
		
		writer.write("line\tpValueCarVsSuc\tpValueCarVsRes\tpValueResVsSuc\n");
		
		for( String s : map.keySet())
		{
			Holder h = map.get(s);
			
			writer.write(s + "\t" + h.carVsSuc + "\t" + h.carVsRes + "\t" + h.resVsSuc + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static HashMap<String, Holder> getHolderMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesCarVsRes.txt")));
		
		HashMap<String, Holder> map = new HashMap<String, Holder>();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Holder h = new Holder();
			map.put(splits[0], h);
			h.carVsRes = Double.parseDouble(splits[1].replaceAll("\"", ""));
		}
		
		reader.close();
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesCarVsSuc.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Holder h = map.get(splits[0]);
			
			if( h == null)
			{
				h = new Holder();
				map.put(splits[0], h);
				
			}
			h.carVsSuc = Double.parseDouble(splits[1].replaceAll("\"", ""));
		}
		
		reader.close();
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesResVsSuc.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Holder h = map.get(splits[0]);
			
			if( h == null)
			{
				h = new Holder();
				map.put(splits[0], h);
				
			}
			h.resVsSuc= Double.parseDouble(splits[1].replaceAll("\"", ""));
		}
		
		reader.close();
		
		return map;
	}
}
