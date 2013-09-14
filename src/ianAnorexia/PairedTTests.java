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


package ianAnorexia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;
import utils.TTest;

public class PairedTTests
{
	private static class Holder
	{
		Double time1 = null;
		Double time2 = null;
	}
	
	private static List<FileManager> getFileManagers() throws Exception
	{
		List<FileManager> list = new ArrayList<FileManager>();
		
		list.add(new FileManager(
				ConfigReader.getIanAnorexiaDir() + File.separator + "unweightedUNIFRAC.txt",
				ConfigReader.getIanAnorexiaDir() + File.separator + 
				"unweightedUNIFRACPairedTTests.txt"));
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( FileManager fm : getFileManagers())
			runPairedTTest(fm);
	}
	
	private static void runPairedTTest(FileManager fm) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fm.getPairedTTestFile()));
		
		writer.write("axis\tpairedPValue\n");
		
		File inFile = fm.getDataFile();
		
		for( int x=2; x < 12; x++)
		{
			writer.write((x-1) + "\t");
			
			HashMap<Integer, Holder> map = getMap(inFile, x);
			
			writer.write(getPairedTTest(map) + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static double getPairedTTest(HashMap<Integer, Holder> map  ) throws Exception
	{
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();
		
		for(Integer i  : map.keySet())
		{
			Holder h = map.get(i);
			
			if( h.time1 != null && h.time2 != null )
			{
				list1.add(h.time1);
				list2.add(h.time2);
			}
		}
		
		return TTest.pairedTTest(list1, list2).getPValue();
	}
	
	private static HashMap<Integer, Holder> getMap(File file, int dataColNum) throws Exception
	{
		HashMap<Integer, Holder> map = new HashMap<Integer, Holder>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Integer patientId = Integer.parseInt(splits[0]);
			
			Holder h = map.get(patientId);
			
			if( h == null)
			{
				h = new Holder();
				map.put(patientId, h);
			}
			
			int time = Integer.parseInt(splits[1]);
			
			if( time == 1)
			{
				if( h.time1 != null)
					throw new Exception("no");
				
				h.time1 = Double.parseDouble(splits[dataColNum]);
			}
			else if (time == 2) 
			{
				if( h.time2 != null)
					throw new Exception("no");
				
				h.time2 = Double.parseDouble(splits[dataColNum]);
			}
			else throw new Exception("No");
		}
		
		reader.close();
		return map;
	}
}
