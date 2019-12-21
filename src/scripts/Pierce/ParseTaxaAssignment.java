package scripts.Pierce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class ParseTaxaAssignment
{
	public static void main(String[] args) throws Exception
	{

		File pivotFile = new File(ConfigReader.getPierce2019Dir() + File.separator + 
					"taxaAsColumns_OTU.txt");
		
		OtuWrapper wrapper = new OtuWrapper(pivotFile);
		
		for( int x=0; x <= 4; x++)
		{
			HashMap<String, Map<String, Double>> map = pivotToLevel(wrapper, x);
			
			for(String s : map.keySet())
				System.out.println(s + " "+  map.get(s));
		}
		
	}
	
	/*
	 * x =0 == phylum
	 * x= 4  == genus
	 * 
	 * Outer key on return map is sample 
	 */ 
	public static HashMap<String, Map<String, Double>> pivotToLevel(OtuWrapper wrapper, int level ) 
		throws Exception
	{
		HashMap<String, Map<String, Double>>  returnMap =new HashMap<String, Map<String, Double>>();
		HashMap<String, String> taxaMap = getMapForLevel(level);
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			
			if( returnMap.containsKey(sampleName))
				throw new Exception("Duplicate " + sampleName);
			
			Map<String, Double> innerMap = new HashMap<String, Double>();
			returnMap.put(sampleName, innerMap);
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				String otuName = wrapper.getOtuNames().get(y);
				
				String taxaName = taxaMap.get(otuName);
				
				if( taxaName == null)
					throw new Exception("Could not find " + otuName);
				
				double newVal = wrapper.getDataPointsUnnormalized().get(x).get(y);
				
				Double oldVal = innerMap.get(taxaName);
				
				if( oldVal == null)
					oldVal =0.0;
				
				innerMap.put(taxaName, oldVal + newVal);
			}
		}
		
		return returnMap;
	}
	
	/*
	 * x =0 == phylum
	 * x= 4  == genus
	 */
	public static HashMap<String, String> getMapForLevel(int level) throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader( 
			ConfigReader.getPierce2019Dir() + File.separator+ 
					"taxa_table16s.txt"));
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 7)
				throw new Exception();
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("Duplciate " + key);
			
			String taxa = splits[level+2];
			
			map.put(key, taxa);
		}
		
		reader.close();
		return map;
	}
}
