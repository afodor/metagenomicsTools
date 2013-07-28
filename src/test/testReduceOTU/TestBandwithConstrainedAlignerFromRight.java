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

package test.testReduceOTU;

import java.util.List;

import junit.framework.TestCase;

import reduceOTU.DP_Expand;
import reduceOTU.IndividualEdit;
import reduceOTU.ReducedTools;
import dynamicProgramming.PairedAlignment;

public class TestBandwithConstrainedAlignerFromRight extends TestCase
{
	public void testSingleMisMatch() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();
		String s1 =  common + "GCTGACG";
		String s2 = common + "ACTGACT";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
						32, 3);
		
		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		assertEquals(list.size(),2);
		assertEquals(dp.getNumErrors(),2);
		
		assertTrue(dp.alignmentWasSuccesful());
		
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa);
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		
	}
}
