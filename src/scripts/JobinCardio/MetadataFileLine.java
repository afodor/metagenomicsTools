package scripts.JobinCardio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private final int sampleIndex;
	private final String experimentString;
	private final int experimentInt;
	private final String group;
	private final boolean isRun2;
	
	public int getSampleIndex()
	{
		return sampleIndex;
	}

	public boolean getIsrun2()
	{
		return isRun2;
	}
	
	public String getExperimentString()
	{
		return experimentString;
	}

	public int getExperimentInt()
	{
		return experimentInt;
	}

	public String getGroup()
	{
		return group;
	}

	private MetadataFileLine(String s, boolean isRun2) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleIndex = Integer.parseInt(splits[0]);
		this.experimentString = splits[3];
		this.experimentInt = Integer.parseInt(splits[4]);
		this.group = splits[5];
		this.isRun2 = isRun2;
	}
	
	private static void addToMap(File f, HashMap<Integer, MetadataFileLine> map, boolean isRun2)
		throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.trim().length() > 0 )
			{
				MetadataFileLine mfl = new MetadataFileLine(s, isRun2);
				
				if( map.containsKey(mfl.sampleIndex))
					throw new Exception("No");
				
				map.put(mfl.sampleIndex, mfl);
			}
		}
		
	}
	
	public static HashMap<Integer, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<Integer, MetadataFileLine> map = new HashMap<Integer, MetadataFileLine>();
		
		File run2 = new File(
				ConfigReader.getJobinCardioDir() + File.separator + 
				"barcode Run2 5-30-2015.txt");
		
		
		addToMap(run2, map, true);
		
		File run1 = new File(
				ConfigReader.getJobinCardioDir() + File.separator + 
				"Barcode Run1 8-6-2015.txt");
		
		addToMap(run1, map, false);
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, MetadataFileLine> map = getMetaMap();
		
		for(Integer i : map.keySet())
			System.out.println(i + " " + map.get(i).getIsrun2());
	}
}
