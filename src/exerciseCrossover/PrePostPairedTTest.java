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
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;


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
		HashSet<Integer> placeboSet = new HashSet<Integer>();
		HashMap<String, List<Double>> sampleMap = new HashMap<String, List<Double>>();
		
		
	}
}
