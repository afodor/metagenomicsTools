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

package eTree;

import java.io.BufferedWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import probabilisticNW.ProbNW;
import probabilisticNW.ProbSequence;

public class ENode implements Serializable
{
	private static final long serialVersionUID = -3274141668372679156L;
	
	private ProbSequence probSequence;
	private ENode parent;
	private double level;
	private String nodeName;
	private List<ENode> daughters =new ArrayList<ENode>();
	private boolean markedForDeletion = false;
	
	public String getNodeName()
	{
		return nodeName;
	}
	
	public ProbSequence getProbSequence()
	{
		return probSequence;
	}
	
	/*
	 * Wildly un-thread safe!
	 */
	public int attemptDaughterMerge() throws Exception
	{
		int numMerged=0;
		
		for(int x=0; x < this.daughters.size()-1; x++)
		{
			ENode xNode = this.daughters.get(x);
			//System.out.println("Attempting daughter merge for " + xNode.level + " " + xNode.nodeName);
			
			if( ! xNode.markedForDeletion)  for( int y=x+1; y < this.daughters.size(); y++)
			{
				ENode yNode = this.daughters.get(y);
				
				if( ! yNode.markedForDeletion)
				{
					ProbSequence possibleAlignment = ProbNW.align(xNode.getProbSequence(), yNode.getProbSequence());
					
					if( possibleAlignment.getAverageDistance() <= xNode.getLevel() )
					{
						numMerged++;
						yNode.markedForDeletion = true;
						xNode.daughters.addAll(yNode.daughters);
						possibleAlignment.setMapCount(xNode.getProbSequence(), yNode.getProbSequence());
						xNode.setProbSequence(possibleAlignment);
					}
				}
			}
		}
		
		for( ENode d: this.daughters)
			numMerged += d.attemptDaughterMerge();
		
		for( Iterator<ENode> i = this.daughters.iterator(); i.hasNext(); )
		{
			if( i.next().markedForDeletion)
				i.remove();
		}
		
		
		return numMerged;
	}

	public void validateNodeAndDaughters() throws Exception
	{
		//System.out.println("Validating " + this.nodeName);
		this.probSequence.validateProbSequence();
		
		if( markedForDeletion)
			throw new Exception("Node should be deleted " + this.nodeName);
		
		for( ENode d : daughters )
			d.validateNodeAndDaughters();
		
		
		if( daughters.size() > 0)
		{
			double sum =0;
			
			
			double aLevel = daughters.get(0).getLevel();
			
			for( ENode d : daughters)
			{
				if( aLevel != d.level)
					throw new Exception("Unexpected level");
				
				sum += d.getProbSequence().getNumRepresentedSequences();
			}
			

			if( Math.abs(sum - this.getProbSequence().getNumRepresentedSequences()) > 0.0000001 )
				throw new Exception( this.nodeName +  " Unexpected # sequences " + sum + " " + this.getProbSequence().getNumRepresentedSequences());
		}
		
	}
	
	public int getNumOfAllDaughters()
	{
		int sum =daughters.size();
		
		for( ENode subNode : daughters)
			sum += subNode.getNumOfAllDaughters();
		
		return sum;
	}
	
	/*
	 * Attempts to merge the other node to thisNode.
	 * If succesful, daughter nodes are added recursively and true is returned
	 * If unsuccesful, this node is not altered and false is returned
	 */
	public boolean attemptToMergeOtherNodeToThisNode( ENode otherNode ) throws Exception
	{
		if( this.level != otherNode.level )
			throw new Exception("Two nodes must be at same level " + this.level + " " + otherNode.level);
		
		ProbSequence possibleSeq = ProbNW.align(this.probSequence, otherNode.probSequence);
		
		if( possibleSeq.getAverageDistance() <= this.level)
		{
			possibleSeq.setMapCount(this.getProbSequence(), otherNode.getProbSequence());
			this.setProbSequence(possibleSeq);
			for( ENode otherDaughter : otherNode.daughters )
			{
				boolean merged = false;
				
				for( ENode thisDaughter : this.daughters )
				{
					if( ! merged)
					{
						merged = thisDaughter.attemptToMergeOtherNodeToThisNode(otherDaughter);
					}
				}
				
				if( ! merged)
					this.daughters.add(otherDaughter);
			}
			
			return true;
		}
		
		return false;
	}
	
	public void writeNodeAndDaughters(BufferedWriter writer, boolean detailed) throws Exception
	{
		int level = ETree.getIndex(this.level);
		String tabString ="";
		
		for (int x=1; x <= level; x++)
			tabString += " ";
		writer.write(tabString +  this.nodeName + " (" + level + ") " + this.level + " " + this.probSequence.getNumRepresentedSequences() + " seqs with " +  this.daughters.size() + " children ");
		
		this.probSequence.writeThisSequenceToText(writer, "\t" + tabString, detailed);
		
		for( ENode enode : daughters)
			enode.writeNodeAndDaughters(writer, detailed);
		
	}
	
	public int getNumOfSequencesAtTips()
	{
		int sum =0;
		
		if( daughters.size() == 0 )
			sum = this.probSequence.getNumRepresentedSequences();
		
		for( ENode subNode : daughters)
			sum += subNode.getNumOfSequencesAtTips();
		
		return sum;
	}

	public ENode getParent()
	{
		return parent;
	}

	public double getLevel()
	{
		return level;
	}
	
	public void setProbSequence(ProbSequence probSequence)
	{
		this.probSequence = probSequence;
	}

	public List<ENode> getDaughters()
	{
		return daughters;
	}
	
	public ENode( ProbSequence probSequence, String name, double level, ENode parent )
	{
		this.probSequence = probSequence;
		this.nodeName = name;
		this.parent = parent;
		this.level = level;
	}
}
