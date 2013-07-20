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


package scripts.sequenceScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import coPhylog.CoPhylogBinaryFileReader;
import coPhylog.ContextCount;

import fileAbstractions.FileUtils;
import fileAbstractions.PairedReads;

import utils.ConfigReader;

/*
 * This has dependencies on CountSNPs
 */
public class CreateSNPPivotTable
{
	
	public static void main(String[] args) throws Exception
	{
		HashSet<Long> ids = getSnpIds();
		System.out.println(ids.size());
		
		HashMap<Long,HashMap<String,Holder>> map = getPivotMap(ids);
		writeResults(map);
		
		
	}
	
	private static void writeResults(HashMap<Long,HashMap<String,Holder>> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBurkholderiaDir()+
				File.separator + "summary" + File.separator + "pivotedSNPS.txt")));
		
		HashSet<String> strains= new HashSet<String>();
		
		for( Long aLong : map.keySet() )
		{
			HashMap<String, Holder> innerMap = map.get(aLong);
			
			for(String s : innerMap.keySet())
				strains.add(s);
		}
		
		List<String> strainList = new ArrayList<String>();
		
		for(String s : strains)
			strainList.add(s);
		
		Collections.sort(strainList);
		
		writer.write("key");
		
		for(String s : strainList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( Long aLong : map.keySet() )
		{
			HashMap<String, Holder> innerMap = map.get(aLong);
			
			writer.write("" + aLong);
			
			for(String s: strainList)
				writer.write("\t"+ innerMap.get(s));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<Long,HashMap<String,Holder>> getPivotMap(HashSet<Long> ids)
		throws Exception
	{
		HashMap<Long,HashMap<String,Holder>> map = new HashMap<Long,HashMap<String,Holder>>();
		
		List<PairedReads> pairedList = DoAllBurkComparisons.getAllBurkholderiaPairs();
		System.out.println(pairedList.size());
		
		for(PairedReads pr : pairedList)
		{
			String strainName = FileUtils.getCommonNameForPairs(pr);
			System.out.println(strainName + " "+ pr.getFirstFileName() + " " + pr.getSecondFileName());
			addToMap(map, pr.getFirstFileName(), strainName, ids);
			addToMap(map, pr.getSecondFileName(), strainName, ids);
		}		
		
		return map;
	}
	
	private static void addToMap(HashMap<Long,HashMap<String,Holder>> map, File sequenceFile, 
			String strainName, HashSet<Long> includedIds) throws Exception
	{
		HashMap<Long, ContextCount> fileMap = 
				CoPhylogBinaryFileReader.readBinaryFile(FileUtils.getCountsFile(sequenceFile));
		
		for(Long aLong : fileMap.keySet())
			if( includedIds.contains(aLong))
			{
				ContextCount cc = fileMap.get(aLong);
				
				HashMap<String, Holder> innerMap = map.get(aLong);
				
				if( innerMap == null)
				{
					innerMap = new HashMap<String,Holder>();
					
					map.put( aLong, innerMap);
				}
				
				Holder h = innerMap.get(strainName);
				
				if( h == null)
				{
					h = new Holder();
					innerMap.put(strainName, h);
				}
				
				h.a += cc.getNumA();
				h.c += cc.getNumC();
				h.g += cc.getNumG();
				h.t+= cc.getNumT();
			}
	}
	
	private static class Holder
	{
		int a=0;
		int c=0;
		int g=0;
		int t=0;
		
		@Override
		public String toString()
		{
			return "[" + a + "," + c + "," + g + "," + t + "]";
		}
	}
	
	private static HashSet<Long> getSnpIds() throws Exception
	{
		HashSet<Long> set = new HashSet<Long>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator + "summary" +
					File.separator + "details.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			set.add(Long.parseLong(s.split("\t")[5]));
		}
		
		return set;
	}
}
