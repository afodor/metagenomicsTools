package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParserFileLine 
{
	/*
	private final String sampleID;
	private final String treatmentGroup;
	private final String timePoint;
	private final String tumorVolume;
	private final String tumorWeight;
	private final String desciption;
	private final int cageNumber;
	*/

	private static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + 
				"IER Project Metadata.txt")));
		
		reader.readLine();
		
		HashMap<String, MetadataParserFileLine> map = new HashMap<>();
		
		return map;
	}
}

