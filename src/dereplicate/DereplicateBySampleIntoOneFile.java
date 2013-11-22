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


package dereplicate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class DereplicateBySampleIntoOneFile
{
	/*
	 * The key for the outer map is the sequence.
	 * The key for the inner map is the sample.
	 */
	private static HashMap<String, HashMap<String, Integer>> pivotToMap(String filePath)
		throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String,Integer>>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(filePath);
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
		{
			HashMap<String, Integer> innerMap = map.get(fs.getSequence());
			
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Integer>();
				map.put(fs.getSequence(), innerMap);
			}
			
			String sampleId = new StringTokenizer(fs.getFirstTokenOfHeader(), "_").nextToken();
			Integer count = innerMap.get(sampleId);
			if( count ==null)
				count =0;
			count++;
			innerMap.put(sampleId, count);
		}
		
		return map;
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String sequence;
		HashMap<String, Integer> map;
		int count=0;
		
		@Override
		public int compareTo(Holder o)
		{
			return o.count - this.count;
		}
	}
	
	private static void writeResults(String outFilePath, HashMap<String, HashMap<String, Integer>> map) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		for(String s : map.keySet())
		{
			Holder h = new Holder();
			h.sequence = s;
			h.map = map.get(s);
			
			for( String sample : h.map.keySet())
				h.count += h.map.get(sample);
			
			list.add(h);
		}
		
		Collections.sort(list);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFilePath)));
		
		int id =1;
		
		for(Holder h : list)
		{
			writer.write(">" + id + ";size=" + h.count + " "  + h.map.toString() + "\n");
			id++;
			writer.write(h.sequence+ "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = pivotToMap(ConfigReader.getETreeTestDir() 
				+ File.separator + "gastro454DataSet" + File.separator + "mels74samples.fna");
		
		writeResults(ConfigReader.getETreeTestDir() 
				+ File.separator + "gastro454DataSet" + File.separator + "mel74DerupLumped.fna", map);
	}
}
