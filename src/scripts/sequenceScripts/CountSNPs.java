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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;


import parsers.SnpResultFileLine;
import utils.ConfigReader;

import fileAbstractions.FileUtils;
import fileAbstractions.PairedReads;

public class CountSNPs
{
	public static double MIN_PVALUE= 0.05;
	
	public static int getYear(String s) throws Exception
	{
		HashSet<String> s2010 = new HashSet<String>();
		s2010.add("136");  s2010.add("137");  s2010.add("138");  s2010.add("139");  
		s2010.add("130");  s2010.add("131");  s2010.add("132");  s2010.add("133");  s2010.add("134");

		for(String s2 : s2010)
			if(s.indexOf(s2)!=-1)
				return 2010;
		
		HashSet<String> s2009 = new HashSet<String>();
		
		s2009.add("142");  s2009.add("143");  s2009.add("144");  s2009.add("147");  
		
		for(String s2 : s2009)
			if(s.indexOf(s2)!=-1)
				return 2009;
		
		HashSet<String> s2011 = new HashSet<String>();
		
		s2011.add("148"); s2011.add("149"); s2011.add("150"); s2011.add("152");
		s2011.add("154"); s2011.add("155"); s2011.add("156"); s2011.add("157");s2011.add("158");
		
		
		for(String s2 : s2011)
			if(s.indexOf(s2)!=-1)
				return 2011;
		
		throw new Exception("Could not find " + s);
	}
	
	
	public static String getAssignment(String s) throws Exception
	{
		HashSet<String> sensitive = new HashSet<String>();
		sensitive.add("137");sensitive.add("139");sensitive.add("142");sensitive.add("144");
		sensitive.add("149");sensitive.add("150");
		
		for(String s2 : sensitive)
			if(s.indexOf(s2)!=-1)
				return "sensitive";
	
		HashSet<String> resistant = new HashSet<String>();
		
		resistant.add("130"); 
		resistant.add("131"); 
		resistant.add("132");
		resistant.add("133"); 
		resistant.add("154");
		resistant.add("155");
		resistant.add("158"); 
		
		for(String s2 : resistant)
			if(s.indexOf(s2)!=-1)
				return "resistant";
		
		throw new Exception("Could not find " + s);
	}
	
	public static String getCode(String s1,String s2) throws Exception
	{
		List<String> list = new ArrayList<String>();
		list.add(getAssignment(s1));
		list.add(getAssignment(s2));
		Collections.sort(list);
		
		return list.get(0) + "_TO_" + list.get(1);
		
	}
	

	public static String getYears(String s1,String s2) throws Exception
	{
		List<String> list = new ArrayList<String>();
		list.add("" + getYear(s1));
		list.add("" + getYear(s2));
		Collections.sort(list);
		
		return list.get(0) + "_TO_" + list.get(1);
		
	}
	
