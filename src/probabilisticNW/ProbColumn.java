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

public class ProbColumn
{
	private int numA=0;
	private int numC=0;
	private int numG=0;
	private int numT=0;
	private int numGap=0;
	private int totalNum =0;
	
	public float getFractionA()
	{
		return ((float)numA) / totalNum;
	}
	
	public float getFractionC()
	{
		return ((float)numC) / totalNum;
	}
	
	public float getFractionG()
	{
		return ((float)numG) / totalNum;
	}
	
	public float getFractionT()
	{
		return ((float)numT) / totalNum;
	}
	

	public float getFractionGap()
	{
		return ((float)numGap) / totalNum;
	}
	
	public int getTotalNum()
	{
		return totalNum;
	}
	
	public int getNumA()
	{
		return numA;
	}

	public int getNumC()
	{
		return numC;
	}

	public int getNumG()
	{
		return numG;
	}

	public int getNumT()
	{
		return numT;
	}

	public int getNumGap()
	{
		return numGap;
	}

	public ProbColumn()
	{
		
	}
	
	public ProbColumn(char c)
	{
		this.addChar(c);
	}
	
	@Override
	public String toString()
	{
		return "["+ getFractionA() + "," + getFractionC() + "," + getFractionG() + "," + getFractionT() + "," + 
						getFractionGap() + "]";
	}
	
	/*
	 * Non A,C,G,T and - are ignored
	 */
	public void addChar(char c)
	{
		totalNum++;
		
		if( c == 'A')
			numA++;
		else if ( c == 'C')
			numC++;
		else if ( c == 'G')
			numG++;
		else if ( c == 'T')
			numT++;
		else if ( c == '-')
			numGap++;
	}
	
	
}
