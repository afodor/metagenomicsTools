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
	
	public double getRawDistance(ContextCount other)
	{
		double sum =0;
		
		sum += (this.numA - other.numA ) * (this.numA - other.numA );
		sum += (this.numC - other.numC ) * (this.numC - other.numC );
		sum += (this.numG - other.numG ) * (this.numG - other.numG );
		sum += (this.numT - other.numT ) * (this.numT - other.numT );
		
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
		HashSet<Character> set = new HashSet<>();
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
