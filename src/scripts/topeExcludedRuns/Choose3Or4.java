package scripts.topeExcludedRuns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class Choose3Or4
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> map = getThreeOrFourMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		
	}
	
	private static HashMap<String, Integer> getThreeOrFourMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeFeb2016Dir() + File.separator 
			+ "Copy of All Diversticulosis Illumina Primer Seq.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int key = Integer.parseInt(splits[12]);
			
			if( key == 3)
			{
				if( ! splits[11].equals("4/29/2015"))
					throw new Exception("No");
			}
			else if ( key == 4)
			{
				if( ! splits[11].equals("9/24/2015"))
					throw new Exception("No");
			}
			else throw new Exception("Unknow key");
			
			map.put(splits[0], key);
		}
		
		return map;
	}
}
