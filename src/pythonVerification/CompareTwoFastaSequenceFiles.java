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


package pythonVerification;

import java.util.List;

import parsers.FastaSequence;

public class CompareTwoFastaSequenceFiles
{
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list1 = FastaSequence.readFastaFile("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\trimmedFasta.txt");
		List<FastaSequence> list2 = FastaSequence.readFastaFile("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\trimmedFromJava.txt");
		
		System.out.println(list1.size() +  " " + list2.size());
		
		for( int x=0; x < list1.size(); x++)
		{
			FastaSequence fs1 = list1.get(x);
			FastaSequence fs2 = list2.get(x);
			
			if( ! fs1.getHeader().equals(fs2.getHeader()))
				throw new Exception("No");
			
			if( ! fs1.getSequence().equals(fs2.getSequence()))
				throw new Exception("No");
			
			
		}
		
	}
}
