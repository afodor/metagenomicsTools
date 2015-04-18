package scripts.JobinApril2015.cjej;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private final String rgSampleID;
	private final int groupID;
	private final int cageID;
	private final String mouse;
	private final String timepoint;
	private final String infected;
	
	private MetadataFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.rgSampleID = splits[0];
		this.groupID = Integer.parseInt(splits[6]);
		this.cageID = Integer.parseInt(splits[7]);
		this.mouse = splits[8];
		this.timepoint = splits[9];
		this.infected = splits[10];
		
	}
	
	public static HashMap<String, MetadataFileLine> getMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map= new HashMap<String, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + "cjej_map.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			MetadataFileLine mfl = new MetadataFileLine(s);
			
			if( map.containsKey(mfl.rgSampleID) )
				throw new Exception("No");
			
			map.put(mfl.rgSampleID, mfl);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map =getMap();
		
		for(String s : map.keySet())
			System.out.println(s);
	}
}
