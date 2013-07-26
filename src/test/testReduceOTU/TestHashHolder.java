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
	public void test1() throws Exception
	{
		String s = getRandomString(32);
		HashHolder hh = new HashHolder(32);
		
		assertTrue( hh.setToString(s));
		assertEquals(hh.getNumValidChars(),32);
		assertEquals(hh.getSequence(), s.substring(0, 32));
		
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