	//This has dependencies on CoPhylogOnBurk and then GenerateDistances and the DoAllBurkComparisons
	public static void main(String[] args) throws Exception
	{	
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator +  "summary" + File.separator +
				"summary.txt")));
		writer.write("fileA\tfileB\tnumInCommon\tkey\ttime\n");
		
		BufferedWriter detailedWriter = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator + "summary" + File.separator + 
				"details.txt"	)));
		
		detailedWriter.write("fileA\tfileB\tnumInCommon\tkey\ttime\tlongID\taCounts1\taCounts2\taCounts3\taCounts4\tbCounts1\tbCounts2\tbCounts3\tbCounts4\tconsensusA\tconsensusB\n");
		
		List<PairedReads> pairedList = DoAllBurkComparisons.getAllBurkholderiaPairs();
		for(int x=0; x < pairedList.size()-1; x++)
		{
			PairedReads prx = pairedList.get(x);
			
			if( FileUtils.getCountsFile(prx.getFirstFileName()).exists() && 
					FileUtils.getCountsFile(prx.getSecondFileName()).exists())
			{
				for( int y=x+1; y < pairedList.size(); y++)
				{
					PairedReads pry = pairedList.get(y);
					
					if( FileUtils.getCountsFile(pry.getFirstFileName()).exists() 
							&& FileUtils.getCountsFile(pry.getSecondFileName()).exists())
					{
						HashMap<Long, SnpResultFileLine> map1 = SnpResultFileLine.parseFile(
								FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getFirstFileName())
														, FileUtils.getCountsFile(pry.getFirstFileName()))	);
						map1 = SnpResultFileLine.filter(map1, MIN_PVALUE);
							

						HashMap<Long, SnpResultFileLine> map2 = SnpResultFileLine.parseFile(
									FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getFirstFileName())
															, FileUtils.getCountsFile(pry.getSecondFileName()))	);
						map2 = SnpResultFileLine.filter(map2, MIN_PVALUE);
							

						HashMap<Long, SnpResultFileLine> map3 = SnpResultFileLine.parseFile(
										FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getSecondFileName())
																, FileUtils.getCountsFile(pry.getFirstFileName()))	);
						map3 = SnpResultFileLine.filter(map3, MIN_PVALUE);
						

						HashMap<Long, SnpResultFileLine> map4 = SnpResultFileLine.parseFile(
										FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getSecondFileName())
																, FileUtils.getCountsFile(pry.getSecondFileName()))	);
						map4 = SnpResultFileLine.filter(map4, MIN_PVALUE);
						
						HashSet<Long> commonLongs = new HashSet<Long>();
						commonLongs.addAll(map1.keySet());
						
						commonLongs.retainAll(map2.keySet());
						commonLongs.retainAll(map3.keySet());
						commonLongs.retainAll(map4.keySet());

						System.out.println(map1.size() +  " " + map2.size() + " " + map3.size() + " " 
								+ map4.size() + " " + commonLongs.size());
						
						writer.write(prx.getFirstFileName().getName() + "\t" + 
										pry.getSecondFileName().getName() + "\t" + 
											commonLongs.size() + "\t" + 
												getCode(prx.getFirstFileName().getName(), pry.getSecondFileName().getName()) + "\t"	
												 + getYears(prx.getFirstFileName().getName(), pry.getSecondFileName().getName()) +  "\n");
						
						for(Long longID : commonLongs)
						{
							detailedWriter.write(prx.getFirstFileName().getName() + "\t" + 
									pry.getSecondFileName().getName() + "\t" + 
										commonLongs.size() + "\t" + 
											getCode(prx.getFirstFileName().getName(), pry.getSecondFileName().getName()) + "\t"	
											 + getYears(prx.getFirstFileName().getName(), pry.getSecondFileName().getName()) + "\t");
							
							detailedWriter.write(longID +"\t" );
							detailedWriter.write(map1.get(	longID).getCounts1() + "\t");
							detailedWriter.write(map2.get(	longID).getCounts1() + "\t");
							detailedWriter.write(map3.get(	longID).getCounts1() + "\t");
							detailedWriter.write(map4.get(	longID).getCounts1() + "\t");
							
							detailedWriter.write(map1.get(	longID).getCounts2() + "\t");
							detailedWriter.write(map2.get(	longID).getCounts2() + "\t");
							detailedWriter.write(map3.get(	longID).getCounts2() + "\t");
							detailedWriter.write(map4.get(	longID).getCounts2() + "\t");
							detailedWriter.write(getConsensus(longID, map1, map2, map3, map4, true) + "\t");
							detailedWriter.write(getConsensus(longID, map1, map2, map3, map4, false) + "\n");
							detailedWriter.flush();
					
						}
						
						writer.flush();
						

					}										
				}
			}
		}
		
		detailedWriter.flush();  detailedWriter.close();
		writer.flush(); writer.close();
	}
	
	private static TreeSet<Character> getConsensus( long aLong, HashMap<Long, SnpResultFileLine> map1,  
			HashMap<Long, SnpResultFileLine> map2, 
				HashMap<Long, SnpResultFileLine> map3, HashMap<Long, SnpResultFileLine> map4, boolean first)
					throws Exception
	{
		TreeSet<Character> set = new TreeSet<Character>();
	
		
		set.addAll(map1.get(aLong).getContextCount(first).getHighest());
		set.addAll(map2.get(aLong).getContextCount(first).getHighest());
		set.addAll(map3.get(aLong).getContextCount(first).getHighest());
		set.addAll(map4.get(aLong).getContextCount(first).getHighest());
		
		return set;
		
	}
}
