package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class MetadataTSVParser
{
	private Integer sampleID =null;
	private String wetLabId;
	private String blindedID;
	private String bioinformaticsID;
	

	public Integer getSampleID()
	{
		return sampleID;
	}

	public String getWetLabId()
	{
		return wetLabId;
	}

	public String getBlindedID()
	{
		return blindedID;
	}

	public String getBioinformaticsID()
	{
		return bioinformaticsID;
	}

	public static HashMap<String, MetadataTSVParser> getMap() throws Exception
	{
		HashMap<String, MetadataTSVParser> map = new LinkedHashMap<String, MetadataTSVParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMbqcDir() + 
				File.separator + "metadata" + File.separator + "metadata.tsv")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			MetadataTSVParser tsv = new MetadataTSVParser();
			tsv.wetLabId = tReader.nextToken().replaceAll("\"", "");
			
			tsv.blindedID = tReader.nextToken().replaceAll("\"", "");
			
			tsv.bioinformaticsID = tReader.nextToken().trim().replaceAll("\"", "");
			
			if( tsv.bioinformaticsID.length() > 0 )
			{
				String sampleIDString= tReader.nextToken().trim().replaceAll("\"", "");
				
				if( sampleIDString.length() > 0 )
				{
					tsv.sampleID = Integer.parseInt(sampleIDString);
					String key = tsv.blindedID + "." + tsv.bioinformaticsID;
					
					if( map.containsKey(key))
						throw new Exception("Duplicate key");
					
					map.put(key, tsv);
				}
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataTSVParser> map= getMap();
		System.out.println("got map with " + map.size());
		
		int stop =0;
		
		for(String s: map.keySet())
			//if( ++stop < 20)
				System.out.println(s);
	}
}
