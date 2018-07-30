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
	private final String sraSampleRun;
	
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
		
		String lastCol = null;
		
		if( splits.length > 31)
		
		lastCol = splits[splits.length-1];
		
		this.sraSampleRun = lastCol;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String,BenitezMetadataParser> bMap = getBenitezCaseControlMap();
		
		for(String s : bMap.keySet())
			System.out.println(s + " " + bMap.get(s).studyGroup + " " + bMap.get(s).status
					+ " " + bMap.get(s).sraSampleRun);
		
		HashMap<String, Integer> eMap = EvanMetadataParser.getEvanCaseControlMap();
		
		for(String s : eMap.keySet())
			if( bMap.containsKey(s))
				throw new Exception("No");
		
		System.out.println("No duplciates");
		
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
