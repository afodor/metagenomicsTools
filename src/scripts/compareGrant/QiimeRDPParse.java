package scripts.compareGrant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class QiimeRDPParse
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,Double>>  expectedMap = getExpectedMap();
		
		for(String s : expectedMap.keySet())
			System.out.println(s + " " + expectedMap.get(s));
	}
	
	// outer key is the sample 
	private static HashMap<String, HashMap<String,Double>> getExpectedMap() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  map =new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getGrantCheckDir() + File.separator + "otu_table_L6.txt")));
		
		reader.readLine();
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
		{
			HashMap<String,Double> innerMap =new HashMap<>();
			
			map.put(topSplits[x], innerMap);
		}
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String taxa = splits[0];
			
			for( int x=1; x < splits.length; x++)
			{
				HashMap<String, Double> innerMap = map.get(topSplits[x]);
				
				if( innerMap.containsKey(taxa))
					throw new Exception("Duplicate" );
				
				innerMap.put(taxa, Double.parseDouble(splits[x]));
			}
		}
		
		return map;
	}
}
