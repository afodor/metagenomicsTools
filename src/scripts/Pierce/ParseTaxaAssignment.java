package scripts.Pierce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class ParseTaxaAssignment
{
	/*
	 * x =0 == phylum
	 * x= 4  == genus
	 */
	HashMap<String, String> getMapForLevel(int level) throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader( 
			ConfigReader.getPierce2019Dir() + File.pathSeparator + 
					"taxa_table16s.txt"));
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 7)
				throw new Exception();
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("Duplciate " + key);
			
			String taxa = splits[level+2];
			
			map.put(key, taxa);
		}
		
		return map;
	}
}
