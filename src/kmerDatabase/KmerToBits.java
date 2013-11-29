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

package kmerDatabase;

public class KmerToBits
{
	private static final int A_INT = 0x0000;
	private static final int C_INT = 0x0001;
	private static final int G_INT = 0x0002;
	private static final int T_INT = 0x0003;
	private int bits = 0x0000;
	private final String s;
	private int index=0;
	
	public static float KMER_SIZE =16;
	
	public KmerToBits(String stringToHash) throws Exception
	{
		this.s =stringToHash;
	
		if(s.length() < KMER_SIZE)
			throw new Exception("String must be longer than " + KMER_SIZE);
		
		for( int x=0; x < 16; x++)
		{
			addChar(s.charAt(x));
			index++;
		}	
	}
	
	public void advance() throws Exception
	{
		addChar(s.charAt(index));
		index++;
	}
	
	public boolean canAdvance()
	{
		return index < s.length()-1;
	}
	
	public int getCurrentIndex()
	{
		return index;
	}
	
	public int getHashAtCurrentPosition()
	{
		return bits;
	}
	
	private void addChar( char c ) throws Exception
	{
		int mask = getMask(c);
		bits = bits << 2;
		bits = bits | mask;
	}
	
	private int getMask(char c) throws Exception
	{
		if( c == 'A' )
			return A_INT;
			
		if( c == 'C' )
			return C_INT;
			
		if( c == 'G' )
			return G_INT;
			
		if( c == 'T' )
			return T_INT;
			
		throw new Exception("Invalid char " + c);
	}
	
	public static void main(String[] args) throws Exception
	{
		String s = "ACCAAGGGGAACCCGGTTTTAAAATTTTA";
		
		KmerToBits kmers = new KmerToBits(s);
		
		System.out.println( kmers.getHashAtCurrentPosition());
		while( kmers.canAdvance())
		{
			kmers.advance();
			System.out.println( kmers.getHashAtCurrentPosition());
		}
	}
}
