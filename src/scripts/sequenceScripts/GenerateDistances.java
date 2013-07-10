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
import java.util.HashMap;

import utils.ConfigReader;

import coPhylog.CoPhylogBinaryFileReader;
import coPhylog.ContextCount;

public class GenerateDistances 
{
	private static class Holder
	{
		private long aLong;
		private ContextCount cc1;
		private ContextCount cc2;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(  
				ConfigReader.getBurkholderiaDir()+ File.separator + "distances" +
						File.separator + "techReps1.txt")));
		
		writer.write("longID\tnumA1\tnumC1\tnumG1\tnumT1\tsum1\tmax1\tnumA2\tnumC2\tnumG2\tnumT2\tsum2\tmax2\tdistance\tstatus\n");
		
		HashMap<Long, ContextCount> map1 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map1 " + map1.size());
		
		HashMap<Long, ContextCount> map2 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_2_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map2 " + map2.size());
		
		int numSearched =0;
		int numFound =0;
		int numDiff =0;
		for(Long l1 : map1.keySet())
		{
			writer.write(l1 + "\t");
			
			ContextCount cc1 = map1.get(l1);
			numSearched++;
			writer.write(cc1.getNumA() + "\t" +  cc1.getNumC() + "\t" + cc1.getNumG() + "\t" + cc1.getNumT() + "\t" +
								cc1.getSum() + "\t" + cc1.getMax() + "\t");
			
			ContextCount cc2 = map2.get(l1);
			
			if( cc2 != null)
			{
				numFound++;
				
				map2.remove(cc2);
				
				writer.write(cc2.getNumA() + "\t" + cc2.getNumC() + "\t" + cc2.getNumG() + "\t" + cc2.getNumT() + "\t" +
						cc2.getSum() + "\t" + cc1.getMax() + "\t" + cc1.getRawDistance(cc2));
				
				if( cc1.isDifferentInHighest(cc2))
				{
					numDiff++;
					writer.write("\tdiff\n");
				}
				else
				{
					writer.write("\tmatch\n");
				}
					
			}
			else
			{
				writer.write("-1\t-1\t-1\t-1\t-1\t-1\t-1\tnotFound\n");
				
			}
			
			if( numSearched % 1000==0)
				System.out.println(numSearched + " " + numFound + " "+ numDiff);
		}
		

		System.out.println(numSearched + " " + numFound + " "+ numDiff + " " + map2.size());
		
	
		writer.flush();  writer.close();
	}
}
