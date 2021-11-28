package scripts.dla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MetadataFileLine
{
	private final String sampleName;
	private final String timepoint;
	private final String patient;
	
	public String getSampleName()
	{
		return sampleName;
	}
	
	public String getTimepoint()
	{
		return timepoint;
	}
	
	public String getPatient()
	{
		return patient;
	}
	
	private MetadataFileLine(String  s)
	{
		String[] splits = s.split("\t");
		
		this.sampleName = splits[0];
		this.timepoint = splits[1];
		this.patient = splits[2];
	}
	
	@SuppressWarnings("resource")
	public static HashMap<String, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\DLA_Analyses2021-main\\input\\metadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			map.put(key, new MetadataFileLine(s));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).patient);
		
	}
	
}
