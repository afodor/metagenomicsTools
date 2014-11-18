package ruralVsUrban;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */
public class PivotRDPs
{
	
	
	/*
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>>  map = getMap(
				ConfigReader.getWolfgangIrishOnlyAbundantOTUDir() + File.separator + 
				"combinedOtus.txt");
		writeResults(map,ConfigReader.getWolfgangIrishOnlyAbundantOTUDir() + File.separator 
				+ "otusPivotedTaxaAsColumns.txt");
	}
	*/
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println( NewRDPParserFileLine.TAXA_ARRAY[x]);
			HashMap<String, HashMap<String, Integer>>  map = getMap(
					ConfigReader.getChinaDir() + File.separator + 
					"threeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt");
			 
			writeResults(map,ConfigReader.getChinaDir() + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "_taxaAsColumns.txt");
		}
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
		
		for( String s : otuList)
			writer.write("\t" +  s);
		
		writer.write("\n");
		
		for( String s : map.keySet())
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
