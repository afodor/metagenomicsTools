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
		
		HashMap<Integer, String> myMap =getWtKoMap(inFile);
		
		for(Integer i : myMap.keySet())
			System.out.println(i + " " + myMap.get(i));
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
}
