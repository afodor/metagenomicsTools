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


package scripts.clusterManipulations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;



public class MapBlastHitsToBitScore
{
	public static HashMap<Integer, Integer> mapHitsToScore(File inputFile) throws Exception
	{
		System.out.println("PARSING: " + inputFile.getAbsolutePath());
		BufferedReader reader = 
			inputFile.getName().toUpperCase().endsWith("GZ") ?
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( inputFile) ) ))
				:
					new BufferedReader(new FileReader(inputFile));
			
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		
		
		for( String s= reader.readLine(); s != null; s = reader.readLine())
		{
			s = s.trim();
			
			if( s.endsWith("0 hits found"))
			{
				addToMap(counts, 0);
			}
			else if( s.endsWith("hits found"))
			{
				s = reader.readLine();
				StringTokenizer sToken = new StringTokenizer(s);
				
				for( int x=0; x < 11; x++)
					sToken.nextToken();
				
				addToMap(counts, Integer.parseInt(sToken.nextToken()));
				
				if( sToken.hasMoreTokens())
					throw new Exception("Unexpected token " + sToken.nextToken());
			}
		}
		
		reader.close();
		return counts;
	}
	
	private static void mapAndReduce( String inputPath, String outputPath)	throws Exception
	{
		HashMap<Integer, Integer> map = mapHitsToScore( new File( inputPath));
		
		File outFile = new File(outputPath);
		
		if(outFile.exists())
			throw new Exception("File already exists " + outFile.getAbsolutePath());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(  outputPath));
		
		writer.write("bitScore\tcounts\n");
		
		List<Integer> list = new ArrayList<Integer>(map.keySet());
		Collections.sort(list);
		Collections.reverse(list);
		
		for(Integer i : list)
			writer.write(i  +"\t" + map.get(i) + "\n");
		
		writer.flush();  writer.close();
	}
	
	private static void addToMap(HashMap<Integer, Integer> map, int bitScore)
	{
		Integer count = map.get(bitScore);
		
		if( count == null)
			count =0;
		
		count++;
		
		map.put(bitScore, count);
	}
	
	public static void main(String[] args) throws Exception
	{
		if( args.length != 2)
		{
			System.out.println("Usage inputfile outputFile");
			System.exit(1);
		}
		
		mapAndReduce(args[0], args[1]);
	}
}
