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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

/*
 * Run MergeMap first
 */
public class MedianOfCage
{	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<Double>> map = getMap();
		HashMap<String, String> sachMap = getSachmap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getSaccharineRatDir() +
				File.separator + "cecumCageGroup2mergedMapUnweightedPCOA.txt")));
				//+ "cecumCageGroup2PcoaUnweigthedUnifrac.txt")));
		
		writer.write("cage\tgroup\tmedian\n");
		
		for(String s : map.keySet())
		{
			writer.write(s + "\t");
			writer.write(sachMap.get(s) + "\t");
			writer.write( getMedian(map.get(s)) + "\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static HashMap<String, String> getSachmap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator 
				+ "mergedMapUnweightedPCOA.txt"))); 
				//"mergedMapUnweightedUnifrac.txt"))); 
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[3].equals("\"CECUM\"") )
			{
				String cage = splits[8];
				
				String group = splits[7];
				
				String oldGroup = map.get(cage);
				
				if( oldGroup == null)
				{
					oldGroup = group;
					map.put(cage, group);
				}
				
				if( ! group.equals(oldGroup))
					throw new Exception("No");
			}
		}
		
		if( map.size() == 0 )
			throw new Exception("No");
		
		
		
		return map;
	}
	
	public static double getMedian(List<Double> list) throws Exception
	{
		Collections.sort(list);
		
		if(list.size() ==0)
			throw new Exception("No");
		
		if(list.size() % 2 == 0)
		{
			int index = list.size() / 2;
			return (list.get(index-1) + list.get(index) ) / 2.0;
		}
		
		return list.get(list.size()/2);
	}
	
	
	/*
	 * Outer key is cage - value is list for 2nd PCA component . 
	 */
	private static HashMap<String, List<Double>> getMap() throws Exception
	{
		HashMap<String, List<Double>> map = new HashMap<String, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				"mergedMapUnweightedPCOA.txt")));
				//"mergedMapUnweightedUnifrac.txt"))); 
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			System.out.println(s);
			String[] splits = s.split("\t");
			//System.out.println(splits[3]);
			
			if( splits[3].equals("\"CECUM\"") )
			{
				String cage = splits[8];
				
				List<Double> list = map.get(cage);
				
				if( list == null)
				{
					list = new ArrayList<Double>();
					map.put(cage, list);
				}
			
				list.add(Double.parseDouble(splits[25]));
			}
		}
		
		reader.close();
		
		return map;
	}
}
