package ruralVsUrban.mostWanted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MostWantedMetadata
{
	private final double maxFraction;
	private final double subjectFractionStool;
	private final String priority;
	
	public double getMaxFraction()
	{
		return maxFraction;
	}

	public double getSubjectFractionStool()
	{
		return subjectFractionStool;
	}

	public String getPriority()
	{
		return priority;
	}

	private MostWantedMetadata(String s, String firstLine ) throws Exception
	{
		String[] splits = s.split("\t");
		this.maxFraction = Double.parseDouble( splits[getIndex("maxFraction", firstLine)]);
		this.subjectFractionStool = Double.parseDouble( splits[getIndex("454_subjectfractions_Stool", firstLine)]); 
		this.priority = splits[1];
	}
	
	private static int getIndex(String query, String firstLine) throws Exception
	{
		String[] splits = firstLine.split("\t");
		
		for( int x=0; x < splits.length; x++)
			if( splits[x].equals(query))
				return x;
		
		throw new Exception("Could not find query");
		
	}
	
	public static HashMap<String, MostWantedMetadata> getMap() throws Exception
	{
		HashMap<String, MostWantedMetadata> map = new HashMap<String, MostWantedMetadata>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir() + File.separator + 
				"mostWanted" + File.separator + 
				"MW_all.txt")));
		
		String firstLine = reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, new MostWantedMetadata(s, firstLine));
		}
		
		return map;
	}
}
