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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import coPhylog.CoPhylogBinaryFileReader;
import coPhylog.ContextCount;

import utils.ConfigReader;

import fileAbstractions.FileUtils;
import fileAbstractions.PairedReads;

public class RunAll
{
	public static final double B_VALUE = 5.0;

	private static void dumpMemoryInfo() 
	{
		double fractionFree= 1- (Runtime.getRuntime().totalMemory()- ((double)Runtime.getRuntime().freeMemory() ))
				/Runtime.getRuntime().totalMemory();

		System.out.println("fraction Free= " + fractionFree);

		double fractionAllocated = 1-  (Runtime.getRuntime().maxMemory()- ((double)Runtime.getRuntime().totalMemory() ))
				/Runtime.getRuntime().maxMemory();

		System.out.println("fraction allocated = " + fractionAllocated);

	}
	
	// todo: Make out map with weak references.
	private static HashMap<Long, ContextCount> getFromCache( File countFile, HashMap<String, HashMap<Long, ContextCount>> outerMap ) throws Exception
	{
		HashMap<Long, ContextCount> returnMap = outerMap.get(countFile.getName());
		
		if( returnMap == null)
		{
			returnMap = CoPhylogBinaryFileReader.readBinaryFile(countFile);
			
			// keep the chache empty until we can do weak references
			//outerMap.put(countFile.getName(), returnMap);
			dumpMemoryInfo();
		}
		
		return returnMap;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<Long, ContextCount>> outerMap = new HashMap<>();
		List<PairedReads> pairedList = getAllBurkholderiaPairs();
		
		for(int x=0; x < pairedList.size(); x++)
		{
			PairedReads pr = pairedList.get(x);
			
			File countFile1 = FileUtils.getCountsFile(pr.getFirstRead());
			File countFile2 = FileUtils.getCountsFile(pr.getSecondRead());
			
			if( countFile1.exists() && countFile2.exists())
			{
				System.out.println("MATCH: " + countFile1.getName());
				HashMap<Long, ContextCount> map1 = getFromCache(countFile1, outerMap);
				HashMap<Long, ContextCount> map2 = getFromCache(countFile1, outerMap);
				
				ApplyWeightedChiSquare.writePValues(B_VALUE, map1, map2, FileUtils.getSNPResultsFile(countFile1, countFile2));
				
				for( int y=x+1; y < pairedList.size(); y++)
				{
					PairedReads pr2 = pairedList.get(y);
					File countFile2_1 = FileUtils.getCountsFile(pr2.getFirstRead());
					File countFile2_2 = FileUtils.getCountsFile(pr2.getSecondRead());
					
					if( countFile2_1.exists() && countFile2_2.exists())
					{
						HashMap<Long, ContextCount> map2_1 = getFromCache(countFile2_1, outerMap);
						HashMap<Long, ContextCount> map2_2 = getFromCache(countFile2_2, outerMap);
						
						ApplyWeightedChiSquare.writePValues(B_VALUE, map1, map2_1, FileUtils.getSNPResultsFile(countFile1, countFile2_1));
						ApplyWeightedChiSquare.writePValues(B_VALUE, map1, map2_2, FileUtils.getSNPResultsFile(countFile1, countFile2_2));
						ApplyWeightedChiSquare.writePValues(B_VALUE, map2, map2_1, FileUtils.getSNPResultsFile(countFile2, countFile2_1));
						ApplyWeightedChiSquare.writePValues(B_VALUE, map2, map2_2, FileUtils.getSNPResultsFile(countFile2, countFile2_2));
					}
				}
			}
				
		}
	}
	
	public static List<PairedReads> getAllBurkholderiaPairs() throws Exception
	{
		List<PairedReads> list = new ArrayList<>();
		
		File dir = new File(ConfigReader.getBurkholderiaDir());
		
		String[] files= dir.list();
		List<String> fileNames = new ArrayList<>();
		
		for( String s : files)
		{
			if( s.endsWith(".gz") )
				fileNames.add(s);
		}
		
		for( int x=0; x < fileNames.size(); x++)
		{
			list.add( makePairedReads( dir.getAbsolutePath() + File.separator +  fileNames.get(x), 
						dir.getAbsolutePath() + File.separator + fileNames.get(x+1)) );
			x++;
		}
		
		for(PairedReads pr : list)
		{
			StringTokenizer sToken1= new StringTokenizer(pr.getFirstRead().getName(), "_");
			StringTokenizer sToken2 = new StringTokenizer(pr.getSecondRead().getName(), "_");
			
			String s1 = sToken1.nextToken() + "_" + sToken1.nextToken();
			String s2 = sToken2.nextToken() + "_" + sToken2.nextToken();
			//System.out.println(s1 + " " + s2);
			
			if( ! s1.equals(s2))
				throw new Exception("No ");
		}
			
		return list;
	}
	
	
	private static PairedReads makePairedReads(final String filepath1, final String filepath2) throws Exception
	{
		return new PairedReads()
		{
			
			@Override
			public File getSecondRead()
			{
				return new File(filepath2);
			}
			
			@Override
			public File getFirstRead()
			{
				return new File(filepath1);
			}
		};
	}
}
