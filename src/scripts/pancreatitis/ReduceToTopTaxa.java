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


package scripts.pancreatitis;

import java.io.File;
import java.util.HashMap;

import parsers.OtuWrapper;

import utils.ConfigReader;

public class ReduceToTopTaxa
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(
			ConfigReader.getPancreatitisDir() + File.separator + "erinHannaHuman_raw_phyForR.txt");
		
		HashMap<String, Double> taxaMap = 
				wrapper.getTaxaListSortedByNumberOfCounts();
		
		for(String s : taxaMap.keySet())
			System.out.println(s + " " + taxaMap.get(s));
		
		int numToInclude = 6;
		
		File outFile = new File(ConfigReader.getPancreatitisDir() + File.separator + 
				"phylaTop6TaxaAsColumns.txt");
		
		wrapper.writeReducedOtuSpreadsheetsWithTaxaAsColumns( outFile
				, numToInclude);
		
		OtuWrapper wrapper2 = new OtuWrapper(outFile);
		wrapper2.writeunLoggedDataWithSamplesAsColumns(new File( 
				ConfigReader.getPancreatitisDir() + File.separator + 
				"phylaTop6SamplesAsColumns.txt"));
		
	}
}
