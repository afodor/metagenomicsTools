package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParserFileLine 
{
	private final String sampleID;
	private final String treatmentGroup;
	private final String timePoint;
	private final String tumorVolume;
	private final String tumorWeight;
	private final String desciption;
	private final Integer cageNumber;
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getTreatmentGroup()
	{
		return treatmentGroup;
	}

	public String getTimePoint()
	{
		return timePoint;
	}

	public String getTumorVolume()
	{
		return tumorVolume;
	}

	public String getTumorWeight()
	{
		return tumorWeight;
	}

	public String getDesciption()
	{
		return desciption;
	}

	public int getCageNumber()
	{
		return cageNumber;
	}

	private MetadataParserFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.treatmentGroup = splits[4];
		this.timePoint= splits[5];
		this.tumorVolume = splits[6];
		this.tumorWeight = splits[7];
		this.desciption = splits[8];
		
		if( splits.length == 10)
			this.cageNumber = Integer.parseInt(splits[9]);
		else 
			this.cageNumber = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = getMetaMap();
	}
	
	private static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + 
				"IER Project Metadata.txt")));
		
		reader.readLine();
		
		HashMap<String, MetadataParserFileLine> map = new HashMap<>();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			MetadataParserFileLine mpfl = new MetadataParserFileLine(s);
			
			if( map.containsKey(mpfl.sampleID))
				throw new Exception("Duplicate " + mpfl.sampleID );
			
			if( ! mpfl.sampleID.equals("BLANK"))
				map.put(mpfl.sampleID, mpfl);
		}
		
		return map;
	}
}

