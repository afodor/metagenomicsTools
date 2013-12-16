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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;

import sun.misc.Perf.GetPerfAction;
import utils.ConfigReader;
import utils.OneWayAnova;
import bottomUpTree.PivotOut;
import bottomUpTree.ReadCluster;
import eTree.ENode;
import eTree.ETree;

public class GetOneWayAnovas
{
	public static HashMap<String, Double> getOneWayAnovaPValues(ENode root) throws Exception
	{
		HashMap<String, Double> returnMap = new HashMap<String, Double>();
		HashMap<Float, List<ENode>> map = ReadCluster.getMapByLevel(root);
		System.out.println(map.size());
		
		for( Float f : map.keySet())
		{
			File outFile = new File(ConfigReader.getETreeTestDir() + File.separator + 
					"Mel74ColumnsAsTaxaFor" + f + ".txt");
			System.out.println(outFile.getAbsolutePath());
			PivotOut.pivotOut(map.get(f), outFile.getAbsolutePath()) ;
			OtuWrapper wrapper = new OtuWrapper(outFile);
			List<List<Double>> list = wrapper.getDataPointsNormalizedThenLogged();
			
			for( int x=0; x < wrapper.getOtuNames().size(); x++ )
			{
				double pValue = 1;
				
				if( ! wrapper.getOtuNames().get(x).equals(ETree.ROOT_NAME))
				{
					List<Number> data = new ArrayList<Number>();
					List<String> factors = new ArrayList<String>();
					for( int y=0; y < wrapper.getSampleNames().size(); y++)
					{
						if( ! wrapper.getSampleNames().get(y).equals(ETree.ROOT_NAME))
						{
							data.add(list.get(y).get(x));
							factors.add("" +  stripSuffix(wrapper.getSampleNames().get(y)));
						}
					}
					
					
					OneWayAnova owa =new OneWayAnova(data, factors);
					
					if(map.containsKey(wrapper.getOtuNames().get(x)))
						throw new Exception("Duplicate");
					
					pValue = owa.getPValue();
					
				}
				
				returnMap.put(wrapper.getOtuNames().get(x), pValue);
			}
			
		}
		
		return returnMap;
	}	
	
	private static int stripSuffix(String s) throws Exception
	{
		boolean keepGoing = true;
		StringBuffer buff = new StringBuffer();
		
		for( char c : s.toCharArray())
		{

			if(keepGoing && Character.isDigit(c))
			{
				buff.append(c);
			}
			else
			{
				keepGoing=true;
			}
		}		
		return Integer.parseInt(buff.toString());
	}
	
	
	
	
}
