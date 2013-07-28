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


package test.testReduceOTU;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

import junit.framework.TestCase;

import reduceOTU.HashHolder;

public class TestHashHolder extends TestCase
{
	public void forAHash( int hash) throws Exception
	{
		String s = getRandomString(100);
		HashHolder hh = new HashHolder(hash);
		
		assertTrue( hh.setToString(s));
		assertEquals(hh.getSequence(), s.substring(0, hash));
		assertEquals(hh.getHashHolderIndex(), hash-1);
		assertEquals(hh.getNumInvalidChars(),0);
		assertEquals(hh.getStringIndex(),0);
		
		int numCalls =0;
		while( hh.advance() )
		{
			numCalls++;
			assertEquals(hh.getHashHolderIndex(), hash -1 + numCalls);
			assertEquals(hh.getSequence(), s.substring(numCalls, numCalls + hash));
			assertEquals(hh.getNumInvalidChars(),0);
			assertEquals(hh.getStringIndex(), numCalls);
		}
		
		assertEquals(hh.getSequence(), null);
		assertEquals(hh.getHashHolderIndex(), s.length());
		assertEquals(hh.getStringIndex(), -1);
	}
	
	public void testHashes() throws Exception
	{
		for( int x=15; x <=32; x++)
			forAHash(x);
	}
	
	public void testAllBad() throws Exception
	{
		String s1 = "ACAGXAAAXAAACCXXATTGZXXAZZ";
		
		HashHolder hh = new HashHolder(6);
		assertFalse(hh.setToString(s1));
		
		hh = new HashHolder(5);
		assertTrue(hh.setToString(s1));
		assertEquals("AAACC",hh.getSequence());
		assertEquals(hh.getHashHolderIndex(), s1.indexOf("AAACC") + "AAACC".length() - 1);
		assertEquals(hh.getStringIndex(), s1.indexOf("AAACC"));
		assertEquals(hh.getNumInvalidChars(),2);
		assertFalse(hh.advance());
		assertEquals(hh.getHashHolderIndex(), s1.length());
		assertEquals(hh.getStringIndex(), -1);
		assertEquals(hh.getSequence(), null);
	}
	
	static String getRandomString(int length) throws Exception
	{
		Random random = new Random();
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < length; x++)
		{
			int val = random.nextInt(4);
			
			if( val ==0)
				buff.append("A");
			else if ( val == 1)
				buff.append("C");
			else if ( val == 2)
				buff.append("G");
			else if ( val == 3)
				buff.append("T");
			else throw new Exception("No");
		}
		
		return buff.toString();
	}
	
	public void testHash() throws Exception
	{
		String common = getRandomString(32);
		String s1 = "AGG" + common;
		String s2 = "GT" + common;
		
		HashMap<Long, Integer> map1 = new LinkedHashMap<Long, Integer>();
		HashMap<Long, Integer> map2= new LinkedHashMap<Long, Integer>();
		
		HashHolder hh1 = new HashHolder(32);
		hh1.setToString(s1);
		map1.put(hh1.getBits(), hh1.getStringIndex());
		
		while(hh1.advance())
			map1.put(hh1.getBits(), hh1.getStringIndex());
		
		hh1.setToString(s2);
		
		map2.put(hh1.getBits(), hh1.getStringIndex());
		
		while(hh1.advance())
			map2.put(hh1.getBits(), hh1.getStringIndex());
		
		//System.out.println(map1); 
		//System.out.println(map2); 
		
		assertEquals(map1.size(), 4);
		assertEquals(map2.size(), 3);
		
		HashSet<Long> set1 = new HashSet<Long>(map1.keySet());
		set1.retainAll(map2.keySet());
		
		assertEquals(set1.size(), 1);
		
		Long key = set1.iterator().next();
		
		assertEquals(map1.get(key), new Integer( 3));
		assertEquals(map2.get(key), new Integer( 2));
	}
	
	public void testMultipleStops() throws Exception
	{
		String s1 = getRandomString(3);
		
		String s2 = getRandomString(3);
		
		while( s2.equals(s1))
			s2 = getRandomString(3);
		
		String s3 = getRandomString(3);
		
		while( s3.equals(s1) || s3.equals(s2))
			s3 = getRandomString(3);
		
		String s4 = getRandomString(3);
		
		while( s4.equals(s1) || s4.equals(s2) || s4.equals(s3))
			s4 = getRandomString(3);
		
		String allString = s1 + "X" + s2 + "XX" + s3 + "X" + s4 + "X";
		
		HashHolder hh = new HashHolder(3);
		assertTrue(hh.setToString(allString));
		assertEquals(hh.getSequence(), s1);
		assertEquals(hh.getHashHolderIndex(), 2);
		assertEquals(hh.getNumInvalidChars(),0);
		assertEquals(hh.getStringIndex(), allString.indexOf(s1));

		assertTrue(hh.advance());
		assertEquals(hh.getSequence(), s2);
		assertEquals(hh.getHashHolderIndex(), 6);
		assertEquals(hh.getNumInvalidChars(),1);
		assertEquals(hh.getStringIndex(), allString.indexOf(s2));
		
		assertTrue(hh.advance());
		assertEquals(hh.getSequence(), s3);
		assertEquals(hh.getHashHolderIndex(), 11);
		assertEquals(hh.getNumInvalidChars(),3);
		assertEquals(hh.getStringIndex(), allString.indexOf(s3));
		
		assertTrue(hh.advance());
		assertEquals(hh.getSequence(), s4);
		assertEquals(hh.getHashHolderIndex(), 15);
		assertEquals(hh.getNumInvalidChars(),4);
		assertEquals(hh.getStringIndex(), allString.indexOf(s4));
		
		
		assertFalse(hh.advance());
		assertEquals(hh.getHashHolderIndex(), allString.length());
		assertEquals(hh.getStringIndex(), -1);
		assertEquals(hh.getSequence(), null);
		
	}
	
}
