package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class AddTaxaNames
{
	private static HashMap<String, String> getTaxaStrings() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
				+ File.separator + 
					"LyteSharon_r01_cr.txt")));
		
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key ="X" + splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			
			map.put(key, splits[splits.length-1]);
		}
		
		reader.close();
	
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getTaxaStrings();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
