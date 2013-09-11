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


package ratSaccharine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;


public class MergeMap
{
	private static HashMap<String, String> readMapFile() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				"mapOut.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			s = s.replaceAll("\"", "");
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			String firstToken = sToken.nextToken();
			
			if( map.containsKey( firstToken))
				throw new Exception("Duplicate");
			
			map.put(firstToken, s.replaceAll("\"", ""));
		}
		
		reader.close();
		
		return map;
	}
	
	private static String getFirstLineOfMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				"mapOut.txt")));
		
		String aString = reader.readLine();
		
		reader.close();
		
		return aString;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> mapLines = readMapFile();
		System.out.println(mapLines);
		
		BufferedReader reader=  new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				//"ttuLyte_70_mergedReads_logNorm_counts.bc.pcoa.axes")));
				"unweighted_unifrac_pc.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				"mergedUnweightedUnifrac.txt")));
		
		writer.write("id\t");
		
		writer.write(getFirstLineOfMap());
		
		for( int x=1; x <=40; x++)
			writer.write("\taxis" + x);
		
		writer.write("\n");
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
			if( ! s.startsWith("#"))
			{
				s = s.replaceAll("\"", "");
				StringTokenizer sToken = new StringTokenizer(s, "\t");
				String key = sToken.nextToken();
				
				String mapLine= mapLines.get(key);
				
				if( mapLine == null)
					throw new Exception("No " + key);
				
				writer.write(mapLine);
				
				for( int x=1; x <=40; x++)
					writer.write("\t" + sToken.nextToken().replaceAll("\"", ""));
				
				writer.write("\n");
			}
		
		writer.flush();  writer.close();
		
		reader.close();
		
	}
}
