package scripts.ting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{
	private int animalID;
	private String genotype;
	private int time;
	private float initialBodyWeight;
	private float lowestBodyWeight;
	private float bodyWeightAtDay8;
	private float maxWeightLossPercent;
	
	public static HashMap<Integer, MetadataParser> getMetaMap() throws Exception
	{
		
		HashMap<Integer, MetadataParser>  map = new HashMap<Integer,MetadataParser>();
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getTingDir() 
				+ File.separator + "20170224_Casp11_DSS_16S_DeNovo.txt")));
		
		String[] mouseSplits = reader.readLine().split("\t");
		String[] genotypeSplits = reader.readLine().split("\t");
		String[] cageSplits = reader.readLine().split("\t");
		String[] timeSplits = reader.readLine().split("\t");
		String[] initialBodySplits = reader.readLine().split("\t");
		String[] lowestBodySplits = reader.readLine().split("\t");
		String[] bodyWeightDay8Splits = reader.readLine().split("\t");
		
		String[] maxWeightLossPercent = reader.readLine().split("\t");
		
		for( int x=1;x  < mouseSplits.length; x++)
		{
			MetadataParser mp = new MetadataParser();
			int mouseId = Integer.parseInt(mouseSplits[x]);
			
			if( map.containsKey(mouseId)) 
				throw new Exception("No");
			
			map.put(mouseId, mp);
			
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, MetadataParser> map =getMetaMap();
	}
}
