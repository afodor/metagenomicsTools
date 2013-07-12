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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.FastQ;

import utils.ConfigReader;

public class TestFastQ
{
	public static void main(String[] args) throws Exception
	{
		FastQ oldFastq = null;
		
		File dir = new File("F:\\March21Laptop\\RawBurkholderiaSequences\\sequences");
		
		String [] names = dir.list();
		
		for(String s : names)
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(  
					dir.getAbsolutePath() + File.separator + s)));
			
		
			int x=0;
			for( FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; fastq = FastQ.readOneOrNull(reader))
			{
				x++;
				
				if( x % 10000 ==0 )
					System.out.println(x + " " + fastq.getSequence());
				
				oldFastq = fastq;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
			System.out.println(oldFastq);
			System.exit(1);
		}
		
		
	}
}
