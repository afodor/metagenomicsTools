package kleb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class ReparseDates
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> datesMap = getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getKlebDir() + File.separator + 
				"distanceVsTimeForAbstract.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s !=null; s=reader.readLine())
		{
			System.out.println(s);
			String[] splits = s.split("\t");
			String firstKey = splits[0];
			String secondKey = splits[1];
			
			if( firstKey.equals(secondKey))
				throw new Exception("NO");
			
			if( ! datesMap.get(firstKey).equals(splits[2]))
				throw new Exception("NO " );
			
			if( ! datesMap.get(secondKey).equals(splits[3]))
				throw new Exception("NO " );
			
			
		}
		
		reader.close();
		System.out.println("PASSED");
	}
	
	private static HashMap<String, String> getMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getKlebDir() + File.separator + 
				"Final_BroadSamples_Locations_AF.txt"));
		
		reader.readLine();  reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0].trim(), splits[9]);
		}
		
		
		reader.close();
		
		return map;
	}
}
