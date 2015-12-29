package scripts.GoranOct2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class PcrFileLine
{
	public static HashMap<String,Double> getCTMeanMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String,Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + "PC0016 16S qPCR.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			//System.out.println(s);
			TabReader tr = new TabReader(s);
			
			tr.nextToken(); tr.nextToken();
			
			String key = tr.nextToken().trim();
			
			if( key.length() == 0 )
				throw new Exception("no");
			
			for( int x=0; x < 4; x++)
				tr.nextToken();
			
			double val = Double.parseDouble(tr.nextToken());
			
			if( map.containsKey(key) && ! map.get(key).equals(val))
				System.out.println("Warning duplicate " + key +  " " + map.get(key) + " " + val);
			
			map.put(key, val);
			
			for( int x=0; x < 4; x++)
				tr.nextToken();
			
			while( tr.hasMore())
			{
				if( tr.hasMore() && tr.nextToken().trim().length() != 0 )
					throw new Exception("No ");
				
			}
			
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getCTMeanMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
