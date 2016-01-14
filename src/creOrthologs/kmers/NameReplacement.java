package creOrthologs.kmers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import creOrthologs.AddMetadata;
import utils.ConfigReader;

public class NameReplacement
{
	public static void main(String[] args) throws Exception
	{
		// brute force and highly inefficient
		HashMap<String, String> map = getNameMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredDistanceMatrices" + File.separator + "outtree")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredDistanceMatrices" + File.separator + "outreeDecoded.txt")));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			for(String s2 : map.keySet())
				s = s.replaceAll(s2, map.get(s2));
			
			writer.write(s + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	private static HashMap<String, String> getNameMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		HashMap<String, String> catMap = AddMetadata.getBroadCategoryMap();
		
		for(String s : catMap.keySet())
			System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"gatheredDistanceMatrices" + File.separator + 
				"allKey.txt")));
		
		for(String s = reader.readLine(); s != null;s= reader.readLine())
		{
			String[] splits = s.split(" ");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			if( map.containsValue(splits[1]))
					throw new Exception("No");
			
			map.put(splits[0], splits[1] +"_" + catMap.get(splits[1]));
		}
		
		reader.close();
		
		return map;
	}
}
