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

package scripts.sequenceScripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ChisquareTest;
import utils.ConfigReader;

import coPhylog.CoPhylogBinaryFileReader;
import coPhylog.ContextCount;


/*
 * This has dependencies on CoPhylogOnBurk 
 */
public class ApplyWeightedChiSquare
{
	
	public static void main(String[] args) throws Exception
	{
		
		List<Double> bVals = new ArrayList<>();
		
		bVals.add(2.0);  bVals.add(4.0); bVals.add(10.0);
		//bVals.add(0.0);bVals.add(0.0001);bVals.add(0.001);bVals.add(0.01);bVals.add(0.1);
		//bVals.add(1.0);bVals.add(10.0);bVals.add(100.0);bVals.add(1000.0);bVals.add(10000.0);bVals.add(100000.0);
		
		HashMap<Long, ContextCount> map1 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map1 " + map1.size());
		CountHolder prior1 = getCounts(map1);
		System.out.println(prior1);
		
		
		HashMap<Long, ContextCount> map2 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_2_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map2 " + map2.size());
		
		CountHolder prior2 = getCounts(map2);
		System.out.println(prior2);
		
		for(Double b : bVals)
			writePValues(b, map1, map2, prior1, prior2);
	}
	
	private static CountHolder getCounts( HashMap<Long, ContextCount> map )
	{
		CountHolder ch = new CountHolder();
		
		long numA=0, numC=0, numG=0, numT=0;
		
		for( ContextCount cc : map.values())
		{
			numA += cc.getNumA();
			numC += cc.getNumC();
			numT += cc.getNumT();
			numG += cc.getNumG();
		}
		
		long sum = numA + numC + numT + numG;
		
		ch.fractionA = ((double)numA) / sum;
		ch.fractionC = ((double)numC) / sum;
		ch.fractionG = ((double)numG )/ sum;
		ch.fractionT = ((double)numT) / sum;
		
		return ch;
	}
	
	private static class CountHolder
	{
		double fractionA;
		double fractionC;
		double fractionG;
		double fractionT;
		
		@Override
		public String toString()
		{
			return "[" + fractionA + "," + fractionC + "," + fractionG + "," + fractionT + "]";
		}
	}
	
	private static void writePValues( double bVal, HashMap<Long, ContextCount> map1 ,
					HashMap<Long, ContextCount> map2, CountHolder prior1, CountHolder prior2 )
		throws Exception
	{
		CountHolder countAverage = new CountHolder();
		countAverage.fractionA = (prior1.fractionA + prior2.fractionA) /2;
		countAverage.fractionC = (prior1.fractionC + prior2.fractionC) /2;
		countAverage.fractionG = (prior1.fractionG + prior2.fractionG) /2;
		countAverage.fractionT = (prior1.fractionT + prior2.fractionT) /2;
		
		System.out.println("Staring bVal = " +  bVal);
		List<Double> pValues = new ArrayList<>();
		
		for( Long aLong : map1.keySet() )
		{
			ContextCount cc2 = map2.get(aLong);
			
			if( cc2 != null)
			{
				ContextCount cc1 = map1.get(aLong);
				
				List<Double> list1 = new ArrayList<>();
				list1.add( cc1.getNumA() + bVal * countAverage.fractionA );
				list1.add( cc1.getNumC() + bVal * countAverage.fractionC );
				list1.add( cc1.getNumG() + bVal * countAverage.fractionG );
				list1.add( cc1.getNumT() + bVal * countAverage.fractionT );
				

				List<Double> list2 = new ArrayList<>();
				list2.add( cc2.getNumA() + bVal * countAverage.fractionA );
				list2.add( cc2.getNumC() + bVal * countAverage.fractionC );
				list2.add( cc2.getNumG() + bVal * countAverage.fractionG );
				list2.add( cc2.getNumT() + bVal * countAverage.fractionT );
				
				double pValue =ChisquareTest.getChisquarePValue(list1, list2); 
				
				if( Double.isInfinite(pValue) || Double.isNaN(pValue))
					pValue =1;
				
				pValues.add(pValue);
				
				if( pValues.size() % 1000000 ==0)
					System.out.println("\t" + pValues.size());
			}
		}
		
		System.out.println("Sorting");
		Collections.sort(pValues);
		
		System.out.println("Writing");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBurkholderiaDir()+
				File.separator + "distances" + File.separator + "pValues_" + bVal + ".txt")));
		writer.write("dunif\tpValue\n");
		
		for( int x=0; x < pValues.size(); x++)
		{
			double expected = ((double)(x+1)) / pValues.size();
			writer.write( expected + "\t" );
			writer.write(pValues.get(x) + "\n");
		}
		
		writer.flush();  writer.close();
		System.out.println("Finished " + bVal);
		
	}
}
