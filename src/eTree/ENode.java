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
	private List<ENode> daughters =new ArrayList<ENode>();
	
	public ProbSequence getProbSequence()
	{
		return probSequence;
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
	
	public ENode( ProbSequence probSequence, double level, ENode parent )
	{
		this.probSequence = probSequence;
		this.parent = parent;
		this.level = level;
	}

	public ENode( String sequence, double level, ENode parent )
	{
		if( level !=0 )
			this.probSequence = new ProbSequence(sequence);
		else
			this.probSequence = null;
		
		this.parent = parent;
		this.level = level;
	}
}
