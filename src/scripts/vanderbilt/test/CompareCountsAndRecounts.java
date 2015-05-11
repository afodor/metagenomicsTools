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
		
		for(String s : wrapper.getSampleNames())
		{
			System.out.println(s);
			HashMap<String, Integer> map = getCountsMap(s);
		}
	}
	
	private static HashMap<String, Integer> getCountsMap(String sample) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getVanderbiltDir() + File.separator + 
				"recountDir" + File.separator +  
				sample + "toRdp.txt.counts")));
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			StringTokenizer sToken =new StringTokenizer(s, "\t");
			
			String key = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Integer.parseInt(sToken.nextToken()));
		}
		
		return map;
	}
}
