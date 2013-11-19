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

import java.io.File;

import dynamicProgramming.DNASubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;

import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class TestProbNW
{
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = 
				new FastaSequenceOneAtATime( ConfigReader.getETreeTestDir() + 
						File.separator + "postLucyFiltering.txt");
		
		String seq1 = fsoat.getNextSequence().getSequence();
		String seq2 = fsoat.getNextSequence().getSequence();
		
		PairedAlignment pa = 
		NeedlemanWunsch.globalAlignTwoSequences(seq1, seq2, new DNASubstitutionMatrix(), -3, 99, true);
		
		System.out.println(pa);
		
	}
}
