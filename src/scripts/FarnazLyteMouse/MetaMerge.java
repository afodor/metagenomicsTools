package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class MetaMerge
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getKeyMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		System.out.println(map.size());
		
		System.out.println(map.get("1@3/24/17@Fecal"));
	}
	
	public static void writeMergedMetaTables() throws Exception
	{
		
	}
	
	/*
	 * Key is animalId@dateString@SampleSource
	 */
	public static HashMap<String, String> getKeyMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
			"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\input\\" + 
					"MCBT_RevisedMetadata_5-9-2020_01_FF.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader =new TabReader(s);
			
			String firstToken = tReader.nextToken().trim();
			
			if( firstToken.length() > 0 )
			{
				String key = tReader.getTokenAtIndex(s, 13) + "@" + tReader.getTokenAtIndex(s, 4) +"@"  + 
								tReader.getTokenAtIndex(s, 1);
				
				if( map.containsKey(key))
					throw new Exception("Duplicate " + key);
				
				map.put(key, firstToken);
			}
		}
		
		return map;
	}
}
