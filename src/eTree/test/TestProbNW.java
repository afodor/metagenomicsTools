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


package eTree.test;


import probabilisticNW.ProbNW;
import probabilisticNW.ProbSequence;
import dynamicProgramming.DNASubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;


public class TestProbNW
{
	public static void main(String[] args) throws Exception
	{
		
		String seq1 = "TTTTTTAAAAAAAAAAAAAAAAAAACCCAAAAAAAAAAAAAAAAAAAAAAAAAAAAATTTTT";
		String seq2 = "TTTTTTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATTTTT";
		
		PairedAlignment pa = 
		NeedlemanWunsch.globalAlignTwoSequences(seq1, seq2, new DNASubstitutionMatrix(), -3, 99, true);
		
		System.out.println(pa);
		
		ProbSequence probSeq1 = new ProbSequence(seq1, 1, "Sample1");
		ProbSequence probSeq2 = new ProbSequence(seq2, 1, "Sample1");
		
		ProbSequence align = ProbNW.align(probSeq1, probSeq2);
		System.out.println(align);
		System.out.println(align.getSumDistance());
	}
}
