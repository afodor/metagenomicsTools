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

import java.util.HashMap;
import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.junit.Test;

import coPhylog.BitHolder;
import coPhylog.ContextCount;
import coPhylog.ContextHash;

public class TestContextHash extends TestCase
{

	@Test
	public void test() throws Exception
	{
		String s= "AACAATTAACAA";
		
		HashMap<Long, ContextCount> map = new LinkedHashMap<Long, ContextCount>();
		
		ContextHash.addToHash(s, map, 2);
		
		for(Long l : map.keySet())
		{
			ContextCount cc = map.get(l);
			System.out.println( l + " " + BitHolder.getContext(l, 2) + " " + cc );
		}
			
	}

}
