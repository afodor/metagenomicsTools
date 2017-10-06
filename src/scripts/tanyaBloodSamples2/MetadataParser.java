package scripts.tanyaBloodSamples2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import utils.ConfigReader;

public class MetadataParser
{
	
	private static HashSet<String> getIncluded()
	{
		HashSet<String> set = new LinkedHashSet<String>();
		
		set.add("ID");
		set.add("Hisp" );
		set.add("RACE_Other");
		set.add("MALE");
		set.add("CAGE");
		set.add("STUDY");
		set.add("Bodyfatper");
		set.add("MRI_HFF");
		set.add("MRI_SAAT");
		set.add("MRI_VAT");
			
		return set;
		
	}
	
	private static int getIndex(String[] splits, String val) throws Exception
	{
		for( int x=0; x < splits.length; x++)
			if( splits[x].equals(val))
				return x;
		
		throw new Exception("Could not find " + val);
	}
	
	public static HashMap<String, HashMap<String,String>> getMetaMap() throws Exception
	{
		BufferedReader reader=  new BufferedReader(new FileReader(new File(
				ConfigReader.getTanyaBloodDir2() + File.separator + 
					"metaairpro_metadata_26apr17.txt")));
		
		String firstLine =reader.readLine();
		String[] topSplits = firstLine.split("\\,");
		
		HashSet<String> included = getIncluded();
		
		HashMap<String, HashMap<String,String>> map = new HashMap<>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\\,");
			
			String tubeId = splits[1];
			
			if( map.containsKey(tubeId))
				throw new Exception("Duplicate " + tubeId);
			
			HashMap<String, String> innerMap = new LinkedHashMap<>();
			
			map.put(tubeId, innerMap);
			
			for(String s2 : included)
			{
				int index = getIndex(topSplits, s2);
				
				innerMap.put(s2,splits[index]);
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,String>> map = 
				getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
