package scripts.kylieAgeManuscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import scripts.KylieAge.AddAge;
import utils.ConfigReader;

public class MetadataParserFileLine
{
	private final String sampleID;
	private final String animalID;
	private final String dateAsFactor;
	private final String diet;
	private final String cage;
	private final String ageCategory;
	private final int days;
	
	public String getSampleID()
	{
		return sampleID;
	}
	
	public int getDays()
	{
		return days;
	}

	public String getAnimalID()
	{
		return animalID;
	}

	public String getDateAsFactor()
	{
		return dateAsFactor;
	}

	public String getDiet()
	{
		return diet;
	}

	public String getCage()
	{
		return cage;
	}

	public String getAgeCategory()
	{
		return ageCategory;
	}

	private MetadataParserFileLine(String s ) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleID= "Sample"  + Integer.parseInt(splits[0]);
		this.animalID = splits[1];
		this.dateAsFactor = splits[2];
		this.days = Integer.parseInt(splits[4]);
		this.diet = splits[5];
		this.cage = splits[6];
		this.ageCategory = splits[8];
	}
	
	public static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = new LinkedHashMap<String, MetadataParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getKylieDropoxDir() + File.separator + 
				"sampleKey.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataParserFileLine meta = new MetadataParserFileLine(s);
			
			if( map.containsKey(meta.sampleID))
				throw new Exception("No");
			
			map.put(meta.sampleID, meta);
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = getMetaMap();
		HashMap<String, String> ageMap = AddAge.getAgeMap();
		
		for(String s : metaMap.keySet())
		{
			MetadataParserFileLine mpfl = metaMap.get(s);
			
			String shortAge = ageMap.get(mpfl.animalID);
			
			if( shortAge == null)
				throw new Exception("No " + mpfl.animalID);
			
			if( ! mpfl.ageCategory.startsWith(shortAge.toLowerCase()))
				throw new Exception("No " + mpfl.ageCategory + " " + shortAge);
			
			System.out.println(mpfl.ageCategory);
			
		}
	}
}
