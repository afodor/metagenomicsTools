package scripts.compareEngel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class CompareMetaphlan
{
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, HashMap<String,Float>>  map = parseMetaphlanSummaryAtLevel(new File("C:\\EngelCheck\\allSamples.metaphlan2.bve.profile.txt"), "g");
		
		for(String s : map.keySet())
		{
			System.out.println(s);
			
			for(String s2 : map.get(s).keySet())
				System.out.println( "\t" + s2 + "\t" + map.get(s).get(s2));
		}
	}
	
	// outer key is sample id;
	// inner key is taxa name
	private static HashMap<String, HashMap<String,Float>> parseMetaphlanSummaryAtLevel(File f, String level) throws Exception
	{
		HashMap<String, HashMap<String,Float>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			//System.out.println(splits[0] + "@" + splits[1] + " " + Float.parseFloat(splits[2]));
			
			String lastMatch = getLastMatchOrNull(splits[1], level);
			
			if( lastMatch != null)
			{
				HashMap<String, Float> innerMap = map.get(splits[0]);
				
				if( innerMap==null)
				{	
					innerMap = new HashMap<>();
					map.put(splits[0], innerMap);
				}
					
					
				if( innerMap.containsKey(lastMatch))
					throw new Exception("Duplicate " + lastMatch);
					
				innerMap.put(lastMatch, Float.parseFloat(splits[2]));
			}
		}
		
		return map;
	}
	
	private static String getLastMatchOrNull(String s, String level)
	{
		s = s.substring(s.lastIndexOf("|") +1, s.length());
		
		String startString = level + "__";
		
		if( ! s.startsWith(startString))
			return null;
		
		return s.replace(startString, "");
	}
}
