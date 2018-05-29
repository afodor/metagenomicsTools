package scripts.laura.sleeveGastroProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataParserFileLine 
{
	private final String sampleID;
	private final String treatmentGroup;
	private final String timePoint;
	private final String tumorVolume;
	private final String tumorWeight;
	private final String description;
	private final int cageNumber;
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map= getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).getCageNumber());
	}
	
	private MetadataParserFileLine(String s )
	{
		String[] splits =s.split("\t");
		
		this.sampleID = splits[0];
		this.treatmentGroup = splits[4];
		this.timePoint = splits[5];
		this.tumorVolume = splits[6];
		this.tumorWeight = splits[7];
		this.description= splits[8];
		this.cageNumber =  Integer.parseInt(splits[9]);
	}
	
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
		return description;
	}

	public int getCageNumber()
	{
		return cageNumber;
	}

	private static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLauraDir() + File.separator + "SleeveGastroProject" + File.separator + 
				"metadata.txt")));
		
		reader.readLine();
		
		HashMap<String, MetadataParserFileLine> map = new LinkedHashMap<>();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			MetadataParserFileLine mpfl = new MetadataParserFileLine(s);
			
			String key = mpfl.getSampleID();
			
			if( map.containsKey(key))
				throw new Exception("NO");
			
			map.put(key,mpfl);
		}
		
		return map;
		
	}
}

