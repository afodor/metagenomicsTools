package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.TabReader;

public class MetadataParser
{
	private final String sampleID;
	private final String extentOfStress;
	private final String dateOfExperiment;
	private final String experiment;
	private final String sex;
	private final String cageID;
	private final String diet;
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getExtentOfStress()
	{
		return extentOfStress;
	}

	public String getDateOfExperiment()
	{
		return dateOfExperiment;
	}

	public String getExperiment()
	{
		return experiment;
	}

	public String getSex()
	{
		return sex;
	}

	public String getCageID()
	{
		return cageID;
	}

	public String getDiet()
	{
		return diet;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> map = getMetaMap();
		System.out.println(map.size());
	}

	public static HashMap<String, MetadataParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParser> map = new HashMap<String, MetadataParser>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\input\\MCBT_RevisedMetadata_5-9-2020_01_FF.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String sampleID = TabReader.getTokenAtIndex(s, 0).trim();
			
			if( sampleID.length() > 0  )
			{
				MetadataParser mp = new MetadataParser(s);
				
				if( ! mp.sampleID.equals(sampleID))
					throw new Exception("Parsing error");
				
				if( map.containsKey(sampleID))
					throw new Exception("Duplicate " +sampleID);
				
				map.put(sampleID, mp);
			}
		}
		
		reader.close();
		
		return map;
	}
	
	private MetadataParser(String s ) throws Exception
	{
		this.sampleID  = TabReader.getTokenAtIndex(s, 0);
		this.extentOfStress = TabReader.getTokenAtIndex(s, 2);
		this.dateOfExperiment = TabReader.getTokenAtIndex(s, 4);
		this.experiment = TabReader.getTokenAtIndex(s, 5);
		this.sex = TabReader.getTokenAtIndex(s, 6);
		this.cageID = TabReader.getTokenAtIndex(s, 10);
		this.diet = TabReader.getTokenAtIndex(s, 11);
	}
}
