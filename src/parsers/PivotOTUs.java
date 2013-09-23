package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class PivotOTUs
{
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>>  map = getMap(
				"C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\rdpGenusThreeCol.txt");
		
		writeResults(map,"C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\pivotFromJava.txt");
	}
	
	private static List<String> getOTUSAtThreshold(
			HashMap<String, HashMap<String, Integer>>  map,
									int threshold) throws Exception
	{
		
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		
		for( String s: map.keySet() )
		{
			HashMap<String, Integer> innerMap = map.get(s);
				
			for(String possibleOtu : innerMap.keySet())
			{
				Integer oldCount = countMap.get(possibleOtu);
					
				if(oldCount == null)
						oldCount = 0;
					
				oldCount += innerMap.get(possibleOtu);
					
				countMap.put(possibleOtu, oldCount);
			}
		}
			
		List<String> otuList= new ArrayList<String>();
		
		for( String s : countMap.keySet() )
			if( countMap.get(s) >= threshold )
				otuList.add(s);
		
		return otuList;
	
	}
	
	public static void writeResults(HashMap<String, HashMap<String, Integer>>  map, String filepath ) 
							throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			filepath)));
		
		writer.write("sample");
		List<String> otuList = getOTUSAtThreshold(map, 0);
		Collections.sort(otuList);
		
		for( String s : otuList)
			writer.write("\t" +  s);
		
		writer.write("\n");
		
		List<String> samples = new ArrayList<String>();
		
		for( String s : map.keySet())
			samples.add(s);
		
		Collections.sort(samples);
		
		for( String s : samples)
		{
			//String expandedString = PivotRDPs.getExpandedString( s);
			//writer.write( expandedString );
			writer.write(s);
				
			HashMap<String, Integer> innerMap = map.get(s);
				
			for( String otu : otuList)
			{
				Integer aVal = innerMap.get(otu);
					
				if(aVal== null)
					aVal = 0;
					
				writer.write("\t" + aVal );
			}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	/*
	 * Parses a file in the format (sequenceID column is ignored!)
	 * 	sequenceID		sample	otu
		GBFQPQG01AMPIA	12-Aug	1
		GBFQPQG01AN1HW	10-Apr	545
		GBFQPQG01A6R9C	10-Mar	4
		GBFQPQG01AN1HP	1-Dec	5
		GBFQPQG01AMPHO	27-Jun	6
	 */
	public static HashMap<String, HashMap<String, Integer>> getMap(String filePath) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = 
			new HashMap<String, HashMap<String,Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath
				)));
			
		reader.readLine();
			
		String nextLine = reader.readLine();
		
		while(nextLine != null)
		{
			StringTokenizer sToken = new StringTokenizer(nextLine, "\t");
			sToken.nextToken();
			String sample = sToken.nextToken();
			String otu = sToken.nextToken();
			
			if( sToken.hasMoreTokens())
				throw new Exception("No");
			
			HashMap<String, Integer> innerMap = map.get(sample);
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Integer>();
				map.put(sample, innerMap);
			}
			
			Integer aValue = innerMap.get(otu);
			
			if( aValue == null)
				aValue =0;
			
			aValue++;
			
			innerMap.put(otu, aValue);
			nextLine = reader.readLine();
		}
		
		return map;
	}
}
