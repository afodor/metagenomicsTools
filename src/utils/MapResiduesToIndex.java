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


package utils;

import java.util.*;

public class MapResiduesToIndex
{
	public static Random random = new Random(System.currentTimeMillis());
	
	public static final char[] residues = new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 
		'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
						 'V', 'W', 'Y' };
	
	public static int NUM_VALID_RESIDUES = residues.length;	
	
	public static final Character[] charResidues = new Character[]
			{
				new Character('A'), 
				new Character('C'), 
				new Character('D'), 
				new Character('E'), 
			    new Character('F'),
				new Character('G'), 
				new Character('H'),
				new Character('I'), 
				new Character('K'), 
				new Character('L'), 
				new Character('M'), 
				new Character('N'), 
				new Character('P'),
				new Character('Q'), 
				new Character('R'), 
				new Character('S'), 
				new Character('T'),
				new Character('V'),
				new Character('W'),
				new Character('Y')			 
			};
	
	public static char getChar( int i ) throws Exception
	{
		return residues[i];
	}
	
	public static char getRandomChar() throws Exception
	{
		return residues[ random.nextInt( residues.length ) ];
	}
	
	public static char getRandomCharFromDistribution(float[] distribution ) throws Exception
	{
		if ( distribution.length != MapResiduesToIndex.NUM_VALID_RESIDUES ) 
			throw new Exception("Unexpected length");
		
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		for ( int x=0; x< distribution.length; x++)
		{
			sum+= distribution[x];
			
			if ( randomFloat <= sum ) 
				return residues[x];
		}
		
		return residues[MapResiduesToIndex.NUM_VALID_RESIDUES -1];
	}
	
	public static Character getChararcter( int i ) throws Exception
	{
		return charResidues[i];
	}
	
	public static int getIndex(char c) throws Exception
	{
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c ) 
				return x;
		
		throw new Exception("Unknown character " + c );
	}
	
	public static int getIndex(String c) throws Exception
	{
		if ( c.length() != 1) 
			throw new Exception("Error!  Expecting a length of one");
		
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c.charAt(0) ) 
				return x;
		
		throw new Exception("Unknown character " + c );
	}
	
	
	public static boolean isValidResidueChar( char c ) 
	{
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c ) 
				return true;
		
		return false;
	}
	
	public static boolean isVaildThreeResidueString( String aString ) 
	{
		try
		{
			SequenceUtils.threeToOne(aString);		
		}
		catch(Exception e ) 
		{
			return false;
		}
		
		return true;
	}
}
