package scripts.topeOneAtATime;

import java.io.File;
import java.util.HashMap;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import parsers.PivotOTUs;
import utils.ConfigReader;

public class MergeUpFromAbundantOTUs
{
	private static final int THRESHOLD = 50;
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getTopeOneAtATimeDir() + 
				File.separator  + "abundantOTU" + File.separator + 
					"abundantOTUsAsColumns.txt");
		
		HashMap<String, NewRDPParserFileLine> rdpMap = 
		NewRDPParserFileLine.getAsMapFromSingleThread(
				ConfigReader.getTopeOneAtATimeDir() + 
				File.separator  + "abundantOTU" + File.separator + 
					"consToRDP2_10_1.txt");
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(taxa);
			HashMap<String, HashMap<String,Integer>> map = 
					mergeForALevel(wrapper, rdpMap, taxa);
			PivotOTUs.writeResults(map,
					ConfigReader.getTopeOneAtATimeDir() + 
					File.separator  + "abundantOTU" + File.separator + 
						"abundantOTUs_" + taxa + "_AsColumns.txt");
		}
	}
	
	private static HashMap<String, HashMap<String,Integer>> 
		mergeForALevel(OtuWrapper wrapper, HashMap<String, NewRDPParserFileLine> rdpMap ,
					String taxa) throws Exception
	{
		HashMap<String, HashMap<String,Integer>>  map = 
				new HashMap<String, HashMap<String,Integer>>();
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			
			if( map.containsKey(sampleName))
				throw new Exception("Parsing error");
			
			HashMap<String, Integer> innerMap = new HashMap<String,Integer>();
			map.put(sampleName, innerMap);
			
			for(int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				String otu = "Consensus" + wrapper.getOtuNames().get(y);
				NewRDPParserFileLine line = rdpMap.get(otu);
				
				if( line == null)
					throw new Exception("Could not find " + otu);
				
				NewRDPNode node = line.getTaxaMap().get(taxa);
				
				if( node != null && node.getScore() >= THRESHOLD)
				{
					Integer count = innerMap.get(node.getTaxaName());
					
					if( count == null)
						count = 0;
					
					count +=  (int) (0.001 +  wrapper.getDataPointsUnnormalized().get(x).get(y));
					innerMap.put(node.getTaxaName(), count);
				}
			}
		}
		
		return map;
	}
}
