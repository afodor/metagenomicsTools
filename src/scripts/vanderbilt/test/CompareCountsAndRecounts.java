package scripts.vanderbilt.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class CompareCountsAndRecounts
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getVanderbiltDir()  + File.separator + "spreadsheets" +
						File.separator + "pivoted_familyasColumns.txt");
		
		for(int x=0; x < wrapper.getSampleNames().size();x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			System.out.println(sampleName);
			HashMap<String, Double> map = getCountsMap(sampleName);
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				String otu = wrapper.getOtuNames().get(y);
				Double expected = map.get(otu);
				Double wrapperCount = wrapper.getDataPointsUnnormalized().get(x).get(y);
				
				if( expected == null)
				{
					if( wrapperCount != 0 )
						throw new Exception("Fail");
				}
				else
				{
					if( Math.abs( wrapperCount - expected ) > 0.00001)
						throw new Exception("Fail " + wrapperCount + " " + expected + " "+ otu);
				}
			}
		}
		
		System.out.println("test passed");
	}
	
	private static HashMap<String, Double> getCountsMap(String sample) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getVanderbiltDir() + File.separator + 
				"recountDir" + File.separator +  
				sample + "toRdp.txt.counts")));
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			StringTokenizer sToken =new StringTokenizer(s, "\t");
			
			String key = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Double.parseDouble(sToken.nextToken()));
		}
		
		return map;
	}
}
