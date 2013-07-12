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


import parsers.SnpResultFileLine;
import utils.ConfigReader;

import fileAbstractions.FileUtils;
import fileAbstractions.PairedReads;

public class CountSNPs
{
	public static double MIN_PVALUE= 0.05;
	
	public static String getAssignment(String s) throws Exception
	{
		HashSet<String> sensitive = new HashSet<>();
		sensitive.add("137");sensitive.add("139");sensitive.add("142");sensitive.add("144");
		sensitive.add("149");sensitive.add("150");
		
		for(String s2 : sensitive)
			if(s.indexOf(s2)!=-1)
				return "sensitive";
	
		HashSet<String> resistant = new HashSet<>();
		
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
		List<String> list = new ArrayList<>();
		list.add(getAssignment(s1));
		list.add(getAssignment(s2));
		Collections.sort(list);
		
		return list.get(0) + "_TO_" + list.get(1);
		
	}
	
	public static void main(String[] args) throws Exception
	{	
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator +  "summary" + File.separator +
				"summary.txt")));
		writer.write("fileA\tfileB\tnumInCommon\tkey\n");
		
		List<PairedReads> pairedList = RunAll.getAllBurkholderiaPairs();
		for(int x=0; x < pairedList.size()-1; x++)
		{
			PairedReads prx = pairedList.get(x);
			
			if( FileUtils.getCountsFile(prx.getFirstRead()).exists() && 
					FileUtils.getCountsFile(prx.getSecondRead()).exists())
			{
				for( int y=x+1; y < pairedList.size(); y++)
				{
					PairedReads pry = pairedList.get(y);
					
					if( FileUtils.getCountsFile(pry.getFirstRead()).exists() 
							&& FileUtils.getCountsFile(pry.getSecondRead()).exists())
					{
						HashMap<Long, SnpResultFileLine> map1 = SnpResultFileLine.parseFile(
								FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getFirstRead())
														, FileUtils.getCountsFile(pry.getFirstRead()))	);
						map1 = SnpResultFileLine.filter(map1, MIN_PVALUE);
							

						HashMap<Long, SnpResultFileLine> map2 = SnpResultFileLine.parseFile(
									FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getFirstRead())
															, FileUtils.getCountsFile(pry.getSecondRead()))	);
						map2 = SnpResultFileLine.filter(map2, MIN_PVALUE);
							

						HashMap<Long, SnpResultFileLine> map3 = SnpResultFileLine.parseFile(
										FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getSecondRead())
																, FileUtils.getCountsFile(pry.getFirstRead()))	);
						map3 = SnpResultFileLine.filter(map3, MIN_PVALUE);
						

						HashMap<Long, SnpResultFileLine> map4 = SnpResultFileLine.parseFile(
										FileUtils.getSNPResultsFile(  FileUtils.getCountsFile(prx.getSecondRead())
																, FileUtils.getCountsFile(pry.getSecondRead()))	);
						map4 = SnpResultFileLine.filter(map4, MIN_PVALUE);
						
						HashSet<Long> commonLongs = new HashSet<>();
						commonLongs.addAll(map1.keySet());
						
						commonLongs.retainAll(map2.keySet());
						commonLongs.retainAll(map3.keySet());
						commonLongs.retainAll(map4.keySet());

						System.out.println(map1.size() +  " " + map2.size() + " " + map3.size() + " " 
								+ map4.size() + " " + commonLongs.size());
						
						writer.write(prx.getFirstRead().getName() + "\t" + 
										pry.getSecondRead().getName() + "\t" + 
											commonLongs.size() + "\t" + 
												getCode(prx.getFirstRead().getName(), pry.getSecondRead().getName()) + "\n"	);
						writer.flush();
						

					}										
				}
			}
		}
		
		writer.close();
	}
}
