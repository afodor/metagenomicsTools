package scripts.hansenAlleleData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class PivotOut
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getHansenAlleleDirectory() + File.separator + 
				"ecoli.txt");
		
		HashMap<Integer, String> myWTMap =getWtKoMap(inFile);

		HashMap<Integer, String> myDateMap =getDateMap(inFile);
		
		for(Integer i : myWTMap.keySet())
			System.out.println(i + " " + myWTMap.get(i) + " " + myDateMap.get(i));
	}
	
	private static HashMap<Integer,String> getWtKoMap(File f) throws Exception
	{
		HashMap<Integer,String> map = new LinkedHashMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		int x=0;
		
		TabReader tReader =new TabReader(reader.readLine());
		
		String aString = "NA";
		
		while(tReader.hasMore())
		{
			String aVal = tReader.nextToken();
			
			if( aVal.trim().length() > 0)
				aString = aVal;
			
			map.put(x, aString);
			x++;
		}
		
		reader.close();
		
		return map;
	}
	

	private static HashMap<Integer,String> getDateMap(File f) throws Exception
	{
		HashMap<Integer,String> map = new LinkedHashMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		reader.readLine();
		
		int x=0;
		
		TabReader tReader =new TabReader(reader.readLine());
		
		String aString = "NA";
		
		while(tReader.hasMore())
		{
			String aVal = tReader.nextToken();
			
			if( aVal.trim().length() > 0)
				aString = aVal;
			
			map.put(x, aString.replace("Day ", ""));
			x++;
		}
		
		for( Integer i : map.keySet())
		{
			if( map.get(i).indexOf("/") != -1)
				map.put(i, map.get(i+1));
		}
		
		reader.close();
		
		return map;
	}
}
