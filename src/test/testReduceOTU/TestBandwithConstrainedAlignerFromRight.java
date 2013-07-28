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
	
	public void testFailedAlignment()  throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();
		String s1 =  common + "AAAA";
		String s2 = common + "CCCC";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 3);
		//System.out.println(dp.getEditList());
		assertFalse(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		
		assertEquals(list.size(),4);
		assertEquals(dp.getNumErrors(),4);
		
		dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 4);
		//System.out.println(dp.getEditList());
		assertTrue(dp.alignmentWasSuccesful());
		list = dp.getEditList();
		
		assertEquals(list.size(),4);
		assertEquals(dp.getNumErrors(),4);
		

		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		//System.out.println(pa.toString());
	}
	
	public void testSingleRightAlignmentInsertionInString2() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();
		String s1 = common + "ACTGACTG" ;
		String s2 = common + "ACTGACT";
		
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
						32, 3);
		
		List<IndividualEdit> list = dp.getEditList();
		assertEquals(list.size(),1);
		assertEquals(dp.getNumErrors(),1);
		//System.out.println(list);
		assertTrue(dp.alignmentWasSuccesful());
		

		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		//System.out.println(pa.toString());
	}
	

	public void testDoubleRightAlignmentDeltionInString2() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("I");
		
		String common = buff.toString();
		String s1 = common + "AAATTT";
		String s2 = common + "AAATTTTT";
		
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 3);

		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		//System.out.println(pa.toString());
		
		assertEquals(list.size(),2);
		assertEquals(dp.getNumErrors(),1);
		assertTrue( dp.alignmentWasSuccesful());
	}

	public void testSingleLeftAlignmentDeletionInString2() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();
		String s1 =  common + "ACTGACT";
		String s2 =  common + "ACTGACTG" ;
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
						32, 3);
		
		List<IndividualEdit> list = dp.getEditList();
		assertEquals(list.size(),1);
		assertEquals(dp.getNumErrors(),1);
		//System.out.println(list);
		assertTrue(dp.alignmentWasSuccesful());
		

		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		//System.out.println(pa.toString());
	}
	
	public void testTrailingNotCountingAsErrors() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();
		String s1 =  common + "ATG";
		String s2 = common + "AATC";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 3);

		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa.toString());
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
		
		assertEquals(list.size(),2);
		assertEquals(dp.getNumErrors(),2);
		assertTrue( dp.alignmentWasSuccesful());
	}
	
	public void testTrailingNotCountingAsErrors2() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();

		String s1 = common + "AAAAAAAAAATTTTTTTC" ;
		String s2 =  common + "AAAAAATTTTTTTG" ;
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 100);

		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa.toString());
		
		assertEquals(list.size(),8);
		assertEquals(dp.getNumErrors(),6);
		assertTrue( dp.alignmentWasSuccesful());
		
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
	
	public void testTrailingNotCountingAsErrors3() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < 32; x++)
			buff.append("X");
		
		String common = buff.toString();

		String s1 = common + "AATTC" ;
		String s2 =  common + "ATG" ;
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 100);

		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa.toString());
		
		assertTrue( dp.alignmentWasSuccesful());
		
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
	
	public void testASequence() throws Exception
	{
		String common = "";
		
		for( int x=0; x < 32; x++)
			common += "X";
		
		String s1 = common + "GGGAGG";
		String s2 = common + "ACCGAG";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 200);
		
		assertTrue(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa);
		
		//System.out.println(pa.getFirstSequence().replaceAll("-",""));
		//System.out.println(s1);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
	
	public void testASequence3() throws Exception
	{
		String common = "";
		
		for( int x=0; x < 32; x++)
			common += "X";
		
		String s1 = common +  "CAC";
		String s2 = common + "ACCCGTCGCCAC";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 200);
		
		assertTrue(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		System.out.println(pa);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
	
	public void testASequence4() throws Exception
	{
		String common = "";
		
		for( int x=0; x < 32; x++)
			common += "X";
		
		String s1 = common +  "AGAAGAGGGCAGGTTACTCACGTGTTACTCACCCGTTCGCCACT";
		String s2 = common + "AGGAAAGTAAGGGGCAGGTTACTCACGTGTTACTCTACCCG";
		
		DP_Expand dp = new DP_Expand(s1, s2, s1.indexOf(common), s2.indexOf(common), 
				32, 200);
		
		assertTrue(dp.alignmentWasSuccesful());
		List<IndividualEdit> list = dp.getEditList();
		//System.out.println(s1.length());
		//System.out.println(list);
		PairedAlignment pa = ReducedTools.getAlignment(s1, list);
		//System.out.println(pa);
		
		assertEquals( pa.getFirstSequence().replaceAll("-",""), s1);
		assertEquals( pa.getSecondSequence().replaceAll("-",""), s2);
	}
}
