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
	
	public int getSampleIndex()
	{
		return sampleIndex;
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

	private MetadataFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		this.sampleIndex = Integer.parseInt(splits[0]);
		this.experimentString = splits[3];
		this.experimentInt = Integer.parseInt(splits[4]);
		this.group = splits[5];
	}
	
	public static HashMap<Integer, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<Integer, MetadataFileLine> map = new HashMap<Integer, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinCardioDir() + File.separator + 
					"barcode Run2 5-30-2015.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.trim().length() > 0 )
			{
				MetadataFileLine mfl = new MetadataFileLine(s);
				
				if( map.containsKey(mfl.sampleIndex))
					throw new Exception("No");
				
				map.put(mfl.sampleIndex, mfl);
			}
		}
		
		return map;
	}
}
