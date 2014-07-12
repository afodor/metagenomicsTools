package bigDataScalingFactors.riskRefOTUs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataParserFileLine
{
	private final String sampleID;
	private final String diagnosis;
	private final String sampleLocation;
	
	public String getSampleLocation()
	{
		return sampleLocation;
	}
	
	public String getSampleID()
	{
		return sampleID;
	}
	
	public String getDiagnosis()
	{
		return diagnosis;
	}
	
	@Override
	public String toString()
	{
		return sampleID + " " + diagnosis + " " + sampleLocation;
	}
	
	public static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = new LinkedHashMap<String, MetadataParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"risk" + File.separator + "dirk" + File.separator + "RISK_map.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataParserFileLine mpfl = new MetadataParserFileLine(s);
			
			if( map.containsKey(mpfl.sampleID))
				throw new Exception("Duplicate");
			
			map.put(mpfl.sampleID, mpfl);
		}
		
		reader.close();
		
		return map;
	}
	
	private MetadataParserFileLine(String s)
	{
		String[] splits = s.split("\t");
		this.sampleID = splits[0];
		this.diagnosis = splits[3];
		this.sampleLocation = splits[7];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = getMetaMap();
		for(String s : map.keySet())
			System.out.println( s +  " " + map.get(s));
	}
}
