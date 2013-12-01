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

import java.io.Serializable;

public class ProbColumn implements Serializable
{
	private static final long serialVersionUID = 397594062593525001L;
	
	private static final int A_INDEX =0;
	private static final int C_INDEX =1;
	private static final int G_INDEX =2;
	private static final int T_INDEX =3;
	private static final int GAP_INDEX =4;
	private final int counts[];
	
	private double distance =0;
	
	
	/*
	 * Makes a deep copy
	 */
	public ProbColumn(ProbColumn otherColumn)
	{
		this(otherColumn.getCounts(), otherColumn.distance);
	}
	
	private ProbColumn(int[] counts, double distance)
	{
		this.counts = counts;
		this.distance = distance;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	/*
	 * Makes a copy of the counts array, so is potentially expensive
	 */
	public int[] getCounts()
	{
		int[] newArray = new int[counts.length];
		
		for( int x=0 ; x < newArray.length; x++)
			newArray[x] = counts[x];
		
		return  newArray;
	}
	
	public char getMostFrequentChar() throws Exception
	{
		double max = Math.max(counts[A_INDEX], counts[C_INDEX]);
		max = Math.max(max, counts[G_INDEX]);
		max = Math.max(max, counts[T_INDEX]);
		max = Math.max(max, counts[GAP_INDEX]);
		
		if( max == counts[A_INDEX])
			return 'A';
		else if( max == counts[C_INDEX])
			return 'C';
		else if (max == counts[G_INDEX])
			return 'G';
		else if( max == counts[T_INDEX])
			return 'T';
		else if( max == counts[GAP_INDEX])
			return '-';
		
		throw new Exception("Logic error");
	}
	
	public double getScoreDiag(ProbColumn other, double match, double mismatch, double gapPenalty)
	{
		double score =0;
		
		double thisNum = this.getTotalNum();
		double otherNum = other.getTotalNum();
		
		for (int x=0; x<= 3; x++)
			score += (this.counts[x] / thisNum) * (other.counts[x]/ otherNum) * match;
		
		for( int x=0; x <=3; x++)
			for( int y=0; y <=3; y++)
				if( x != y)
					score += (this.counts[x]/thisNum) * (other.counts[y]/otherNum) * mismatch;
		
		score += (this.counts[GAP_INDEX] / thisNum + other.counts[GAP_INDEX] / otherNum) * gapPenalty /2;
		return score;
	}
	
	/*
	 * Makes and returns a new column based on a merge of this column and 
	 * the otherColumn
	 */
	public ProbColumn merge( ProbColumn otherColumn)
	{
		int[] counts = new int[5];
		double distance = 0;
		
		for( int x=0; x < 5; x++)
		{
			counts[x] = this.counts[x] + otherColumn.counts[x];
		}
		
		for( int x=0; x < 5; x++)
			for (int y= 0; y < 5; y++)
				if( x != y )
					distance += (this.counts[x]/this.getTotalNum()) * (otherColumn.counts[y]/otherColumn.getTotalNum());
		
		return new ProbColumn(counts, distance);
	}
	
	public double getFractionA()
	{
		return counts[A_INDEX] / getTotalNum();
	}
	
	public double getFractionC()
	{
		return counts[C_INDEX]  / getTotalNum();
	}
	
	public double getFractionG()
	{
		return counts[G_INDEX]  / getTotalNum();
	}
	
	public double getFractionT()
	{
		return counts[T_INDEX]  / getTotalNum();
	}
	

	public double getFractionGap()
	{
		return counts[GAP_INDEX] / getTotalNum();
	}
	
	public double getTotalNum()
	{
		int sum=0; 
		
		for( int x=0; x < 5; x++ )
			sum += counts[x];
		
		return sum;
	}
	
	
	public ProbColumn(char c) throws Exception
	{
		this(c,1);
	}
	
	public ProbColumn(char c, int n) throws Exception
	{
		if( n < 0 )
			throw new Exception("Can't make column with count " + n);
		
		counts= new int[5];
		
		this.addChar(c,n);
	}
	
	@Override
	public String toString()
	{
		return "["+ counts[0] + "," + counts[1] + "," + counts[2] + "," + counts[3]+ "," + 
						counts[4] + "]";
	}
	
	/*
	 * Non A,C,G,T and - are ignored
	 */
	public void addChar(char c, int n) throws Exception
	{
		if( c == 'A')
			counts[A_INDEX]+=n;
		else if ( c == 'C')
			counts[C_INDEX]+=n;
		else if ( c == 'G')
			counts[G_INDEX]+=n;
		else if ( c == 'T')
			counts[T_INDEX]+=n;
		else if ( c == '-')
			counts[GAP_INDEX]+=n;
		else throw new Exception("Invalid character");
	}
}
