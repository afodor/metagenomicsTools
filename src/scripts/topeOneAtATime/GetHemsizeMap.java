package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class GetHemsizeMap
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getHemsizeMap();
		
		for(String s : map.keySet())
			System.out.println(s + " "+ map.get(s));
	}
	
	public static HashMap<String, String> getHemsizeMap() throws Exception
	{	
		HashMap<String, String>  map = new LinkedHashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"Hemerrhoids_bugs.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			String key = tReader.getNext();
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			for( int x=0; x < 7; x++)
				tReader.nextToken();
			
			String val = tReader.nextToken().trim();
			
			if(tReader.hasMore())
				throw new Exception("No");
			
			if( val.length() == 0 )
				val = "none";
			
			map.put(key, val);
		}
		
		return map;
	}
}
