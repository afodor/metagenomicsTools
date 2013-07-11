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

import java.util.HashMap;
import java.util.List;

import parsers.SnpResultFileLine;

import fileAbstractions.FileUtils;
import fileAbstractions.PairedReads;

public class CountSNPs
{
	public static double MIN_PVALUE= 0.05;
	
	public static void main(String[] args) throws Exception
	{
		// what SNPs are present in all 4 comparisons ?
		List<PairedReads> pairedList = RunAll.getAllBurkholderiaPairs();
		
		for( int x=0; x < pairedList.size()-1; x++)
		{
			PairedReads prX = pairedList.get(x);
			
			for( int y=x+1; y < pairedList.size(); y++)
			{
				try
				{
					PairedReads prY = pairedList.get(y);
					
					System.out.println(prX.getFirstRead() + " "+ prY.getFirstRead());
					
					HashMap<Long, SnpResultFileLine> map1 = SnpResultFileLine.parseFile(
						FileUtils.getSNPResultsFile(prX.getFirstRead(), prY.getFirstRead())	);
					map1 = SnpResultFileLine.filter(map1, MIN_PVALUE);
					

					HashMap<Long, SnpResultFileLine> map2 = SnpResultFileLine.parseFile(
						FileUtils.getSNPResultsFile(prX.getFirstRead(), prY.getSecondRead())	);
					map2 = SnpResultFileLine.filter(map2, MIN_PVALUE);
					

					HashMap<Long, SnpResultFileLine> map3= SnpResultFileLine.parseFile(
						FileUtils.getSNPResultsFile(prX.getSecondRead(), prY.getFirstRead())	);
					map3 = SnpResultFileLine.filter(map3, MIN_PVALUE);
					

					HashMap<Long, SnpResultFileLine> map4= SnpResultFileLine.parseFile(
						FileUtils.getSNPResultsFile(prX.getSecondRead(), prY.getFirstRead())	);
					map4 = SnpResultFileLine.filter(map4, MIN_PVALUE);
					
					System.out.println(map1.size() +  " " + map2.size() + " " + map3.size() + " " + map4.size());
					System.exit(1);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
	}
}
