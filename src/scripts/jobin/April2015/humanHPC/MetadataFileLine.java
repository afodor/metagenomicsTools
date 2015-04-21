package scripts.jobin.April2015.humanHPC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private final String rgSampleID;
	private final String diagnostic;
	
	public String getDiagnostic()
	{
		return diagnostic;
	}
	
	public String getRgSampleID()
	{
		return rgSampleID;
	}
	private MetadataFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.rgSampleID = splits[0];
		this.diagnostic = splits[7];
		
	}
	
	public static HashMap<String, MetadataFileLine> getMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map= new HashMap<String, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + "hpc_map.txt"	)));
		
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