package scripts.ChinaMerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.net.ssl.SSLContext;

import utils.ConfigReader;

public class MergeTwo
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, String> map = getBPointForwardRead();
		HashMap<Integer, List<Integer>> globalMap = getMapFromGlobalFile();
	}
	
	private static HashMap<Integer, List<Integer>> getMapFromGlobalFile() throws Exception
	{
		HashMap<Integer, List<Integer>>  map = new HashMap<Integer, List<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"global_library.fna.txt"
				+ "")));
		
		String[] firstSplits = reader.readLine().split("\t");
		
		List<Integer> keys = new ArrayList<Integer>();
		
		for( int x=1; x < firstSplits.length; x++)
			keys.add(Integer.parseInt(firstSplits[x].replace("B", "").trim()));
		
		for( Integer i : keys )
			map.put(i, new ArrayList<Integer>());
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int index =1;
			
			for( Integer i : keys)
			{
				List<Integer> innerList = map.get(i);
				innerList.add( Integer.parseInt(splits[index].trim()));
				index++;
			}
		}
		
		return map;
	}
	
	private static HashMap<Integer, String> getBPointForwardRead() throws Exception
	{
		 HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKatieDir() + File.separator + 
				"chinaur.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[4].equals("second_B") && Integer.parseInt(splits[1])==1)
			{
				Integer key = Integer.parseInt(splits[2]);
				
				if( map.containsKey(key))
					throw new Exception("No");
				
				map.put(key,s);
			}
		}
		
		return map;
	}
}
