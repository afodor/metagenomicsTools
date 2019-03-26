package scripts.dolphinCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class CheckAbundances
{
	private static HashMap<String, String> getBodySiteMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			"C:\\Thomas_Dolphin\\dolphinMetadata_withSampleID-CORRECTED.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			//System.out.println(s);
			if( ! s.startsWith("Water"))
			{
				s=s.replaceAll("\"", "");
				String[] splits = s.split("\t");
				map.put(splits[0], splits[6] );
			}
		}
		
		return map;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> bodySiteMap =getBodySiteMap();
		
		for(String s : bodySiteMap.keySet())
			System.out.println(s + " " + bodySiteMap.get(s));
	}
}
