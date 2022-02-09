package scripts.hansenAlleleData_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.TabReader;

public class PivotOut
{
	public static void main(String[] args) throws Exception
	{
		File inDirectory = new File( "C:\\JonathanHansenAlleles\\inputTextFiles_2");
		
		String[] inputFiles = inDirectory.list();
		
		List<String> fileNames =new ArrayList<String>();
		
		for(String s : inputFiles)
		{
			if( s.endsWith( ".txt") && s.indexOf("annotations") == -1 )
			{
				File inFile = new File(inDirectory.getAbsolutePath() + File.separator + 
							s);
				
				String prefix = s.replace(".txt", "");
		
				File outFile = new File("C:\\JonathanHansenAlleles\\outputTextFiles_2"
						+ File.separator +  prefix +  "ForR.txt");
				
				System.out.println(inFile.getName() + " "+ outFile.getName());
				fileNames.add(outFile.getName());
				writeFileForR(inFile, outFile);
			}
		}
		
		System.out.print("inFiles <- c(");
		for(int x=0; x < fileNames.size(); x++)
		{
			System.out.print( "\"" +  fileNames.get(x).replace("ForR.txt", "")  + "\"" );
			
			if( x < fileNames.size() - 1)
				System.out.print(",");
		}
		
		System.out.println(")");
	}
	
	public static void writeFileForR(File inFile, File outFile) throws Exception
	{	
		HashMap<Integer, String> myWTMap =getWtKoMap(inFile);
 
		HashMap<Integer, String> myDateMap =getDateMap(inFile);
		
		System.out.println(myDateMap);
		
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

		writer.write("\taverageReadDepth");
		
		for( int x=6; x < topSplits.length-2; x++ )
		{
			String key = getKey(x, myWTMap, myDateMap, mouseIDMap);
			
			String nextKey = getKey(x+1, myWTMap, myDateMap, mouseIDMap);
			
			if( ! key.equals(nextKey))
				throw new Exception("Parsing error " + key + " " + nextKey + " "+ x);
			
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
			
			StringBuffer buff = new StringBuffer();
			long totalReads = 0;
			long numAboveThreshold =0;
			for( int x=6; x < splits.length-1; x++)
			{
				double aVal = Double.parseDouble(splits[x]);
				double anotherVal =Double.parseDouble(splits[x+1]);
				
				if( anotherVal >= 10 )
				{
					buff.append("\t" +(aVal / anotherVal) );
					totalReads += anotherVal;
					numAboveThreshold++;
				}
				else
				{
					buff.append("\tNA");
				}
					
				x++;
			}
			
			double averageReadDepth = ((double)totalReads)/numAboveThreshold;
			
			if( numAboveThreshold == 0 )
				averageReadDepth = 0;
				
			writer.write("\t" +averageReadDepth + buff.toString());
			
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
			if( ! map.get(i).equals("NA"))
				if(  map.get(i).indexOf("/") != -1 || Integer.parseInt(map.get(i))> 10000)
					map.put(i, map.get(i+1));
		}
		
		reader.close();
		
		return map;
	}
}
