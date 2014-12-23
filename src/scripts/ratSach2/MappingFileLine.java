package scripts.ratSach2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MappingFileLine
{
	private final String sampleID;
	private final String line;
	private final String ratID;
	private final String tissue;
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getLine()
	{
		return line;
	}

	public String getRatID()
	{
		return ratID;
	}

	public String getTissue()
	{
		return tissue;
	}

	public static HashMap<String, MappingFileLine> getMap() throws Exception
	{
		HashMap<String, MappingFileLine> map =new LinkedHashMap<String, MappingFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getRachSachReanalysisDir() + 
				File.separator + "Dess150_05262014_try02_mapping NKD.txt"));
		
		reader.readLine();
		
		for( String s= reader.readLine() ; s != null && s.trim().length() > 0;  s= reader.readLine())
		{
			MappingFileLine mfl = new MappingFileLine(s);
			
			if(map.containsKey(mfl.sampleID))
				throw new Exception("No");
			
			map.put(mfl.sampleID, mfl);
			
			
		}
		
		reader.close();
		return map;
	}
	
	public MappingFileLine(String s)
	{
		String[] splits = s.split("\t");
		this.tissue = splits[4];
		this.line = splits[7];
		this.ratID = splits[9];
		this.sampleID = splits[0];
		
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MappingFileLine> map = getMap();
		
		for(String s : map.keySet())
		{
			MappingFileLine mfl = map.get(s);
			System.out.println(s +  " " + mfl.getLine() + " " + mfl.getTissue() );
		}
	}
	
}
