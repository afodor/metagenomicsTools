package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private final String sanVsSol;
	private final String plq3Orplq4;
	private final String rNumber;
	
	private MetadataFileLine(String firstLineKey, String secondLineKey)
		throws Exception
	{
		//System.out.println(firstLineKey + " " + secondLineKey);
		String rNumber = firstLineKey.split("-")[0];
		
		if( ! rNumber.startsWith("R"))
			throw new Exception("No");
		
		this.rNumber = rNumber;
		
		this.plq3Orplq4 = secondLineKey.split("_")[1];
		
		if( ! this.plq3Orplq4.startsWith("plq"))
			throw new Exception("No");
		
		this.sanVsSol = secondLineKey.split("_")[0];
		
		//if( ! this.sanVsSol.equals(firstLineKey.split("-")[1]))
			//	throw new Exception("No");
	}
	
	public String getSanVsSol()
	{
		return sanVsSol;
	}

	public String getPlq3Orplq4()
	{
		return plq3Orplq4;
	}

	public String getrNumber()
	{
		return rNumber;
	}

	public static HashMap<String, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map = new HashMap<String, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getGoranTrialDir()+
				File.separator + "familyOut.txt")));
		
		String[] firstSplits = reader.readLine().split("\t");
		String[] secondSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
		{
			if( map.containsKey(firstSplits[x]))
				throw new Exception("Duplicate key");
			
			map.put(firstSplits[x], new MetadataFileLine(firstSplits[x],secondSplits[x]));
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = getMetaMap();
		
		for(String s: map.keySet())
		{
			MetadataFileLine mfl = map.get(s);
			System.out.println(s + " " +mfl.getPlq3Orplq4() + " " + mfl.getSanVsSol() + " " + mfl.getrNumber());
		}
	}
}
