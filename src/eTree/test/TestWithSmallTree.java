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

import probabilisticNW.ProbSequence;
import utils.ConfigReader;
import dynamicProgramming.DNASubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;
import eTree.ETree;

public class TestWithSmallTree
{
	public static void main(String[] args) throws Exception
	{
		String s3B1568  =
				"CTGGGCCGTGTCTCAGTCCCAATGTGGCCGTTCAACCTCTCAGTCCGGCTACCGATCGCGGTCTTGGTGAGCCGTTACCTCACCAACTAACTAATCGGACGCGAGTCCATCTTCAAGCGATAAAATCTTTGATATCAAAACCATGTGGTTCCGATATATCATGCGGTATTAGCATTCTTTTCAGAATGTTATCCCCCTCTTGAAGGCAGGTTACTCACGCG";
		
		String s3B1842= "CTGGGCCGTGTCTCAGTCCCAATGTGGCCGTTCAACCTCTCAGTCCGGCTACCGATCGCGGTCTTGGTGAGCCGTTACCTCACCAACTAACTAATCGGACGCGAGTCCATCTTCAAGCGATAAAATCTTTGATATCAAAACCATGTGGTTCCGATATATCATGCGGTATTAGCATTCTTTTCAGAATGTTATCCCCCCTCTTGAAGGCAGGTTACTCACGCG";
		
		PairedAlignment pa = 
				NeedlemanWunsch.globalAlignTwoSequences(s3B1568, s3B1842, new DNASubstitutionMatrix(), -3, 99, true);
				
				System.out.println(pa.toString());
				
		ETree eTree = new ETree();
		eTree.addSequence(new ProbSequence(s3B1568, "S1"), "S1",false);
		eTree.addSequence(new ProbSequence(s3B1842, "S2"), "S2",false);
		
		eTree.writeAsText(ConfigReader.getETreeTestDir() + File.separator + "smallTree.txt", true);
	}
}
