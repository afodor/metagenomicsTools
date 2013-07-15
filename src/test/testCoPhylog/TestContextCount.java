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


package test.testCoPhylog;

import java.util.HashSet;

import coPhylog.ContextCount;
import junit.framework.TestCase;

public class TestContextCount extends TestCase
{
	public void test1() throws Exception
	{
		ContextCount cc = new ContextCount(12,0,2,3);
		
		assertEquals(cc.getNumA(),12);
		assertEquals(cc.getNumC(),0);
		assertEquals(cc.getNumG(),2);
		assertEquals(cc.getNumT(),3);
		
		HashSet<Character> set = cc.getHighest();
		
		assertEquals(set.size(), 1);
		assertEquals(set.iterator().next().charValue(), 'A');
		
		
		cc = new ContextCount(0,15,15,15);
		
		assertEquals(cc.getNumA(),0);
		assertEquals(cc.getNumC(),15);
		assertEquals(cc.getNumG(),15);
		assertEquals(cc.getNumT(),15);
		set = cc.getHighest();
		
		assertEquals(set.size(), 3);
		
		assertTrue(set.contains('C'));

		assertTrue(set.contains('G'));

		assertTrue(set.contains('T'));
		assertFalse(set.contains('A'));
	}
}
