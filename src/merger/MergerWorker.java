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

import java.io.BufferedWriter;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import parsers.FastQ;

import utils.Translate;

public class MergerWorker implements Runnable
{
	
	private static final long startTime = System.currentTimeMillis();
	
	private final FastQ s1;
	private final FastQ s2;
	private final BufferedWriter writer;
	private final Semaphore semaphore;
	private final String sampleKey;
	private final int minOverlap;
	
	public static AtomicLong numberMerged = new AtomicLong(0);
	public static AtomicLong numberMatched = new AtomicLong(0);
	
	public MergerWorker(FastQ s1, 
							FastQ s2, 
								BufferedWriter writer, 
									Semaphore semaphore, 
										String sampleKey, 
											int minOverlap)
	{
		this.s1=s1;
		this.s2=s2;
		this.writer = writer;
		this.semaphore = semaphore;
		this.sampleKey = sampleKey;
		this.minOverlap = minOverlap;
	}
	
	private static class Holder
	{
		int s2StartPos;
		int numOverlap=0;
	}
	
	@Override
	public void run()
	{
		try
		{
			String s1Forward = s1.getSequence();
			String s2Rev = Translate.safeReverseTranscribe(s2.getSequence());
			
			Holder h = new Holder();
			
			// If I have an overlap of 70, but I am 69 from the end, the next overlap
			// can't be greater than the overlap I have, so I don't need to search
			// so I can subtract h.numOverlap from the length of the 2nd sequence
			for( int s2startPos=0; s2startPos < s2Rev.length() - h.numOverlap; s2startPos++)
			{
				int overlap =0;
				
				for( int x=0; 
					x < s1Forward.length() && x + s2startPos < s2Rev.length(); 
						x++)
				{
					char c1 = s1Forward.charAt(x);
					char c2 = s2Rev.charAt(x + s2startPos);
					
					if( c1 != 'N' && c1 == c2)
						overlap++;
				}
				
				if( overlap > h.numOverlap )
				{
					h.s2StartPos = s2startPos;
					h.numOverlap = overlap;
				}
			}
		
			if( h.numOverlap >= minOverlap)
			{
				StringBuffer consensus = new StringBuffer();
				String qualScoreReverse = new StringBuffer(s2.getQualScore()).reverse().toString();
				
				for(  int x=0; x < s1.getSequence().length() && 
												x + h.s2StartPos < s2Rev.length(); x++)
				{
					char c1 = s1.getSequence().charAt(x);
					char c2 = s2Rev.charAt(x + h.s2StartPos);
					
					if( c1 == c2)
						consensus.append(c1);
					else if ( c1 == 'N')
						consensus.append(c2);
					else if ( c2 == 'N')
						consensus.append(c1);
					else
					{
						int q1 = s1.getQualScore().charAt(x);
						int q2 = qualScoreReverse.charAt(x + h.s2StartPos);
						
						consensus.append(q1 > q2 ? c1 : c2);
					}
				}
				
				
				String header1 = new StringTokenizer( s1.getHeader()).nextToken();
				String header2 = new StringTokenizer( s2.getHeader()).nextToken();
				
				if( header1.endsWith("/1"))
					header1 = header1.substring(0, header1.length()-2);
				
				if( header2.endsWith("/2"))
					header2 = header2.substring(0, header2.length()-2);
				
				if( ! header1.equals(header2))
				{
					System.out.println("No " + header1 + " " + header2);
					System.exit(1);
					
				}
					
				numberMatched.incrementAndGet();
				
				synchronized( writer)
				{
					
					writer.write(">");
					writer.write(header1);
					writer.write("_");
					writer.write(sampleKey);
					writer.write("\n");
					writer.write(consensus.toString());
					writer.write("\n");
					
					/*
					for( int y=0; y < h.s2StartPos; y++)
						System.out.print(" ");
					
					System.out.println(s1Forward);
					
					System.out.println(s2Rev);
					System.out.println(consensus.toString());
					System.out.println( h.s2StartPos + " " +  h.numOverlap);
					System.out.println("\n\n");
					*/
				}
			}
			
			if( numberMerged.incrementAndGet() % 10000 == 0 )
			{
				System.out.println(numberMerged + " " + numberMatched + " " + 
						" permits=" + semaphore.availablePermits() + 
					" " + (System.currentTimeMillis() - startTime) / 1000f + " seconds "	);
				
				
				synchronized(writer)
				{
					writer.flush();
				}

			}
			
			
			semaphore.release();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}