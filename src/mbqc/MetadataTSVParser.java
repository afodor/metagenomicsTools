package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;
import utils.TabReader;

public class MetadataTSVParser
{
	private Integer sampleID =null;
	private String wetLabId;
	private String blindedID;
	private String bioinformaticsID;
	private String sampleType;
	
	public String getSampleType()
	{
		return sampleType;
	}

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

	
	public static HashMap<String, List<MetadataTSVParser>> collapseByBioinformaticsID() throws Exception
	{
		HashMap<String, List<MetadataTSVParser>> map = new HashMap<String, List<MetadataTSVParser>>();
		
		HashMap<String, MetadataTSVParser> oldMap = getMap();
		 
		for(String s : oldMap.keySet())
		{
			MetadataTSVParser mParser = oldMap.get(s);
			
			List<MetadataTSVParser> innerList = map.get(mParser.getBioinformaticsID());
			
			if( innerList == null)
			{
				innerList = new ArrayList<MetadataTSVParser>();
				map.put(mParser.getBioinformaticsID(), innerList);
			}
			
			innerList.add(mParser);
			
			String anID = mParser.getBioinformaticsID();
			
			// allow these to be accessed stripping off leading zeros...
			while(anID.startsWith("0"))
			{
				anID = anID.substring(1);
				
				innerList = map.get(anID);
				
				if( innerList == null)
				{
					innerList = new ArrayList<MetadataTSVParser>();
					map.put(anID, innerList);
				}
				
				innerList.add(mParser);
			}
		}
		
		return map;
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
				tsv.sampleType = tReader.nextToken().trim().replaceAll("\"", "");
				
				if( sampleIDString.length() > 0 )
				{
					tsv.sampleID = Integer.parseInt(sampleIDString);
				}
				else
				{
					tsv.sampleID = -1;
				}
				
				String key = tsv.blindedID + "." + tsv.bioinformaticsID;
					
				if( map.containsKey(key))
					throw new Exception("Duplicate key");
					
				map.put(key, tsv);
				
				String aBioID = tsv.bioinformaticsID;
				
				// sometimes ids like conecalo62.0238403120 lose their leading 0 in downstream analysis...
				// here we allow those ids to be founds without their leading zeros.
				while(aBioID.startsWith("0"))
				{
					aBioID = aBioID.substring(1);
					
					key = tsv.blindedID + "." + aBioID;
					
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
		System.out.println(map.get("conecalo62.506170089").getSampleID());
		
		/*
		for(String s: map.keySet())
			if(s.startsWith("conecalo62"))
				System.out.println(s);
				*/
	}
}


