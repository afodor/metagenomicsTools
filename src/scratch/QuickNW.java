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


package scratch;

import dynamicProgramming.DNASubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;

public class QuickNW
{
	public static void main(String[] args) throws Exception
	{
		String s3B11145 = "CTGGGCCGTGTCCCAGTCCCAATGTGGCCGTTCAACCTCTCAGTCCGGCTACCGATCGTCGCCTTGGTGGGCCGTTACCTCACCAACTAGCTAATCGGACGCGAGGCCATCTCAAAGCGGATTGCTCCTTTTCCCTCTTCCCGATGCCGGGTCGTGGGCTTATGCGGTATTAGCAGTCGTTTCCAACTGTTGTCCCCCTCTTTGAGGCAGGTTCCTCACGCGTTACTCACCCGTTCG";
		String s3B1385 = "CTGGGCCGTGTCTCAGTCCCAATGTGGCCGTTCAACCTCTCAGTCCGGCTACCGATCGTCGCCTTGGTGGGCCGTTACCTCACCAACTAGCTAATCGGACGCGAGGCCATCTCAAAGCGGATTGCTCCTTTTCCCTCTTCCCGATGCCGGGTCGTGGGCTTATGCGGTATTAGCAGTCGTTTCCAACTGTTGTCCCCCTCTTTGAGGCAGGTTCCTCACGCGTTACTCACCCGTTCG";
		
		PairedAlignment pa = 
		NeedlemanWunsch.globalAlignTwoSequences(s3B11145, s3B1385, new DNASubstitutionMatrix(), -3, 99, true);
		
		System.out.println(pa.toString());
	}
}
