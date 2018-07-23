package scripts.jamesEOE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.TabReader;

public class QuickNormalizationCheck
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(new File("C:\\JamesEOE\\genus.tsv"));
		
		HashMap<String, HashMap<String,Double>>  map = getNormalizedMap();
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleID = wrapper.getSampleNames().get(x);
			
			HashMap<String, Double> innerMap = map.get(sampleID);
			
			if(innerMap == null)
				throw new Exception("Could not find " + sampleID);
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				double expectedVal = wrapper.getDataPointsNormalizedThenLogged().get(x).get(y);
				
				String otuName = wrapper.getOtuNames().get(y);
				otuName = otuName.replaceAll("/", "\\.");
				otuName = otuName.replaceAll(" ", "\\.");
				Double spreadsheetVal = innerMap.get(otuName);
				
				if( spreadsheetVal == null)
					throw new Exception("Could not find " + otuName);
				
				double diff = Math.abs(spreadsheetVal - expectedVal);
				
				System.out.println(spreadsheetVal  + " " + expectedVal + " "+  diff);
				
				if( diff > 0.00001)
					throw new Exception("Fail " + spreadsheetVal  + " " + expectedVal + " "+  diff);
			}
		}
	}
	
	// outer key is sample; inner key is taxa
	private static HashMap<String, HashMap<String,Double>> getNormalizedMap() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\JamesEOE\\genus_Normalized.tsv")));
		
		String [] topSplits = reader.readLine().split("\t");
		
		for(String s =reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			String key = tReader.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			HashMap<String,Double> innerMap = new HashMap<>();
			map.put(key,innerMap);
			
			for( int x=1; x <=6; x++)
				tReader.nextToken();
			
			int index =7;
			
			while( tReader.hasMore())
			{
				String taxaKey = topSplits[index];
				if( innerMap.containsKey(taxaKey))
					throw new Exception("Logic error");
				
				innerMap.put(taxaKey, Double.parseDouble(tReader.nextToken()));
				index++;
			}
		}
		
		return map;
	}
}
