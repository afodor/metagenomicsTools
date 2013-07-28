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


package scripts.compareOTUs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class CompareReduceAndAbundanOTU
{
	public static void main(String[] args) throws Exception
	{
		writeIntFile(ConfigReader.getReducedOTUDir() + File.separator + "CF.cons", ConfigReader.getReducedOTUDir() + File.separator + "cfCounts.txt");

		writeIntFile(ConfigReader.getReducedOTUDir() + File.separator + "repOTU.fasta", ConfigReader.getReducedOTUDir() + File.separator + "repCounts.txt");
	}
	
	private static void writeIntFile(String fastaFile, String outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		
		writer.write( "outNum\tnumSeqs\n" );
		
		int x=0;
		for(Integer i : getNums(fastaFile))
		{
			x++;
			writer.write(x+ "\t");
			writer.write( i + "\n" );
		}
			
		writer.flush(); writer.close();
	}
	
	private static List<Integer> getNums( String filePath ) throws Exception
	{
		List<Integer> list = new ArrayList<Integer>();
		
		List<FastaSequence> fastalist = FastaSequence.readFastaFile(filePath);
		
		for(FastaSequence fs : fastalist)
			list.add(getNum(fs.getHeader()));
		
		return list;
	}
	
	private static int getNum(String header)
	{
		StringTokenizer sToken = new StringTokenizer(header);
		
		sToken.nextToken();  sToken.nextToken();
		
		StringTokenizer sToken2 = new StringTokenizer(sToken.nextToken(), "=");
		
		sToken2.nextToken();
		
		return Integer.parseInt(sToken2.nextToken());
	}
}
