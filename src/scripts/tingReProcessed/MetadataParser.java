package scripts.tingReProcessed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{
	private int animalID;
	private String genotype;
	private String cage;
	boolean cohousing;
	private int time;
	private float initialBodyWeight;
	private float lowestBodyWeight;
	private float bodyWeightAtDay8;
	private float maxWeightLossPercent;
	
	public String getCage()
	{
		return cage;
	}
	
	public int getAnimalID()
	{
		return animalID;
	}

	public String getGenotype()
	{
		return genotype;
	}

	public int getTime()
	{
		return time;
	}
	
	public boolean isCohousing()
	{
		return cohousing;
	}

	public float getInitialBodyWeight()
	{
		return initialBodyWeight;
	}

	public float getLowestBodyWeight()
	{
		return lowestBodyWeight;
	}

	public float getBodyWeightAtDay8()
	{
		return bodyWeightAtDay8;
	}

	public float getMaxWeightLossPercent()
	{
		return maxWeightLossPercent;
	}

	public static HashMap<Integer, MetadataParser> getMetaMap() throws Exception
	{
		
		HashMap<Integer, MetadataParser>  map = new HashMap<Integer,MetadataParser>();
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getTingDir() 
				+ File.separator + "may_2017_rerun" + File.separator + 
				"20170512_Casp11_DSS_5groups_16S_DeNovo_NoPhiX_NoPrimerSeq_L6.txt")));
		
		String[] mouseSplits = reader.readLine().split("\t");
		String[] genotypeSplits = reader.readLine().split("\t");
		String[] cageSplits = reader.readLine().split("\t");
		String[] coHouseSplits = reader.readLine().split("\t");
		String[] timeSplits = reader.readLine().split("\t");
		String[] initialBodySplits = reader.readLine().split("\t");
		String[] lowestBodySplits = reader.readLine().split("\t");
		String[] bodyWeightDay8Splits = reader.readLine().split("\t");
		
		String[] maxWeightLossPercentSplits = reader.readLine().split("\t");
		
		for( int x=1;x  < mouseSplits.length; x++)
		{
			MetadataParser mp = new MetadataParser();
			int mouseId = Integer.parseInt(mouseSplits[x]);
			
			if( map.containsKey(mouseId)) 
				throw new Exception("No");
			
			map.put(mouseId, mp);
			
			mp.genotype = genotypeSplits[x];
			mp.time = Integer.parseInt(timeSplits[x]);
			mp.initialBodyWeight = Float.parseFloat(initialBodySplits[x]);
			mp.lowestBodyWeight = Float.parseFloat(lowestBodySplits[x]);
			mp.bodyWeightAtDay8 = Float.parseFloat(bodyWeightDay8Splits[x]);
			mp.maxWeightLossPercent= Float.parseFloat(maxWeightLossPercentSplits[x].replace("%", ""));
			mp.cage = cageSplits[x];
			
			if( coHouseSplits[x].equals("Yes"))
				mp.cohousing = true;
			else if ( coHouseSplits[x].equals("No"))
				mp.cohousing = false;
			else throw new Exception("Unknown " + coHouseSplits[x]);
			
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, MetadataParser> metaMap = getMetaMap();
		
		for(Integer i : metaMap.keySet() )
			System.out.println(i + " " + metaMap.get(i).maxWeightLossPercent + " " + metaMap.get(i).cohousing);
	}
}
