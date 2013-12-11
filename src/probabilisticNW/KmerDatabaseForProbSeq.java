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


package probabilisticNW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import eTree.ENode;

import kmerDatabase.KmerToBits;

public class KmerDatabaseForProbSeq
{
	private HashMap<Integer, HashSet<ENode>> kmerMap =
			new HashMap<Integer, HashSet<ENode>>();
	
	private void addHash(int i, ENode eNode)
	{
		HashSet<ENode> set = kmerMap.get(i);
		
		if( set == null)
		{
			set = new HashSet<ENode>();
			kmerMap.put(i,set);
		}
		
		set.add(eNode);
	}
	
	public List<KmerQueryResultForProbSeq> queryDatabase(String query) throws Exception
	{
		List<KmerQueryResultForProbSeq> list = new ArrayList<KmerQueryResultForProbSeq>();
		
		HashMap<ENode, Integer> countMap = new HashMap<ENode, Integer>();
		
		KmerToBits toBits = new KmerToBits(query);
		addToCountMap(toBits.getHashAtCurrentPosition(), countMap);
		
		while(toBits.canAdvance())
		{
			toBits.advance();
			addToCountMap(toBits.getHashAtCurrentPosition(), countMap);
		}
		
		for(ENode eNode: countMap.keySet())
			list.add( new KmerQueryResultForProbSeq(eNode, countMap.get(eNode)) );
		
		Collections.sort(list);
		return list;
	}
	
	private void addToCountMap( int hash,  HashMap<ENode, Integer> countMap)
	{
		HashSet<ENode> samples = kmerMap.get(hash);
		
		if( samples != null)
		{
			for(ENode eNode: samples)
			{
				Integer count = countMap.get(eNode);
				
				if( count == null)
					count=0;
				
				count++;
				
				countMap.put(eNode, count);
			}
		}
	}
	
	public void addSequenceToDatabase(ENode eNode)
		throws Exception
	{
		KmerToBits toBits = new KmerToBits(eNode.getProbSequence().getConsensusUngapped());
		
		addHash(toBits.getHashAtCurrentPosition(), eNode);
		
		while(toBits.canAdvance())
		{
			toBits.advance();
			addHash(toBits.getHashAtCurrentPosition(), eNode);
		}
	}
	
	/*
	 * Any sequence with non ACGT is ignored
	 */
	public static KmerDatabaseForProbSeq buildDatabase(List<ENode> list) throws Exception
	{
		KmerDatabaseForProbSeq db = new KmerDatabaseForProbSeq();
		
		for( ENode eNode : list)
		{	
			db.addSequenceToDatabase(eNode);
		}		
		return db;
	}
}
