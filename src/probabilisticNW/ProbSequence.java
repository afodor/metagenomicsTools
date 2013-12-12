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

/*
 * Do not override equals or hashmap (will break KmerDatabaseFroProbSeq)
 */
public class ProbSequence implements Serializable
{
	private static final long serialVersionUID = -8572579288693934310L;

	public static double TRIM_INITIAL_GAP_THREHSOLD = 0.25;
	
	private List<ProbColumn> columns = new ArrayList<ProbColumn>();
	private int n=0;
	private HashMap<String, Integer> sampleCounts= new HashMap<String, Integer>();
	private double alignmentScore = Double.MIN_VALUE;
	
	public void replaceWithDeepCopy(ProbSequence otherSequence)
	{
		otherSequence = makeDeepCopy(otherSequence);
		this.columns = otherSequence.columns;
		this.n = otherSequence.n;
		this.sampleCounts = otherSequence.sampleCounts;
		this.alignmentScore = otherSequence.alignmentScore;
	}
	
	public static ProbSequence makeDeepCopy(ProbSequence probSequence)
	{
		List<ProbColumn> list= new ArrayList<ProbColumn>();
		
		for( ProbColumn pc : probSequence.columns)
			list.add(new ProbColumn(pc));
		
		ProbSequence ps = new ProbSequence(list, probSequence.n);
		
		HashMap<String,Integer> newMap = new HashMap<String, Integer>();
		for(String s: probSequence.sampleCounts.keySet())
			newMap.put(s, probSequence.sampleCounts.get(s));
		ps.sampleCounts = newMap;
		ps.n = probSequence.n;
		ps.alignmentScore = probSequence.alignmentScore;
		return ps;
	}
	
	public double getAlignmentScore()
	{
		return alignmentScore;
	}
	
	public double getAlignmentScoreAveragedByCol()
	{
		return alignmentScore/this.columns.size();
	}
	
	public void setAlignmentScore(double alignmentScore)
	{
		this.alignmentScore = alignmentScore;
	}
	
	public void validateProbSequence() throws Exception
	{
		if( n < 1)
			throw new Exception("ProbSequence must represent at least one sequence " + n);
		
		int aSum =0;
		
		for(  String s: sampleCounts.keySet())
			aSum += sampleCounts.get(s);
		
		if( aSum != n)
			throw new Exception("Wrong number of sequences " + n + " " + aSum);
		
		for( ProbColumn pc : columns )
			if(  Math.abs(pc.getTotalNum() -aSum) > 0.0000001 )
				System.out.println( "Wrong number of sequences " + n + " " + pc.getTotalNum());
	}
	
	
	/*
	 * tabString is prefixed to each output line (to allow for consistent tabing).
	 * (Set tabString to "" disable)
	 */
	public void writeThisSequenceToText( BufferedWriter writer,  String tabString , boolean detailed)
		throws Exception
	{
		writer.write(tabString +  sampleCounts.toString() + "\n");
		
		if(detailed) 
		{
			writer.write(tabString + getConsensus().toString() + "\n");
			
			for( int x=0; x < this.columns.size(); x++)
			{
				ProbColumn probC = this.columns.get(x);
				boolean foundNonOneZero = false;
				
				for( int y=0;y <=4; y++)
				{
					boolean zeroOrOne = 
							Math.abs( probC.getCounts()[y] - 0) <= 0.00000000001 
									|| Math.abs( probC.getCounts()[y] - probC.getTotalNum()) <= 0.00000000001 ;
					if( ! zeroOrOne  )  
						foundNonOneZero = true;
				}
					
				if( foundNonOneZero )
				{
					writer.write( probC.getTotalNum() + " divided as " +  "char " + x + " " + probC.getCounts()[0]  + " " + probC.getCounts()[1]  
							+ " " + probC.getCounts()[2] + " " + probC.getCounts()[3] + " " + probC.getCounts()[4] 
									+ " " + probC.getFractionGap() + " " + probC.getDistance() + "\n");
				}
			}
			
		}
		
		
		writer.write("\n");
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
		int sum =0;
		HashMap<String, Integer> newMap = new HashMap<String, Integer>();
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
			
			int added = count1 + count2;
			sum+=added;
			newMap.put(s, added);
		}
		
		this.sampleCounts = newMap;
		this.n = sum;
	}
	
	/*
	 * Not thread safe and client should not modify contents (but is not prevented from doing so)
	 */
	public List<ProbColumn> getColumns()
	{
		return columns;
	}
	
	public ProbSequence(String s, String sampleID) throws Exception
	{
		this(s,1, sampleID);
	}
	
	public ProbSequence(String s, int numCopiesDereplicatedSequence, String sampleID) throws Exception
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
	

	public String getConsensusUngapped() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x  < columns.size(); x++)
		{
			buff.append( columns.get(x).getMostFrequentChar() );
		}
		
		return buff.toString().replaceAll("-", "");
	}
	
	public String toString()
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		StringBuffer buff = new StringBuffer();
		buff.append(this.n + " sequences with distance " + this.getAverageDistance());
		//buff.append( getConsensus());
	
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
