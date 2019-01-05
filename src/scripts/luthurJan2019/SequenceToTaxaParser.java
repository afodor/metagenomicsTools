package scripts.luthurJan2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class SequenceToTaxaParser
{
	private final String key;
	private final String kingdom;
	private final String phylum;
	private final String aClass;
	private final String order;
	private final String family;
	private final String genus;
	private final String species;
	
	private SequenceToTaxaParser(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.key = splits[0];
		this.kingdom = splits[1];
		this.phylum = splits[2];
		this.aClass = splits[3];
		this.order = splits[4];
		this.family = splits[5];
		this.genus = splits[6];
		
		this.species = splits[7];
		
	}
	
	public static HashMap<String, SequenceToTaxaParser> getMap() throws Exception
	{
		HashMap<String, SequenceToTaxaParser> map = new LinkedHashMap<>();
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir() + 
			File.separator + "data" + File.separator + "luther.taxa.species.SILVA.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			SequenceToTaxaParser stp = new SequenceToTaxaParser(s);
			
			if( map.containsKey(stp.key))
				throw new Exception("Duplicate");
			
			map.put(stp.key,stp);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, SequenceToTaxaParser> map =getMap();
		
		for(String s: map.keySet())
			System.out.println(s + " " + map.get(s).genus);
	}
}
