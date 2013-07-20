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


package coPhylog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.FastQ;
import utils.ConfigReader;

/*
 * A brute force, naive initial attempt to implement
 * this algorithm:
 * 
 * http://www.ncbi.nlm.nih.gov/pubmed/23335788
 * 
 * "Co-phylog: an assembly-free phylogenomic approach for closely related organisms."
 */
public class MakeContextMap
{
	/*
	 * Todo: Speed this up.. Shouldn't have to keep re-checking the same 
	 * positions..
	 */
	private static boolean allValid(String s)
	{
		for( int x=0; x < s.length(); x++)
		{
			char c= s.charAt(x);
			
			if ( ! (c=='A' || c=='C' || c =='G' || c == 'T'))
				return false;
		
		}
		
		return true;
	}
	
	private static boolean valid(char c)
	{
		if ( c=='A' || c=='C' || c =='G' || c == 'T')
				return true;
		
		return false;
	}
	
	public static HashMap<String, ContextCount> 
		getContextMap( File fastQFile, int leftHashLength, 
						int rightHashLength) throws Exception
	{
		HashMap<String, ContextCount> map = new HashMap<String, ContextCount>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fastQFile));
		
		int numLines=0;
		
		for(  FastQ fastq = FastQ.readOneOrNull(reader); 
					fastq != null; 
						fastq = FastQ.readOneOrNull(reader))
		{
			numLines++;
			String seq = fastq.getSequence().toUpperCase();
			
			// todo: test to make sure we are getting to the end of the sequence
			// todo: add the reverse complement
			int stopPoint = seq.length() - (leftHashLength + rightHashLength + 1);
			
			for( int x=0; x < stopPoint; x++ )
			{
				String leftHash = seq.substring(x, x + leftHashLength);
				
				char insert = seq.charAt(x + leftHashLength);
				
				String rightHash = seq.substring(x + leftHashLength + 1, 
						x + leftHashLength + 1 + rightHashLength);
				
				String key = leftHash + rightHash;
				
				if(allValid(key) && valid(insert))
				{
					ContextCount cc = map.get(key);
					
					if( cc == null)
					{
						cc = new ContextCount();
						map.put(key, cc);
					}
					
					cc.increment(insert);
				}
			}
			
			if( numLines % 10000 ==0)
			{
				System.out.println(numLines + " " + map.size() + " " + ((float)map.size() / numLines));
				writeMap(map, new File( "c:\\temp\\out.txt"));
				System.exit(1);
			}
				
		}
		
		return map;
	}
	
	private static void writeMap(HashMap<String, ContextCount>  map, File outFile) throws Exception
	{
		System.out.println("Writing " + outFile.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("context\tnumA\tnumC\tnumG\tnumT\n");
		
		for( String s : map.keySet() )
		{
			writer.write(s + "\t");
			
			ContextCount cc= map.get(s);
			
			writer.write(cc.getNumA() +"\t" + cc.getNumC() +"\t" + cc.getNumG() + "\t" + cc.getNumT()
					+ "\n");
			
		}
			
		writer.flush(); writer.close();
		System.out.println("Finished");
	}
	
	public static void main(String[] args) throws Exception
	{
		File file = new File(ConfigReader.getBurkholderiaDir() + File.separator + 
				"AS130-2_ATCACG_s_2_1_sequence.txt");
		
		HashMap<String, ContextCount> 
			map = getContextMap(file, 15, 15);
		
		File outFile = new File(ConfigReader.getBurkholderiaDir() + File.separator + 
				"AS130-2_ATCACG_s_2_1_contextMap.txt");
		
		writeMap(map, outFile);
	}
}
