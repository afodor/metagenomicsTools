package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private String sanVsSol;
	private String plq3Orplq4;
	private String rNumber;
	
	private static HashMap<String, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map = new HashMap<String, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getGoranTrialDir()+
				File.separator + "familyOut.txt")));
		
		String[] firstSplits = reader.readLine().split("\t");
		String[] secondSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
		{
			System.out.println(firstSplits[x] + " " + secondSplits[x]);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = getMetaMap();
	}
}
