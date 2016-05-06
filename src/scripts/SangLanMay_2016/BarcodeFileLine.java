package scripts.SangLanMay_2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class BarcodeFileLine
{
	public static HashMap<String, String> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getSangLabMay2016Dir() + File.separator + "022516AY27F-mapping.txt")));
		
		reader.readLine(); 
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[1]))
				throw new Exception("No");
			
			map.put(splits[1],splits[0]);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getBarcodeToSampleMap();
		
		for(String s : map.keySet())
			System.out.println(s +  " " + map.get(s));
	}
}
