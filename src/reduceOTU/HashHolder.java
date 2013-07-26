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

package reduceOTU;

public class HashHolder
{
	private final int wordSize;
	
	public int getTargetChars()
	{
		return wordSize;
	}
	
	private long bits = 0x0l;
	
	public long getBits()
	{
		return bits;
	}
	
	// the index of the current string
	private int index =0;
	
	public int getIndex()
	{
		return index;
	}
	
	private int numValidChars=0;
	
	public int getNumValidChars()
	{
		return numValidChars;
	}
	
	private String s = null;
	
	private static final Long A_LONG = new Long(0x0000l);
	private static final Long C_LONG = new Long(0x0001l);
	private static final Long G_LONG = new Long(0x0002l);
	private static final Long T_LONG = new Long(0x0003l);
	
	private final Long A_SHIFT;
	private final Long C_SHIFT;
	private final Long G_SHIFT;
	private final Long T_SHIFT;	
	
	public boolean setToString(String s) throws Exception
	{
		return setToString(s, true);
	}
	
	private boolean setToString(String s, boolean startAtZero) throws Exception
	{		
		this.s = s;
		
		if(startAtZero)
		{
			index=-1;
		}
		
		while( canStillRead() && numValidChars < wordSize)
		{
			index++;
			
			if( canStillRead())
			{
				boolean isValidChar = add( s.charAt(index) );
				
				if( ! isValidChar )
				{
					numValidChars=0;
					bits = 0x0000;
				}
				else
				{
					numValidChars++;
				}
			}
		}
		
		return numValidChars >= wordSize;
	}
	
	
	private boolean canStillRead()
	{
		return index < s.length();
	}
	
	public String getSequence() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		long val = bits;
		
		for( int x=0; x < wordSize; x++)
		{
			buff.append( getChar(val& this.T_SHIFT) );
			val = val << 2;
		}
		
		return buff.toString();
	}
	
	
	private char getChar(long val) throws Exception
	{
		if( val==this.A_SHIFT)
			return 'A';
		if( val == this.C_SHIFT)
			return 'C';
		if( val == this.G_SHIFT)
			return 'G';
		if(val== this.T_SHIFT)
			return 'T';
		
		throw new Exception("Unknown " + val);
	}
	
	public boolean advance() throws Exception
	{
		index++;
		
		if( ! canStillRead())
			return false;
		
		if(add( s.charAt(index) ))
		{
			numValidChars++;
			return true;
		}
			
		numValidChars=0;
		bits=0x0000;
		
		return this.setToString(s,false);
	}
	
	public HashHolder(int wordSize) throws Exception
	{
		if(wordSize> 32 || wordSize< 2)
			throw new Exception("Expecting word size between 2 and 32");
		
		int shift = (wordSize-1) * 2;
		
		this.A_SHIFT = A_LONG << shift;
		this.C_SHIFT = C_LONG << shift;
		this.G_SHIFT = G_LONG << shift;
		this.T_SHIFT = T_LONG << shift;
			
		this.wordSize= wordSize;
	}
	
	private boolean add(char newChar) throws Exception
	{
		Long mask = getMaskOrNull(newChar);
		
		if( mask == null)
			return false;
		
		bits = bits << 2;
		
		bits = bits | mask;
		
		return true;
	}
	
	private Long getMaskOrNull(char c)
	{
		
		if( c == 'T' || c =='t' )
			return T_LONG;
		
		if( c == 'G' || c =='g' )
			return G_LONG;
		
		if( c == 'C' || c =='c' )
			return C_LONG;
		
		if( c == 'A' || c == 'a')
			return A_LONG;
		
		return null;
		
	}
}
