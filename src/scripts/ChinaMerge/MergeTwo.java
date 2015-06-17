package scripts.ChinaMerge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import utils.ConfigReader;

public class MergeTwo
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, String> map = getBPointForwardRead();
		HashMap<Integer, List<Integer>> globalMap = getMapFromGlobalFile();
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKatieDir() + File.separator + 
					"mergedOut.txt")));
		
		writer.write(getFirstLine());
		
		List<String> sampleNames = getSampleNames();
		
		for(String s : sampleNames)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(Integer i: globalMap.keySet())
		{
			String fileLine = map.get(i);
			
			if(fileLine!= null)
			{
				writer.write(fileLine);
				
				List<Integer> innerList = globalMap.get(i);
				
				for(Integer i2 : innerList)
					writer.write("\t" + i2);
				
				writer.write("\n");
			}
				
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static List<String> getSampleNames() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"global_library.fna.txt"
				+ "")));
		
		reader.readLine();
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			list.add(s.split("\t")[0]);
		}
		
		return list;
	}
	
	private static HashMap<Integer, List<Integer>> getMapFromGlobalFile() throws Exception
	{
		HashMap<Integer, List<Integer>>  map = new HashMap<Integer, List<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"global_library.fna.txt"
				+ "")));
		
		String[] firstSplits = reader.readLine().split("\t");
		
		List<Integer> keys = new ArrayList<Integer>();
		
		for( int x=1; x < firstSplits.length; x++)
			keys.add(Integer.parseInt(firstSplits[x].replace("B", "").trim()));
		
		for( Integer i : keys )
			map.put(i, new ArrayList<Integer>());
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int index =1;
			
			for( Integer i : keys)
			{
				List<Integer> innerList = map.get(i);
				innerList.add( Integer.parseInt(splits[index].trim()));
				index++;
			}
		}
		
		return map;
	}
	
	private static String getFirstLine() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"chinaur.txt")));
		
		String a = reader.readLine();
		
		
		reader.close();
		return a;
	}
	
	private static HashMap<Integer, String> getBPointForwardRead() throws Exception
	{
		 HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"chinaur.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[4].equals("second_B") && Integer.parseInt(splits[1])==1)
			{
				Integer key = Integer.parseInt(splits[2]);
				
				if( map.containsKey(key))
					throw new Exception("No");
				
				map.put(key,s);
			}
		}
		
		return map;
	}
}
