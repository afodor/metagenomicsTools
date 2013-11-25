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


package probabilisticNW;

import java.io.BufferedWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ProbSequence implements Serializable
{
	private static final long serialVersionUID = -8572579288693934310L;

	public static double TRIM_INITIAL_GAP_THREHSOLD = 0.25;
	
	private List<ProbColumn> columns = new ArrayList<ProbColumn>();
	private int n=0;
	private HashMap<String, Integer> sampleCounts= new HashMap<String, Integer>();
	
	/*
	 * tabString is prefixed to each output line (to allow for consistent tabing).
	 * (Set tabString to "" disable)
	 */
	public void writeThisSequenceToText( BufferedWriter writer,  String tabString )
		throws Exception
	{
		writer.write(tabString + this.toString() + "\n");
		writer.write(tabString + sampleCounts.toString() + "\n");
		writer.write(tabString + getConsensus().toString() + "\n");
	}
	
	/*
	 * Clients should not modify the map but are not prevented from doing so
	 */
	public HashMap<String, Integer> getSampleCounts()
	{
		return sampleCounts;
	}
	
	public double getAverageDistance()
	{
		double sum =0;
		double n=0;
		
		int startPosition=0;
		int endPosition = columns.size()-1;
		
		while( columns.get(startPosition).getDistance() >= TRIM_INITIAL_GAP_THREHSOLD && startPosition < columns.size()-1 )
			startPosition++;
		
		while( columns.get(endPosition).getDistance() >= TRIM_INITIAL_GAP_THREHSOLD && endPosition>0)
			endPosition--;
		
		for( int x=startPosition; x <= endPosition; x++)
		{
			sum += columns.get(x).getDistance();
			n++;
		}
			
		return sum/n;
	}
	
	public int getNumRepresentedSequences()
	{
		return n;
	}
	
	public void setMapCount(ProbSequence oldParentSequence1, ProbSequence oldParentSequence2)
	{
		HashSet<String> keys = new HashSet<String>(oldParentSequence1.sampleCounts.keySet());
		keys.addAll(oldParentSequence2.sampleCounts.keySet());
		
		for(String s : keys)
		{
			Integer count1 = oldParentSequence1.sampleCounts.get(s);
			if (count1 == null)
				count1 = 0;
			
			Integer count2 = oldParentSequence2.sampleCounts.get(s);
			if (count2 == null)
				count2 = 0;
			
			this.sampleCounts.put(s, count1 + count2);
		}
	}
	
	/*
	 * Not thread safe and client should not modify contents (but is not prevented from doing so)
	 */
	public List<ProbColumn> getColumns()
	{
		return columns;
	}
	
	public ProbSequence(String s, String sampleID)
	{
		this(s,1, sampleID);
	}
	
	public ProbSequence(String s, int numCopiesDereplicatedSequence, String sampleID)
	{
		for( char c : s.toCharArray())
			this.columns.add(new ProbColumn(c, numCopiesDereplicatedSequence));
		
		n=numCopiesDereplicatedSequence;
		this.sampleCounts.put(sampleID, numCopiesDereplicatedSequence);
	}
	
	public ProbSequence(String s, int numCopiesDereplicatedSequence, HashMap<String, Integer> sampleMap)
		throws Exception
	{
		for( char c : s.toCharArray())
			this.columns.add(new ProbColumn(c, numCopiesDereplicatedSequence));
		
		n=numCopiesDereplicatedSequence;
		this.sampleCounts = sampleMap;
		
		int n=0;
		for( String key : sampleMap.keySet())
			n+= sampleMap.get(key);
		
		if( n != numCopiesDereplicatedSequence)
			throw new Exception("numCopiesDereplicatedSequence must equal sum of sample map");
	}
	
	/*
	 * Does not make a defensive copy of the ProbColumn and is therefore not thread-safe
	 * or immune to client tampering
	 */
	public ProbSequence( List<ProbColumn> columns, int n)
	{
			this.columns = columns;
			this.n = n;
	}
	
	public String getConsensus() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x  < columns.size(); x++)
		{
			buff.append( columns.get(x).getMostFrequentChar() );
		}
		
		return buff.toString();
	}
	
	public String toString()
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		StringBuffer buff = new StringBuffer();
		buff.append(this.n + " sequences with distance " + this.getAverageDistance());
		
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getFractionA()) + " " );
		
		buff.append("\n");
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getFractionC()) + " " );
		
		buff.append("\n");
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getFractionG()) + " " );
		
		buff.append("\n");
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getFractionT()) + " " );
		
		buff.append("\n");
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getFractionGap()) + " " );
		
		buff.append("\n");
		
		for( int x=0; x < columns.size(); x++)
			buff.append( nf.format(columns.get(x).getDistance()) + " " );
		
		return buff.toString();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		ProbSequence probSeq = new ProbSequence("ACGT-", "Sample1");
		System.out.println(probSeq);
	}
}
