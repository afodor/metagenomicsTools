package scripts.farnazTessa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MetadataParser
{
	private int day;
	
	public int getDate()
	{
		return day;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> map = getMetaMap();
		
		for(String s : map.keySet())
		{
			System.out.println( s + " " + map.get(s).day); 
		}
			
	}
	
	private MetadataParser(String s) throws Exception
	{
		String[] splits =s.split("\t");
		
		this.day = Integer.parseInt(splits[0]);
	}
	
	public static HashMap<String, MetadataParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParser> map = new LinkedHashMap<String, MetadataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\HCT_Probiotic2020-main\\input\\prebio_meta_FF_060920.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( ! splits[4].equals("NA"))
				map.put(splits[4], new MetadataParser(s));
		}
		
		reader.close();
		return map;
	}
}
