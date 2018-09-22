package scripts.topeVicki;

import java.io.File;
import java.io.FileReader;

import java.io.BufferedReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileParser
{
	//SampleID	Race	BMI	Tumor Histology	Tumor Grade
	private final String sampleID;
	private final String race;
	private final String bmi;
	private final String tumorHistology;
	private final int tumorGrade;

	private MetadataFileParser(String s)
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.race = splits[1];
		this.bmi = splits[2];
		this.tumorHistology = splits[3];
		this.tumorGrade = Integer.parseInt(splits[4]);
	}
	
	public static HashMap<String, MetadataFileParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileParser> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getTopeVickiDir()+
				File.separator + "meta.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataFileParser mfp = new MetadataFileParser(s);
			
			if( map.containsKey(mfp.sampleID))
				throw new Exception("Parsing error");
			
			map.put(mfp.sampleID, mfp);
		}
		
		reader.close();
		
		return map;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileParser> metaMap =MetadataFileParser.getMetaMap();
	}
}
