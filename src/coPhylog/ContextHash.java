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


package coPhylog;

import java.util.HashMap;

public class ContextHash
{
	/*
	 * Not thread safe
	 */
	public static void addToHash( String s , HashMap<Long, ContextCount> map,
					int contextSize) throws Exception
	{
		BitHolder bh = new BitHolder(contextSize);
		bh.setToString(s, false);
		process(bh, map);
		
		while( bh.advance())
			process(bh, map);
		
		bh.setToString(s, true);
		process(bh, map);
		
		while( bh.advance())
			process(bh, map);
		
	}
	
	/*
	 * Not thread safe
	 */
	private static void process(BitHolder bh, HashMap<Long, ContextCount> map) throws Exception
	{
		long aLong = bh.getBits();
		
		ContextCount cc = map.get(aLong);
		
		if( cc == null)
		{
			cc = new ContextCount();
			map.put(aLong, cc);
		}
		
		cc.increment(bh.getMiddleChar());
	}
}
