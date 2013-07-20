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


package coPhylog;

import java.util.HashSet;

public class ContextCount
{
	private byte numA=-128;
	private byte numC=-128;
	private byte numG=-128;
	private byte numT=-128;
	
	public ContextCount( byte numA, byte numC, byte numG, byte numT )
	{
		this.numA = numA;
		this.numC = numC;
		this.numG = numG;
		this.numT = numT;
	}
	
	public ContextCount( int numA, int numC, int numG, int numT) throws Exception
	{
		if( (numA-128) > Byte.MAX_VALUE  || (numC-128) > Byte.MAX_VALUE 
				|| (numG-128) > Byte.MAX_VALUE || (numT-128) > Byte.MAX_VALUE)
			throw new Exception("All must be below " +  Byte.MAX_VALUE + " " + numA + " " + numC + " " + numG + " "+ numT);
		

		if( numA < 0|| numC < 0|| numG < 0|| numT < 0)
			throw new Exception("No negative numbers");
		
		this.numA = (byte) (numA - 128);
		this.numC = (byte) (numC - 128);
		this.numG = (byte) (numG - 128);
		this.numT = (byte) (numT - 128);
	}
	
	public double getRawDistance(ContextCount other)
	{
		double sum =0;
		
		sum += (this.getNumA() - other.getNumA() ) * (this.getNumA() - other.getNumA() );
		sum += (this.getNumC() - other.getNumC() ) * (this.getNumC() - other.getNumC() );
		sum += (this.getNumG() - other.getNumG() ) * (this.getNumG() - other.getNumG() );
		sum += (this.getNumT() - other.getNumT() ) * (this.getNumT() - other.getNumT() );
		
		return Math.sqrt(sum);
		
	}
	
	public boolean isDifferentInHighest( ContextCount other ) 
	{
		HashSet<Character> thisHighest = getHighest();
		HashSet<Character> otherHighest = other.getHighest();
		
		for( Character c : thisHighest)
			if( otherHighest.contains(c))
				return false;
		
		return true;
	}
	
	public HashSet<Character> getHighest()
	{
		HashSet<Character> set = new HashSet<Character>();
		int val = -128;
		
		if( numA > val )
		{
			set.add('A');
			val = numA;
		}
		
		if( numC >= val)
		{
			if( numC > val )
				set.clear();
			
			set.add('C');
			val = numC;
		}
		
		if( numG >= val)
		{
			if( numG > val )
				set.clear();
			
			set.add('G');
			val = numG;
		}
		
		if( numT >= val)
		{
			if( numT > val )
				set.clear();
			
			set.add('T');
			val = numT;
		}
		
		return set;
	}
	
	public ContextCount()
	{
		
	}
	
	public void incrementA()
	{
		if( numA <= 127 )
			numA++;
	}
	
	public void incrementC()
	{
		if( numC <= 127)
			numC++;
	}
	
	public void incrementG()
	{
		if( numG <= 127)
			numG++;
	}
	
	public void incrementT()
	{
		if( numT<=127)
			numT++;
	}
	
	
	public boolean isSingleton()
	{
		if( numA > -127 )
			return false;

		if( numC > -127 )
			return false;
		
		if( numG > -127 )
			return false;

		if( numT > -127 )
			return false;
		
		return true;
	}
	
	public void increment(char c)
	{
		if( c == 'A' )
			incrementA();
		else if ( c== 'C')
			incrementC();
		else if( c == 'G')
			incrementG();
		else if( c == 'T')
			incrementT();
	}
	
	public int getSum()
	{
		return getNumA() + getNumC() + getNumG() + getNumT();
	}
	
	public int getMax()
	{
		int val = getNumA();
		
		val = Math.max(val, getNumC());
		val = Math.max(val, getNumG());
		val = Math.max(val, getNumT());
		
		return val;
	}

	public int getNumA()
	{
		return numA+128;
	}

	public int getNumC()
	{
		return numC+128;
	}

	public int getNumG()
	{
		return numG+128;
	}

	public int getNumT()
	{
		return numT+128;
	}
	
	public byte getAAsByte()
	{
		return numA;
	}
	
	public byte getCAsByte()
	{
		return numC;
	}
	
	public byte getGAsByte()
	{
		return numG;
	}
	
	public byte getTAsByte()
	{
		return numT;
	}

	@Override
	public String toString()
	{
		return
		"[" + (numA+128) + "," + (numC+128) + "," + (numG+128) + "," + (numT+128) + "]";
	}
	
	
}
