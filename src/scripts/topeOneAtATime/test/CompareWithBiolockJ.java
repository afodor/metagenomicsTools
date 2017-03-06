package scripts.topeOneAtATime.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class CompareWithBiolockJ
{
	private static class Holder
	{
		private Double fromBioLockJ = null;
		private Double fromAF = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> firstMap =getFirstMap();
		System.out.println(firstMap.size());
		addToMap(firstMap);
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTopeOneAtATimeDir()  + File.separator + 
					"compareBiolockJgenus.txt")));
		
		writer.write("taxa\tbiolockJP\tafP\n");
		for(String s : firstMap.keySet())
		{
			Holder h = firstMap.get(s);
			
			writer.write(s  + "\t" + h.fromBioLockJ + "\t" + h.fromAF + "\n" );
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addToMap(HashMap<String,Holder> map) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + "merged" +
					File.separator + "pValuesFor_genus_read1_.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			Holder h = map.get(key);
			
			if( h == null)
			{
				h =new Holder();
				map.put(key,h);
			}
			
			h.fromAF = Double.parseDouble(splits[1]);
		}
	}
	
	private static HashMap<String,Holder> getFirstMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"FromMichael" + File.separator + 
				"meta_pValuesFor_genus.txt")));
		
		HashMap<String, Holder> map = new HashMap<String,Holder>();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String key = splits[0];
			
			if(map.containsKey(key))
				throw new Exception("No");
			
			Holder h = new Holder();
			h.fromBioLockJ = Double.parseDouble(splits[1]);
			map.put(key, h);
		}
		
		reader.close();
		

		return map;
	}
	
}
