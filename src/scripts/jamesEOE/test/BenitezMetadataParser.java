package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class BenitezMetadataParser
{
	private final String studyGroup; // control or EOE
	private final String status; // control active or inactive
	
	public String getStudyGroup()
	{
		return studyGroup;
	}

	public String getStatus()
	{
		return status;
	}

	private BenitezMetadataParser(String s)
	{
		String[] splits = s.split("\t");
		this.studyGroup  = splits[11];
		this.status = splits[12];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String,BenitezMetadataParser> map = getBenitezCaseControlMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).studyGroup + " " + map.get(s).status);
	}
	
	private static HashMap<String,BenitezMetadataParser> getBenitezCaseControlMap( ) throws Exception
	{
		HashMap<String,BenitezMetadataParser> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getJamesEoeDirectory() + File.separator + 
				"test" + File.separator + "sample_attributes.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			BenitezMetadataParser bmp = new BenitezMetadataParser(s);
			
			map.put(splits[0], bmp);
		}
		
		return map;

	}

}
