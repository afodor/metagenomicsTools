package scripts.dolphinCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

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
			s=s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			//System.out.println(s);
			if( ! s.startsWith("Water"))
			{
				map.put(splits[0], splits[6] );
			}
			else
			{
				map.put(splits[0], "water");
			}
		}
		
		reader.close();
		return map;
		
	}
	
	//outer key is body site; inner key is taxa;
	private static HashMap<String, HashMap<String,List<Double>>> getBodySiteToTaxa(String filter) throws Exception
	{
		HashMap<String, String> siteMap = getBodySiteMap();
		List<Long> rowSums = getRowSums(filter);
		
		HashMap<String, HashMap<String,List<Double>>> map = new HashMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Thomas_Dolphin\\hierarch_merge.txt")));
		
		List<String> bodySites= new ArrayList<>();
		
		String s = reader.readLine();
		String[] splits = s.split("\t");
		
		for( int x= 4; x < splits.length; x++)
		{
			String key = new StringTokenizer(splits[x], "_").nextToken();
			String bodySite = siteMap.get(key);
			
			if( bodySite == null)
			{
				System.out.println("Could not find " + key);
			}
			else
			{
				System.out.println("Found " + key + " " + bodySite);
			}
				
			bodySites.add(bodySite);
		}
		
		
		
		reader.close();
		return map;
	}
	
	private static List<Long> getRowSums(String filter) throws Exception
	{
		List<Long> list= new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Thomas_Dolphin\\hierarch_merge.txt")));
		
		String[] tops = reader.readLine().split("\t");
		
		for( int x=4;  x < tops.length; x++)
			list.add(0l);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != tops.length)
				throw new Exception("No");
			
			if( splits[3].equals(filter))
			{
				for( int x=4; x < splits.length; x++)
					list.set(x-4, list.get(x-4)+ Long.parseLong(splits[x]));
			}
		}
		
		reader.close();
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,List<Double>>>  bigMap = getBodySiteToTaxa("genus");
	}
}
