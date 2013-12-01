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


package bottomUpTree;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import eTree.PivotToSpreadheet;

import probabilisticNW.ProbSequence;
import utils.ConfigReader;



public class PivotOut
{
	public static void main(String[] args) throws Exception
	{
		List<ProbSequence> list = ReadCluster.readFromFile(
				ConfigReader.getETreeTestDir() + File.separator + "Merged74At03.merged", false);
		
		int otuNum =1;
		
		HashMap<String, HashMap<String, Integer>> outerMap = new HashMap<String, HashMap<String,Integer>>();
		
		for( ProbSequence probSeq : list)
		{
			outerMap.put("OTU" + otuNum, probSeq.getSampleCounts());
			System.out.println(otuNum + ":" + probSeq.getSampleCounts());
			otuNum++;
		}
		
		PivotToSpreadheet.writeResults(new File(ConfigReader.getETreeTestDir() + File.separator + "mergedTaxaAsColumns.txt"), 
				outerMap);
	}
}
