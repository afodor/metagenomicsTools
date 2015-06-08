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

public class MakeContextMapBits
{
	
	private static final int CONTEXT_SIZE = 12;

	public static void main(String[] args) 
	{
		long contextMask = getContextMask();
		
		System.out.println(Long.toBinaryString(contextMask));
	}
	
	private static long getContextMask()
	{
		int one = 0x0001;
		int start= one;
		
		for( int x=1; x < CONTEXT_SIZE ; x++)
		{
			start = start << 1;
			start = start | one;
		}
		
		return start;
	}
}
