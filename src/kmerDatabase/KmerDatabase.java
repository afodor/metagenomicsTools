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

package kmerDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class KmerDatabase
{
	private HashMap<Integer, HashSet<String>> kmerMap =
			new HashMap<Integer, HashSet<String>>();
	
	public void removeFromDatabase( String target)
	{
		for(int key : kmerMap.keySet())
		{
			for( Iterator<String> i = kmerMap.get(key).iterator(); i.hasNext();)
			{
				if( i.next().equals(target))
					i.remove();
			}
		}
	}
	
	public KmerDatabase()
	{
		
	}
	
	private void addHash(int i, String id)
	{
		HashSet<String> set = kmerMap.get(i);
		
		if( set == null)
		{
			set = new HashSet<String>();
			kmerMap.put(i,set);
		}
		
		set.add(id);
	}
	
	public List<KmerQueryResult> queryDatabase(String query) throws Exception
	{
		List<KmerQueryResult> list = new ArrayList<KmerQueryResult>();
		
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		
		KmerToBits toBits = new KmerToBits(query);
		addToCountMap(toBits.getHashAtCurrentPosition(), countMap);
		
		while(toBits.canAdvance())
		{
			toBits.advance();
			addToCountMap(toBits.getHashAtCurrentPosition(), countMap);
		}
		
		for(String s : countMap.keySet())
			list.add( new KmerQueryResult(s, countMap.get(s)) );
		
		Collections.sort(list);
		return list;
	}
	
	private void addToCountMap( int hash,  HashMap<String, Integer> countMap)
	{
		HashSet<String> samples = kmerMap.get(hash);
		
		if( samples != null)
		{
			for(String s : samples)
			{
				Integer count = countMap.get(s);
				
				if( count == null)
					count=0;
				
				count++;
				
				countMap.put(s, count);
			}
		}
	}
	
	public void addSequenceToDatabase(String sequence, String id)
		throws Exception
	{
		KmerToBits toBits = new KmerToBits(sequence);
		
		addHash(toBits.getHashAtCurrentPosition(), id);
		
		while(toBits.canAdvance())
		{
			toBits.advance();
			addHash(toBits.getHashAtCurrentPosition(), id);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		KmerDatabase kd = new KmerDatabase();
		
		kd.addSequenceToDatabase("AAAAAAAAAAAAAAAAAA", "S1");
		kd.addSequenceToDatabase("CCCCCCCCCCCCCCCCCCCCCCCCCC", "S2");
		kd.addSequenceToDatabase("GGGGGGGGGGGGGGGGGGGGGGGGGGGGG", "S3");
		System.out.println(kd.kmerMap);
		
		System.out.println(kd.queryDatabase("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCGGGGGGGGGGGGGGGGG"));
	}
}
