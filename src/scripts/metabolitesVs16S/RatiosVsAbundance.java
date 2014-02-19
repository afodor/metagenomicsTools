/** 
 * Author:  anthony.fodor@gmail.com
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

package scripts.metabolitesVs16S;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.Avevar;
import utils.ConfigReader;

import scripts.metabolitesVs16S.TTestsCaseVsControl.Holder;

public class RatiosVsAbundance
{
	public static void main(String[] args) throws Exception
	{
		String[] vals = { "fam" , "gen", "ord", "phy" , "cls", "counts" };
		
		for(String s : vals)
			runALevel(s);
		
	}
	
	public static void runALevel(String level) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMetabolitesCaseControl() + 
				File.separator + "topeFeb2014_raw_" + level + ".txt")));
		
		List<String> taxaNames= ( TTestsCaseVsControl.getTaxaNames(reader.readLine()));
		HashMap<String, Holder> taxaMap = new LinkedHashMap<String, Holder>();
		
		for( String s : taxaNames)
		{
			Holder h = new Holder();
			h.taxaName = s;
			taxaMap.put(s, h);
		}
		
		TTestsCaseVsControl.populateMap(taxaNames, taxaMap, reader);
		normalizeMap(taxaMap);
		//writeResults(taxaMap, taxaNames,level);
		
		reader.close();
	}
	
	private static void addCountsToMap(HashMap<String, Double> countMap,
				HashMap<String, Double> innerMap, String taxaID)
	{
		for( String sampleID : innerMap.keySet())
		{
			double innerCounts= innerMap.get(sampleID);
			
			Double sumCounts = countMap.get(sampleID);
			
			if( sumCounts == null)
				sumCounts = 0.0;
			
			sumCounts += innerCounts;
			
			countMap.put(sampleID, sumCounts);
		}
	}
	
	private static void normalizeMap( HashMap<String, Holder> map ) throws Exception
	{
		// key is sampleID
		HashMap<String, Double> countMap = new HashMap<String, Double>();
		
		for( String taxaId : map.keySet())
		{
			addCountsToMap(countMap, map.get(taxaId).caseMap, taxaId);
			addCountsToMap(countMap, map.get(taxaId).controlMap, taxaId);
		}
		
		for( String taxaId : map.keySet())
		{
			for(String s : map.get(taxaId).caseMap.keySet())
			{
				double oldVal = map.get(taxaId).caseMap.get(s);
				oldVal = oldVal / countMap.get(s);
				map.get(taxaId).caseMap.put(s, oldVal);
			}
			
			for(String s : map.get(taxaId).controlMap.keySet())
			{
				double oldVal = map.get(taxaId).controlMap.get(s);
				oldVal = oldVal / countMap.get(s);
				map.get(taxaId).controlMap.put(s, oldVal);
			}
		}
		
		countMap = new HashMap<String, Double>();
		for( String taxaId : map.keySet())
		{
			addCountsToMap(countMap, map.get(taxaId).caseMap, taxaId);
			addCountsToMap(countMap, map.get(taxaId).controlMap, taxaId);
		}
		
		for( Double d : countMap.values())
			if( Math.abs(d - 1) > 0.0001)
				throw new Exception("No");
	}
	
	private static void writeResults( HashMap<String, Holder> map , List<String> taxaName, String level ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMetabolitesCaseControl() +
				File.separator +  level + "TTest.txt")));
		
		List<Holder> resultsList = new ArrayList<Holder>(map.values());
		Collections.sort(resultsList);
		
		writer.write(level + "\tcaseAverage\tcontrolAverage\tcaseControlRatio\tpValue\tbhCorrectedPValue\n");
		
		int index =1;
		for( Holder h : resultsList) 
		{
			writer.write(h.taxaName + "\t");
			List<Double> caseList = new ArrayList<Double>();
			List<Double> controlList = new ArrayList<Double>();
			
			for( String s2 : h.caseMap.keySet() )
				caseList.add(h.caseMap.get(s2));
			
			for( String s2 : h.controlMap.keySet())
				controlList.add(h.controlMap.get(s2));
			
			double caseAvg = new Avevar(caseList).getAve() ;
			writer.write( caseAvg+ "\t");
			
			double controlAvg = new Avevar(controlList).getAve();
			writer.write(controlAvg+ "\t");
			writer.write( caseAvg / controlAvg + "\t");
				
			writer.write(h.pValue + "\t");
			writer.write(h.pValue * resultsList.size() / index + "\n");
			index++;
			
		} 
		
		writer.flush();  writer.close();
	}
}
