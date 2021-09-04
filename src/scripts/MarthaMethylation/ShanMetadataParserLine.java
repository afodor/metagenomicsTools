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
	
	public static final String METADATA_FILE_PATH = "C:\\MarthaMethylation\\shanCountTables\\metadata_all.txt";
	
	private ShanMetadataParserLine(String s)
	{
		String[] splits = s.split("\t");
		
		this.studyID = splits[0];
		this.sampleID = splits[2];
		this.sampleType = splits[3];
		this.visit = splits[6];
	}
	
	public static HashMap<String, ShanMetadataParserLine> getMetaMap() throws Exception
	{
		HashMap<String, ShanMetadataParserLine> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE_PATH));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			
		}
		
		return map;
	}
	
	
}
