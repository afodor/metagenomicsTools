package mbqc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class CompareFoldChanges
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMbqcDir() + 
				File.separator + "af_out" + File.separator +  "pValuesNAVsNonNA.txt")));
		
		HashMap<String, Double> expectedMap = getExpected();
		
		for(String s : expectedMap.keySet())
			System.out.println(s);
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0]  +"_" + splits[1] + "_" + splits[2] + "_" + splits[5];
			
			Double expVal = expectedMap.get(key);
			
			if( expVal == null)
				throw new Exception("No " + key);
			
			if( splits[9].trim().length() > 0 )
			{

				if( Math.abs(expVal - Double.parseDouble(splits[9])) > 0.0001)
					throw new Exception("No");
			}
			
		}
		
		System.out.println("Passed foldchange");
	}
	
	private static HashMap<String, Double> getExpected() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
		ConfigReader.getMbqcDir() + File.separator + "af_out" + File.separator + 
		"testValsForNAvsNonNA.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0] + "_" + splits[1] + "_" + splits[2] + "_" +  splits[3].split("p__")[1];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Double.parseDouble(splits[6]));
			
		}
		
		return map;
	}
}
