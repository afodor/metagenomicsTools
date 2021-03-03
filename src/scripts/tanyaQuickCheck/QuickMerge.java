package scripts.tanyaQuickCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class QuickMerge
{
	public static String[] LEVELS = {"phylum", "class", "order", "family", "species"};
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		/*
		for(String level : LEVELS)
		{
			System.out.println(level);
			OtuWrapper wrapper = new OtuWrapper("C:\\tanyaQuickRep\\tanya_kraken_"+ level +  ".txt");
			
			wrapper.writeLoggedDataWithTaxaAsColumns( new File( "C:\\tanyaQuickRep\\tanya_kraken_"+ level +  "logNorm.txt"));
		}
		*/
	}
	
	
	private static HashMap<String, String> getMetaMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\tanyaQuickRep\\alderete_k99_metadata_30dec18.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tr = new TabReader(s);
			
			String value= tr.getNext();
			tr.getNext();
			
			String key = tr.getNext();
			
			String[] parts  = value.split("\\.");
			
			value = parts[0];
			
			map.put(key, value);
		}
		
		reader.close();
		return map;
	}
}
