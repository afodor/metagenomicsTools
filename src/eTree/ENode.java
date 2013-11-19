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

import java.util.ArrayList;
import java.util.List;

import probabilisticNW.ProbSequence;

public class ENode
{
	private ProbSequence probSequence;
	private ENode parent;
	private double level;
	private String nodeName;
	private List<ENode> daughters =new ArrayList<ENode>();
	
	public String getNodeName()
	{
		return nodeName;
	}
	
	public ProbSequence getProbSequence()
	{
		return probSequence;
	}
	
	public int getNumOfAllDaughters()
	{
		int sum =daughters.size();
		
		for( ENode subNode : daughters)
			sum += subNode.getNumOfAllDaughters();
		
		return sum;
	}
	
	public int getNumOfSequencesAtTip()
	{
		int sum =0;
		
		if( daughters.size() == 0 )
			sum = this.probSequence.getNumRepresentedSequences();
		
		for( ENode subNode : daughters)
			sum += subNode.getNumOfSequencesAtTip();
		
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

	public ENode( String sequence, String name, double level, ENode parent )
	{
		if( level !=0 )
			this.probSequence = new ProbSequence(sequence);
		else
			this.probSequence = null;
		
		this.nodeName = name;
		this.parent = parent;
		this.level = level;
	}
}
