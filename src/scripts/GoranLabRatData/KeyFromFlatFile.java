package scripts.GoranLabRatData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class KeyFromFlatFile
{
	public static HashMap<String, String> getKeyMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getGoranLabData() + File.separator + "keyAsLines.txt"
			)));
		
		for(String key = reader.readLine(); key != null; key= reader.readLine())
		{
			if( map.containsKey(key))
				throw new Exception("No");
			
			String group = reader.readLine();
			
			map.put(key, group);
			
			for(int x=0; x < 3; x++)
				if( reader.readLine() == null)
					throw new Exception("No");
			
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map =getKeyMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
