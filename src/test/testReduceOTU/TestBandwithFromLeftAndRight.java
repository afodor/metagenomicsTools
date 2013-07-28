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

import dynamicProgramming.PairedAlignment;
import junit.framework.TestCase;
import reduceOTU.DP_Expand;
import reduceOTU.IndividualEdit;
import reduceOTU.ReducedTools;

public class TestBandwithFromLeftAndRight extends TestCase
{
	public void testASequence() throws Exception
	{
		String common = "";
		
		for( int x=0; x < 32; x++)
			common += "X";
		
		String s1 = "NNAAG" + common + "GGGAGG";
		String s2 = "AANNG" + common + "ACCGAG";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 200);
		
		assertTrue(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		System.out.println(pa);
		
		System.out.println(pa.getFirstSequence().replaceAll("-",""));
		System.out.println(s1);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
	
	/*  TEST CASE FAILING !!!! 
	public void test1() throws Exception
	{
		String common ="GTCTCAGTCCCAGTGTGGCTGGTCGTCCTCTCAGACCAGCTACTGATCGTCGCCTTG";
		String s1 = "NNAAGGGACCCANN" + common + "ACCGTT" + common;
		String s2 = "ACCGGAC" + common + "AGGN";
		
		System.out.println(  s1.indexOf(common));
		System.out.println(  s2.indexOf(common));
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 200);
		
		assertTrue(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		System.out.println(pa);
		
		System.out.println(pa.getFirstSequence().replaceAll("-",""));
		System.out.println(s1);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		
	}
	*/
}
