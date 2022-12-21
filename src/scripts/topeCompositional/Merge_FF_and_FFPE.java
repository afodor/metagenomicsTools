package scripts.topeCompositional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class Merge_FF_and_FFPE
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> ffMetaMap = getIntegerToKeyMap();
		
		for(String s : ffMetaMap.keySet())
			System.out.println(s + " " + ffMetaMap.get(s));
		
		 HashMap<String, Integer> countMap = getFFCounts();
	}
	
	//"Key is Sample_x_ff@taxa ; value is count"
	private static HashMap<String, Integer> getFFCounts() throws Exception
	{
		HashMap<String, Integer> ffMetaMap = getIntegerToKeyMap();
		
		HashMap<String, Integer> countMap = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\topeComparisonData\\FF_OTU_metaRemoved.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Integer topeIndex = ffMetaMap.get(splits[0]);
			
			if( topeIndex == null)
				throw new Exception("Could not map " + splits[0]);
		}
		
		return countMap;
	}
	
	@SuppressWarnings({  "resource" })
	private static HashMap<String, Integer> getIntegerToKeyMap() throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\topeComparisonData\\FF_Metadata.txt")));
		
		reader.readLine();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("No");
			
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			
			sToken.nextToken(); sToken.nextToken();
			
			int aValue = Integer.parseInt(sToken.nextToken().replace("U", ""));
			
			map.put(splits[1], aValue);
			
			
		}
		
		return map;
	}
}
