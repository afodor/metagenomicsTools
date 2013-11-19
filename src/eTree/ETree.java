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

package eTree;

import parsers.FastaSequenceOneAtATime;

public class ETree
{
	public static final float[] LEVELS = { 0.1f, 0.07f, 0.04f, 0.03f, 0.02f, 0.01f };
	
	private final ENode topNode;
	
	public ETree(String starterSequence)
	{
		this.topNode = new ENode(starterSequence, LEVELS[0], null);
		ENode lastNode = topNode;
		
		for( int x=1; x < LEVELS.length; x++)
		{
			ENode nextNode = new ENode(starterSequence, LEVELS[x], lastNode);
			lastNode = nextNode;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = 
				new FastaSequenceOneAtATime("G:\\wolfgangIrishOnlyAbundantOTU\\postLucyFiltering.txt");
		
		ETree eTree = new ETree(fsoat.getNextSequence().getSequence());
		
		
	}
	
}
