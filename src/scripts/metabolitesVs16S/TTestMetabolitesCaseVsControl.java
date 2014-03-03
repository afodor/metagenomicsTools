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
import java.util.StringTokenizer;

import utils.Avevar;
import utils.ConfigReader;
import utils.TTest;
import utils.TabReader;

public class TTestMetabolitesCaseVsControl
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMetabolitesCaseControl() + 
				File.separator + "sampleList.txt")));
		
		List<String> taxaNames= (getTaxaNames(reader.readLine()));
		HashMap<String, Holder> taxaMap = new LinkedHashMap<String, Holder>();
		
		for( String s : taxaNames)
		{
			Holder h = new Holder();
			h.taxaName = s;
			taxaMap.put(s, h);
		}
		
		populateMap(taxaNames, taxaMap, reader);
		addPValues(taxaMap, taxaNames);
		writeResults(taxaMap, taxaNames);
		
		reader.close();
	}
	
	
	
	static void populateMap( List<String> taxaNames, HashMap<String, Holder> map, BufferedReader reader )
		throws Exception
	{
		for( String s= reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			TabReader sToken = new TabReader(s);
			sToken.nextToken(); sToken.nextToken();
			String sample = sToken.nextToken().replaceAll("\"", "");
			System.out.println(sample);
			
			String caseControlString = sToken.nextToken().trim().replaceAll("\"", "");
			
			for( int x=0; x < taxaNames.size(); x++)
			{
				System.out.println("\t"+ taxaNames.get(x));
				HashMap<String, Double> innerMap = null;
				String taxa = taxaNames.get(x);
				Holder h = map.get(taxa);
				
				if( caseControlString.equals("case"))
				{
					innerMap = h.caseMap;
				}
				else if( caseControlString.equals("control"))
				{
					innerMap = h.controlMap;
				}
				else throw new Exception("NO " + caseControlString );
				
				if( innerMap.containsKey(sample))
					throw new Exception("duplicate sample  " + sample);
				
				String aVal = sToken.nextToken().trim().replaceAll("\"", "");
				
				if( aVal.length() > 0 )
					innerMap.put(sample, Double.parseDouble(aVal) );
			}
		}
	}
	
	static class Holder implements Comparable<Holder>
	{
		String taxaName;
		double pValue;
		HashMap<String, Double> caseMap = new HashMap<String,Double>();
		HashMap<String, Double> controlMap = new HashMap<String,Double>();
		
		@Override
		public int compareTo(Holder arg0)
		{
			return Double.compare(this.pValue, arg0.pValue);
		}
	}
	
	private static void addPValues( HashMap<String, Holder> map , List<String> taxaName)
	{
		for( String s : taxaName ) 
		{
			Holder h = map.get(s);
			List<Double> caseList = new ArrayList<Double>();
			List<Double> controlList = new ArrayList<Double>();
			
			for( String s2 : h.caseMap.keySet() )
				caseList.add(h.caseMap.get(s2));
			
			for( String s2 : h.controlMap.keySet())
				controlList.add(h.controlMap.get(s2));
			
			double pValue =1 ;
			
			try
			{
				pValue = TTest.ttestFromNumberUnequalVariance(caseList, controlList).getPValue();
			}
			catch(Exception ex)
			{
				
			}
			h.pValue = pValue;
		}
	}
	
	private static void writeResults( HashMap<String, Holder> map , List<String> taxaName) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMetabolitesCaseControl() +
				File.separator +  "Metaboloites_TTest.txt")));
		
		List<Holder> resultsList = new ArrayList<Holder>(map.values());
		Collections.sort(resultsList);
		
		writer.write("metabolite\tcaseAverage\tcontrolAverage\tcaseControlRatio\tpValue\tbhCorrectedPValue\n");
		
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
			
			double ratio = caseAvg / controlAvg ;
			
			if( ratio < 1)
				ratio = -1/ratio;
			
			writer.write( ratio + "\t");
			
			
				
			writer.write(h.pValue + "\t");
			writer.write(h.pValue * resultsList.size() / index + "\n");
			index++;
			
		} 
		
		writer.flush();  writer.close();
	}
	
	static List<String> getTaxaNames(String headerLine) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		StringTokenizer sToken = new StringTokenizer(headerLine, "\t");
		
		sToken.nextToken(); sToken.nextToken(); sToken.nextToken(); sToken.nextToken();
		
		while( sToken.hasMoreTokens())
			list.add(sToken.nextToken().replaceAll("\"", ""));
			
		return list;
	}
}
