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

import java.util.Random;

import junit.framework.TestCase;

import reduceOTU.HashHolder;

public class TestHashHolder extends TestCase
{
	public void forAHash( int hash) throws Exception
	{
		System.out.println(hash);
		String s = getRandomString(100);
		HashHolder hh = new HashHolder(hash);
		
		assertTrue( hh.setToString(s));
		assertEquals(hh.getNumValidChars(),hash);
		assertEquals(hh.getSequence(), s.substring(0, hash));
		assertEquals(hh.getIndex(), hash-1);
		
		int numCalls =0;
		while( hh.advance() )
		{
			numCalls++;
			assertEquals(hh.getIndex(), hash -1 + numCalls);
			assertEquals(hh.getSequence(), s.substring(numCalls, numCalls + hash));
		}
		
		assertEquals(hh.getIndex(), hash + numCalls);
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
		assertEquals(hh.getIndex(), s1.indexOf("AAACC") + "AAACC".length() - 1);
		
	}
	
	private String getRandomString(int length) throws Exception
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
	
}
