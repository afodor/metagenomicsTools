package bigDataScalingFactors.mouseDonors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataParserFileLine
{
	private final String sample;
	private final String env_package;
	private final String innoculum;
	private final Integer tumorNumber;
	
	@Override
	public String toString()
	{
		return this.sample + " " + this.env_package + " " + this.innoculum + " " + this.tumorNumber;
	}
	
	public String getSample()
	{
		return sample;
	}

	public String getEnv_package()
	{
		return env_package;
	}

	public String getInnoculum()
	{
		return innoculum;
	}

	public Integer getTumorNumber()
	{
		return tumorNumber;
	}

	private MetadataParserFileLine(String s)
	{
		String[] splits = s.split("\t");
		this.sample = splits[0];
		this.env_package = splits[4];
		this.innoculum = splits[6];
		this.tumorNumber = splits[8].equals("NA") ? null :  Integer.parseInt(splits[8]);
	}
	
	public static HashMap<String, MetadataParserFileLine> parseMetadata() throws Exception
	{
		HashMap<String, MetadataParserFileLine> map =
				new LinkedHashMap<String, MetadataParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"MouseDonors" + File.separator + "map.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			MetadataParserFileLine mpfl = new MetadataParserFileLine(s);
			
			if( map.containsKey(mpfl.sample))
				throw new Exception("Duplicate");
			
			map.put(mpfl.sample, mpfl);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = parseMetadata();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
