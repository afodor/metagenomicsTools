package scripts.compareEngel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataFileParser
{
	
	
	/*
	private final float eupct;  // european ancestry
	private final float yripct;  // african ancestry
	private final int c_mrace; // maternal race
	private final int c_case2; // preterm/term
	*/
	
	public static void main(String[] args) throws Exception
	{
		getMetaMap();
	}
	
	private final static String FILE_NAME = "PIN0108_MicrobCov_NoSNP_20190607.txt";
	
	public static HashMap<String, MetadataFileParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileParser> map = new HashMap<>();
		
		HashMap<String,Integer> topLineMap= getColumnIndexMap();
		
		for(String s : topLineMap.keySet())
			System.out.println(s + " " + topLineMap.get(s));
		
		return map;
	}
	
	private static HashMap<String, Integer> getColumnIndexMap()
		throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getEngelCheckDir() + File.separator + 
						FILE_NAME)));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=0; x< topSplits.length; x++)
		{
			if( map.containsKey(topSplits[x]))
				throw new Exception("Duplicate " + topSplits[x]);
			
			map.put(topSplits[x], x);
		}
		
		reader.close();
		return map;
	}
}
