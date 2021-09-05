package scripts.MarthaMethylation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class ShanMetadataParserLine
{
	private final String studyID;
	private final String sampleID;
	private final String sampleType;
	private final String visit;
	
	public String getStudyID()
	{
		return studyID;
	}

	public String getSampleID()
	{
		return sampleID;
	}

	public String getSampleType()
	{
		return sampleType;
	}

	public String getVisit()
	{
		return visit;
	}

	public static String getMetadataFilePath()
	{
		return METADATA_FILE_PATH;
	}

	public static final String METADATA_FILE_PATH = "C:\\MarthaMethylation\\shanCountTables\\metadata_all.txt";
	
	private ShanMetadataParserLine(String s)
	{
		String[] splits = s.split("\t");
		
		this.studyID = splits[0];
		this.sampleID = splits[2];
		this.sampleType = splits[3];
		this.visit = splits[6];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, ShanMetadataParserLine> metaMap = getMetaMap();
		
		for(String  s: metaMap.keySet())
			System.out.println(s + " "+ metaMap.get(s).studyID);
	}
	
	public static HashMap<String, ShanMetadataParserLine> getMetaMap() throws Exception
	{
		HashMap<String, ShanMetadataParserLine> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE_PATH));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String sampleId = splits[2];
			
			if( map.containsKey(sampleId))
				throw new Exception("Duplicate " + sampleId);
			
			map.put(sampleId, new ShanMetadataParserLine(s));
			
		}
		
		return map;
	}
	
	
}
