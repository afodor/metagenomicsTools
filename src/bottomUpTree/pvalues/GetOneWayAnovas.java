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


package bottomUpTree.pvalues;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;

import utils.ConfigReader;
import bottomUpTree.PivotOut;
import bottomUpTree.ReadCluster;
import eTree.ENode;

public class GetOneWayAnovas
{
	public static HashMap<String, Double> getOneWayAnovaPValues() throws Exception
	{
		HashMap<Float, List<ENode>> map = ReadCluster.getMapByLevel(
				ConfigReader.getETreeTestDir() + File.separator + "bottomUpMelMerged0.2.merged",false, false);
		System.out.println(map.size());
		
		for( Float f : map.keySet())
		{
			File outFile = new File(ConfigReader.getETreeTestDir() + File.separator + 
					"Mel74ColumnsAsTaxaFor" + f + ".txt");
			System.out.println(outFile.getAbsolutePath());
			PivotOut.pivotOut(map.get(f), outFile.getAbsolutePath()) ;
			OtuWrapper wrapper = new OtuWrapper(outFile);
			
			
		}
		
		return null;
	}	
	
	
	
	
}
