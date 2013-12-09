/** 
 * Author:  anthony.fodor@gmail.com
 * 
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

import java.util.Comparator;

public class KmerQueryResultForProbSeq implements Comparable<KmerQueryResultForProbSeq>
{
	private ProbSequence alignSeq;
	private final ProbSequence probSeq;
	private final int counts;
	
	public void setAlignSeq(ProbSequence alignSeq)
	{
		this.alignSeq = alignSeq;
	}
	
	public ProbSequence getAlignSeq()
	{
		return alignSeq;
	}
	
	public ProbSequence getProbSeq()
	{
		return probSeq;
	}
	
	public int getCounts()
	{
		return counts;
	}
	
	public static class SortByNumSequences implements Comparator<KmerQueryResultForProbSeq>
	{
		@Override
		public int compare(KmerQueryResultForProbSeq arg0,
				KmerQueryResultForProbSeq arg1)
		{
			return arg1.getProbSeq().getNumRepresentedSequences() - arg0.getProbSeq().getNumRepresentedSequences();
		}
	}
	
	public KmerQueryResultForProbSeq(ProbSequence probSeq, int counts)
	{
		this.probSeq=probSeq;
		this.counts = counts;
	}

	@Override
	public String toString()
	{
		return this.probSeq.toString() + " " + this.counts + "  hits ";
	}
	
	@Override
	public int compareTo(KmerQueryResultForProbSeq o)
	{
		return o.counts - this.counts;
	}
}
