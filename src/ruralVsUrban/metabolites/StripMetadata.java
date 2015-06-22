package ruralVsUrban.metabolites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;
import utils.TabReader;

public class StripMetadata
{
	public static List<String> getNames() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(ConfigReader.getChinaDir() + File.separator + 
						"metabolites" + File.separator + 
						"for_anthony.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		int startIndex = getIndex(splits, "As_water");
		int endIndex = getIndex( splits, "As_Toenail");
		
		List<String> list = new ArrayList<String>();
	
		for( int x=startIndex; x <= endIndex; x++)
			list.add(splits[x]);
		
		reader.close();
		
		return list;
	}
	
	public static HashMap<Integer, List<Double>> getMap() throws Exception
	{
		HashMap<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(ConfigReader.getChinaDir() + File.separator + 
						"metabolites" + File.separator + 
						"for_anthony.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		int startIndex = getIndex(splits, "As_water");
		int endIndex = getIndex( splits, "As_Toenail");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			Integer id = Integer.parseInt(tReader.nextToken());
			for( int x=1;x  < startIndex; x++)
				tReader.nextToken();
			
			List<Double> list = new ArrayList<Double>();
			
			for( int x= startIndex; x <= endIndex; x++)
			{
				String token = tReader.nextToken();
				
				if( token.trim().length() > 0 )
					list.add(Double.parseDouble(token));
				else
					list.add(null);
				
			}
			
			List<Double> oldList = map.get(id);
			
			if( oldList == null)
			{
				map.put(id, list);
			}
			else
			{
				if( ! oldList.equals(list))
				{
					System.out.println(oldList);
					System.out.println(list);
					throw new Exception("No");

				}
			}
		}
		
		reader.close();
		return map;
	}
	
	private static int getIndex(String[] splits, String tag) throws Exception
	{
		for( int x=0; x < splits.length; x++)
			if( splits[x].equals(tag) )
				return x;
		
		throw new Exception("No");
		
	}
	
	public static void main(String[] args) throws Exception
	{
		List<String> list = getNames();
		System.out.println(list);
		HashMap<Integer, List<Double>> map=  getMap();
		System.out.println(map);
		
		for(Integer i : map.keySet())
		{
			if( map.get(i).size() != list.size())
				throw new Exception("No");
		}
		
	}
}
