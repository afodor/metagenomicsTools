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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TTest;


public class PrePostPairedTTest
{
	private static HashSet<Integer> getPlaceboSet()
	{
		HashSet<Integer> set =new HashSet<Integer>();
		
		set.add(2);
		set.add(4);
		set.add(6);
		set.add(9);
		set.add(11);
		set.add(14);
		set.add(15);
		set.add(18);
		set.add(19);	
		set.add(20);
		set.add(21);
		set.add(25);
		
		return set;
	}
	
	private static HashMap<String, List<Double>> getsampleToPCOAMap() throws Exception
	{
		HashMap<String, List<Double>> map = new LinkedHashMap<String, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getCrossoverExerciseDir() + File.separator + 
				"mergedPCOA.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			for( int x=0; x < 7; x++)
				sToken.nextToken();
			
			String key = sToken.nextToken();
			List<Double> list = new ArrayList<Double>();
			
			if( map.containsKey(key))
				throw new Exception("No");
			map.put(key, list);
			
			sToken.nextToken(); sToken.nextToken();
			
			while(sToken.hasMoreTokens())
				list.add(Double.parseDouble(sToken.nextToken()));
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<Integer> placeboSet = getPlaceboSet();
		HashMap<String, List<Double>> sampleMap = getsampleToPCOAMap();
		System.out.println(sampleMap);
		
		BufferedWriter writer =  new BufferedWriter(new FileWriter(new File(ConfigReader.getCrossoverExerciseDir() +
				File.separator + "pairedTTestTreatment.txt")));
		
		writer.write("axis\tlog10PValue\tsampleSize\tpre\tpost\n");
		
		
		for( int pcoaAxis=0; pcoaAxis<=5; pcoaAxis++)
		{
			List<Double> pre = new ArrayList<Double>();
			List<Double> post = new ArrayList<Double>();
			
			for(int x=1; x<=27;x++) if ( x != 8)
			{
				if(placeboSet.contains(x))
				{
					List<Double> aPre = sampleMap.get(x + "A");
					List<Double> aPost = sampleMap.get(x + "B");
					
					if( aPre != null && aPost != null)
					{
						pre.add( aPre.get(pcoaAxis) );
						post.add( aPost.get(pcoaAxis) );
					}
				}
				else
				{
					List<Double> aPre = sampleMap.get(x + "D");
					List<Double> aPost = sampleMap.get(x + "E");
					
					if( aPre != null && aPost != null)
					{
						pre.add( aPre.get(pcoaAxis) );
						post.add( aPost.get(pcoaAxis) );
					}
				}
			}
			
			writer.write((pcoaAxis+1) + "\t");
			writer.write( TTest.pairedTTest(pre, post).getPValue() + "\t" );
			
			if(pre.size() != post.size())
				throw new Exception("Logic error");
			writer.write(pre.size() + "\t");
			writer.write(pre + "\t");
			writer.write(post + "\n");
			
		}
		
		writer.flush(); writer.close();
	
	}
		
}
