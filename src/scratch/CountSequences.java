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

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;

public class CountSequences
{
	public static void main(String[] args) throws Exception
	{
		File dir = new File("D:\\E_Tree_TestDir\\gastro454DataSet");
		
		int sum = 0;
		for(String s : dir.list())
		{
			if( s.startsWith("DEREP"))
			{
				List<FastaSequence> list = FastaSequence.readFastaFile(dir.getAbsolutePath() + File.separator + s);
				
				for( FastaSequence fs : list)
				{
					StringTokenizer sToken = new StringTokenizer(fs.getFirstTokenOfHeader(), "_");
					sToken.nextToken(); sToken.nextToken();
					sum+= Integer.parseInt(sToken.nextToken());
				}
			}
				
		}
		
		System.out.println(sum);
	}
}
