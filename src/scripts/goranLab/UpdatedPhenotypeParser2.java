package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class UpdatedPhenotypeParser2
{
	public static String SUGBEV_PLUS_JUICE = "sugbev_plusjuice";
	
	public static HashMap<Integer, Double> getBeverageMap(String columnName) throws Exception
	{
		HashMap<Integer, Double> map = new LinkedHashMap<Integer, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranTrialDir() + File.separator + "sol16sdiet041915AF.txt")));
		
		int index = getIndex(reader.readLine(), columnName);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Integer parseInt = Integer.parseInt(splits[0]);
			
			if( map.containsKey(parseInt))
				throw new Exception("No");
			
			map.put(parseInt, Double.parseDouble(splits[index]));
		}
		
		return map;
	}
	
	private static int getIndex(String header, String columnName) throws Exception
	{
		String[] splits = header.split("\t");
		
		for( int x=0; x < splits.length; x++)
			if( splits[x].equals(columnName))
				return x;
		
		throw new Exception("Could not find " + header);
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, Double> map = getBeverageMap(SUGBEV_PLUS_JUICE);
		
		for( int i : map.keySet())
			System.out.println(i + " " + map.get(i));
	}
}
