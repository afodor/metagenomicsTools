package scripts.KylieAge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{
	//Monkey #	Old/Young	Location	Old Shared Community History	Cohabitation/Neighbour

	private final String monkey;
	private final String oldYoung;
	private final String location;
	private final String oldShare;
	private final String cohabitationNeighbor;
	
	public String getMonkey()
	{
		return monkey;
	}
	
	public String getOldYoung()
	{
		return oldYoung;
	}

	public String getLocation()
	{
		return location;
	}

	public String getOldShare()
	{
		return oldShare;
	}

	public String getCohabitationNeighbor()
	{
		return cohabitationNeighbor;
	}

	private MetadataParser(String s) throws Exception
	{
		String[] splits = s.split("\t");
		this.monkey = splits[0];
		this.oldYoung = splits[1];
		this.location = splits[2];
		this.oldShare = splits[3];
		
		if( splits.length > 5)
			throw new Exception("No");
		
		this.cohabitationNeighbor = splits.length ==5 ? splits[4] : "NA";
	}
	
	public static HashMap<String, MetadataParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParser> map = new HashMap<String, MetadataParser>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getKylieAgeDir() + File.separator + 
				"Fodor_Age_Housing_MonkeysFeb2015.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			MetadataParser mp = new MetadataParser(s);
			
			if( map.containsKey(mp.monkey))
				throw new Exception("No");
			
			map.put(mp.monkey, mp);
		}
		
		return map;
	}
	
	public static void main(String[] args)
	{
		
	}
}
