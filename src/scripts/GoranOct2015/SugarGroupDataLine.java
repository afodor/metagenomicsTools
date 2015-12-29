package scripts.GoranOct2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class SugarGroupDataLine
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, Integer> map = getMap();
		
		for(Integer i: map.keySet())
			System.out.println(i +  " " + map.get(i));
		
	}
	
	public static HashMap<Integer, Integer> getMap() throws Exception
	{
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + 
					"Phenotype Data Long TLA 02Oct15.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			Integer key = Integer.parseInt(tReader.nextToken());
			
			for( int x=0;x < 5; x++)
				tReader.nextToken();
			
			Integer val = Integer.parseInt(tReader.nextToken());
			
			Integer oldVal = map.get(key);
			
			if( oldVal != null )
				if( ! oldVal.equals(val) )
					throw new Exception("Inconsitent annotation");
			
			map.put(key, val);
		}
		
		return map;
	}
}
