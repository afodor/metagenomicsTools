package scripts.hansenAlleleData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class PivotOut
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getHansenAlleleDirectory() + File.separator + 
				"inputTextFiles" + File.separator + "B_caccae.txt");
		
		File outFile = new File(ConfigReader.getHansenAlleleDirectory() + File.separator + 
				"outputTextFiles" + File.separator + "B_caccaeForR.txt");
		
		writeFileForR(inFile, outFile);
	}
	
	public static void writeFileForR(File inFile, File outFile) throws Exception
	{	
		HashMap<Integer, String> myWTMap =getWtKoMap(inFile);
 
		HashMap<Integer, String> myDateMap =getDateMap(inFile);
		
		HashMap<Integer, String> mouseIDMap =getMouseIDMap(inFile);
		
		for(Integer i : myWTMap.keySet())
			System.out.println(i + " " + myWTMap.get(i) + " " + myDateMap.get(i) + " " + mouseIDMap.get(i));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();  reader.readLine();  reader.readLine();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String topLine = reader.readLine();
		
		String[] topSplits= topLine.split("\t");

		writer.write(topSplits[0]);
	
		for( int x=1; x < 6; x++)
			writer.write("\t" + topSplits[x]);

		for( int x=6; x < topSplits.length-2; x++ )
		{
			String key = getKey(x, myWTMap, myDateMap, mouseIDMap);
			
			String nextKey = getKey(x+1, myWTMap, myDateMap, mouseIDMap);
			
			if( ! key.equals(nextKey))
				throw new Exception("Parsing error");
			
			writer.write("\t" + key);
			
			x++;
		}
		
		writer.write("\tcontrol\n");
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0]);
			for( int x=1; x < 6; x++)
				writer.write("\t" + splits[x]);
			
			for( int x=6; x < splits.length-1; x++)
			{
				double aVal = Double.parseDouble(splits[x]);
				double anotherVal =Double.parseDouble(splits[x+1]);
				
				if( anotherVal >= 10 )
					writer.write("\t" +(aVal / anotherVal) );
				else
					writer.write("\tNA");
				
				x++;
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static String getKey(int x, HashMap<Integer, String> myWTMap, HashMap<Integer, String>  myDateMap,
			HashMap<Integer, String> mouseIDMap ) throws Exception
	{
		return myWTMap.get(x) +"_" +mouseIDMap.get(x) + "_" + myDateMap.get(x);
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

	private static HashMap<Integer,String> getMouseIDMap(File f) throws Exception
	{
		HashMap<Integer,String> map = new LinkedHashMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		reader.readLine();reader.readLine();
		
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
