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

package test.testReduceOTU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import dynamicProgramming.PairedAlignment;

import reduceOTU.DP_Expand;
import reduceOTU.HashHolder;
import reduceOTU.IndividualEdit;
import reduceOTU.ReducedTools;

import junit.framework.TestCase;

import utils.ConfigReader;

/*
 * This has dependecies on external files and is slow so is not included in the test suite
 */
public class TestOnRealOTUs extends TestCase
{
	public void test1() throws Exception
	{
		List<String> seqs = getAllSequences();
		
		int numSearched=0;
		int numAligned =0;
		
		for( int x=0; x < seqs.size()-1; x++ )
		{
			String xSeq = seqs.get(x);
			HashMap<Long, Integer> xMap = HashHolder.getWordIndex(xSeq, 32);
			
			for( int y=x+1; y < seqs.size(); y++)
			{
				String ySeq = seqs.get(y);
				HashMap<Long, Integer> yMap = HashHolder.getWordIndex(ySeq, 32);
				
				numSearched++;
				
				HashSet<Long> xSet = new HashSet<Long>(xMap.keySet());
				HashSet<Long> ySet = new HashSet<Long>(yMap.keySet());
				
				xSet.retainAll(ySet);
				
				if( xSet.size() > 0)
				{
					long key = xSet.iterator().next();
					DP_Expand dp = new DP_Expand(xSeq, ySeq, xMap.get(key),
											yMap.get(key), 32, 10000);
					
					assertTrue(dp.alignmentWasSuccesful());
					List<IndividualEdit> list = dp.getEditList();
					System.out.println(list);
					System.out.println(xSeq);
					System.out.println(ySeq);
					System.out.println(dp.getNumErrors() + " " + 
							((double)dp.getNumErrors()) / Math.min(xSeq.length(), ySeq.length()) );
					PairedAlignment pa = ReducedTools.getAlignment(xSeq, list);
					System.out.println(pa);
					
					assertEquals( pa.getFirstSequence().replaceAll("-",""), xSeq);
					assertEquals( pa.getSecondSequence().replaceAll("-",""), ySeq);
					
					numAligned++;
					
					System.out.println("\tSearched for words : " 
							+ numSearched + " aligned " + numAligned );
				
				}
			}
		}
		
	
	}
	
	private static List<String> getAllSequences() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getReducedOTUDir() + File.separator + "derepped.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			list.add(new StringTokenizer(s).nextToken());
		}
		
		reader.close();
		
		return list;
	}
	
}
