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

package merger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;


public class MergePairedEnds
{
	private static final int NUM_PERMIT = 10;
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
								"javaMergedNoPrimers.txt")));
		
		BufferedReader forwardReader =
			new BufferedReader(new InputStreamReader( 
					new GZIPInputStream( new FileInputStream( "s_7_1_sequence.txt.gz") ) ));
		
		BufferedReader reverseReader =
			new BufferedReader(new InputStreamReader( 
					new GZIPInputStream( new FileInputStream( File.separator + "s_7_2_sequence.txt.gz") ) ));
			
		
		HashMap<String, Integer> forwardMap = new HashMap<String,Integer>();
			//FirstParse.getForwardPrimers();
		
		List<String> forwardKeys= new ArrayList<String>( forwardMap.keySet());
		
		HashMap<String, Integer> backwardMap = new HashMap<String,Integer>(); // FirstParse.getReversePrimers();
		List<String> reverseKeys = new ArrayList<String>(backwardMap.keySet());
		
		Semaphore s = new Semaphore(NUM_PERMIT);
		
		boolean finished = false;
		
		while( ! finished)
		{
			FastQ forwardQ = FastQ.readOneOrNull(forwardReader);
			FastQ backQ = FastQ.readOneOrNull(reverseReader);
			
			if( forwardQ == null)
			{
				finished = true;
				
				if( backQ !=  null)
					throw new Exception("Unequal lengths");
			}
			else
			{
				boolean foundForward= false;
				String forwardKey = null;
				String backwardKey = null;
				
				for( int x=0; !foundForward&& x < forwardKeys.size();x++) 
					if( forwardQ.getSequence().startsWith( forwardKeys.get(x)))
					{
						foundForward= true;
						forwardKey =forwardKeys.get(x); 
						int primerLength = forwardKey.length();
						forwardQ.setSequence(forwardQ.getSequence().substring(primerLength));
						forwardQ.setQualScore(forwardQ.getQualScore().substring(primerLength));
					}
						
				boolean foundBoth = false;
				
				if( foundForward)
					for( int x=0; !foundBoth && x < reverseKeys.size(); x++)
						if( backQ.getSequence().startsWith(reverseKeys.get(x)))
						{
							foundBoth = true;
							backwardKey = reverseKeys.get(x);
							int primerLength = backwardKey.length();
							backQ.setSequence(backQ.getSequence().substring(primerLength));
							backQ.setQualScore(backQ.getQualScore().substring(primerLength));
						}
				
				if( foundBoth)
				{
					Integer forwardInt = forwardMap.get(forwardKey);
					
					if( forwardInt == null)
						throw new Exception("No");
					
					Integer reverseInt = backwardMap.get(backwardKey);
					
					if( reverseInt == null)
						throw new Exception("No");
					
					s.acquire();
					MergerWorker m= new MergerWorker(forwardQ, backQ, writer, s,
							"F" + forwardInt + "@"  + "R" + reverseInt
							);
					new Thread(m).start();
				}
			}	
		}
		
		int numAcquired = 0;
		
		while( numAcquired < NUM_PERMIT)
		{
			s.acquire();
			numAcquired++;
		}
		
		writer.flush();  writer.close();
		forwardReader.close();  reverseReader.close();
	}
}
