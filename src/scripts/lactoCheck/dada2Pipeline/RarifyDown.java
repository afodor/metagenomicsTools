package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class RarifyDown
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> map = getDepthMap();
		
		int rareNumber = Integer.MAX_VALUE;
		
		for(Integer i : map.values())
			if( i > 50)
				rareNumber =Math.min(rareNumber, i);
		
		System.out.println(rareNumber);
	}
	
	private static HashMap<String, Integer> getDepthMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"readSummary.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Integer.parseInt(splits[1]));
		}
		
		return map;
	}
}
