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


package exerciseCrossover;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeSpreadsheets
{
	private static String numericOnly(String s)
	{
		StringBuffer buff =new StringBuffer();
		
		for( char c : s.toCharArray() )
		{
			if( Character.isDigit(c))
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> metaMap =getMetadataMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCrossoverExerciseDir() + File.separator + 
		"mergedPCOA.txt")));
		
		writer.write("sampleNum\t");
		writer.write("patientID\tfbar\trbar\tsample\ttrt\ttimepoint\tsample\ttrt\ttimepoint\t");
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getCrossoverExerciseDir() + File.separator + 
				"pcoaAll.txt")));
		
		writer.write(reader.readLine().replaceAll("\"", "") + "\n");
		
		for(String s = reader.readLine(); s!= null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String key= sToken.nextToken();
			String aLine = metaMap.get(key);
			if(aLine == null)
				throw new Exception("No");
			
			StringTokenizer mapToken = new StringTokenizer(aLine, "\t");
			
			writer.write(  mapToken.nextToken() );
			writer.write( "\t" + numericOnly(mapToken.nextToken()) );
			
			while(mapToken.hasMoreTokens())
				writer.write("\t" + mapToken.nextToken());
			
			while(sToken.hasMoreTokens())
				writer.write("\t" + sToken.nextToken());
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		
		reader.close();
	}
	
	private static HashMap<String, String> getMetadataMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getCrossoverExerciseDir() + File.separator + 
				"mapAll.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!=null; s = reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String key = new StringTokenizer(s, "\t").nextToken();
			if(map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, s);
		}
		
		reader.close();
		
		return map;
	}
}
