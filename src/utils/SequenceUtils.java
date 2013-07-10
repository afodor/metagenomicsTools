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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.StringTokenizer;

public class SequenceUtils
{
		private static float[] maxSurfaceAreas = { 
		188.789f,
		200.211f,
		226.145f,
		265.54f,
		301.605f,
		155.998f,
		246.736f,
		247.242f,
		291.399f,
		254.034f,
		280.255f,
		238.508f,
		216.448f,
		252.54f,
		309.349f,
		196.96f,
		215.754f,
		223.66f,
		281.692f,
		300.091f };

	public static float getPercentIdentity(String string1, String string2) 
		throws Exception
	{
		if ( string1.length() != string2.length())
			throw new Exception("Unequal lengths " + 
							string1.length() + " vs " + string2.length() );
		
		int numIdentical = 0;
		
		string1 = string1.toUpperCase();
		string2 = string2.toUpperCase();
		
		for ( int x=0; x < string1.length(); x++ )
			if ( string1.charAt(x)  == string2.charAt(x))
				numIdentical++;
			
		return 100f * ((float)numIdentical) / string1.length(); 
	}
		
	/**  returns either the aa, * for stop or ? for unknown
	 */
	public static char getSafeTranslationChar(String codon ) throws Exception
	{
		if ( codon.length() != 3 ) 
			throw new Exception("Codon should be three characters");
		
		if ( Translate.isValidCodon(codon) ) 
		{
			if ( Translate.isStopCodon(codon))
				return '*';
			else
				return SequenceUtils.threeToOne(  Translate.translate(codon)).charAt(0);
		}
			
		return '?';	
	}
		
	public static int getExactlyOneIndex(String fullString, String subString)
		throws Exception
	{
		int firstIndex = fullString.indexOf(subString);
		
		if ( firstIndex == -1)
			return -1;
			
		int lastIndex = fullString.lastIndexOf(subString);
		
		if ( firstIndex == lastIndex)
			return firstIndex;
			
		throw new Exception("At least two matches" 
				+ firstIndex + " " + lastIndex);
	}
		
	public static char flip ( char inChar ) throws Exception
	{
		if ( inChar == 'A' ) 
			return 'T';
			
		if ( inChar == 'C' ) 
			return 'G';
			
		if ( inChar == 'G' ) 
			return 'C';
			
		if ( inChar == 'T' ) 
			return 'A';
			
		if ( inChar == 'N' ) 
			return 'N';
			
		throw new Exception("Unknown character " + inChar );		
	}
	
	public static String getRandomDnaNMer(int n) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for (int x=1; x<=n; x++ )
			buff.append(Translate.getRandomNucleotide());
		
		return buff.toString();
	}
	
	public static String maskLowercase( String inString ) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for (int x=0; x < inString.length(); x++ ) 
		{
			char c = inString.charAt(x);
			
			if  ( Character.isLowerCase(c) )
				buff.append("N");
			else
				buff.append(c);
		}
		
		return buff.toString();
	}
					
	// static access only
	private SequenceUtils()
	{
	}
	
	public static String getMiddleLine( String topString, String bottomString ) throws Exception
	{
		topString = topString.trim();
		bottomString = bottomString.trim();
		StringBuffer buff = new StringBuffer();
		
		if ( topString.length() != bottomString.length() ) 
			throw new Exception("Expecting equal length strings! " + topString + " " + bottomString + " " + 
		topString.length() + " " + bottomString.length() );
			
		for ( int x=0; x < topString.length(); x++ )
		{
			char c1 = topString.charAt(x);
			char c2= bottomString.charAt(x);
			
			if ( c1 == c2 ) 
				buff.append(c1);
			else
				buff.append("-");				
		}	
		
		return buff.toString();
	}
	
	/** Strips out all but A, C, G, T
	 */
	public static String getValidDnaChars( String inString ) 
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x < inString.length(); x++ ) 
		{
			char c = inString.charAt(x);
			
			if ( c == 'A' || c == 'C' || c == 'G' || c == 'T' ) 
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	/** Strips out all but A, C, G, T, N
		 */
	public static String getValidDnaCharsOrN( String inString ) 
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x < inString.length(); x++ ) 
		{
			char c = inString.charAt(x);
			
			if ( c == 'A' || c == 'C' || c == 'G' || c == 'T' || 
							c == 'N' ) 
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	public static boolean isAllValidUpperCaseDnaCharsOrN( String inString )
	{
		for ( int x=0; x < inString.length(); x++  )
			if ( ! isValidUpperCaseDnaCharOrN(inString.charAt(x)) ) 
				return false;
		
		return true;
	}
	
	public static boolean isValidDnaChar( char c ) 
	{
		if ( c == 'A' || c == 'C' || c == 'G' || c == 'T' 
				|| c == 'a' || c == 'c' || c == 'g' || c == 't' ) 
					return true;
			
		return false;
	}
	
	public static boolean isValidUpperCaseDnaCharOrN( char c ) 
	{
		if ( c == 'A' || c == 'C' || c == 'G' || c == 'T' || c =='N' ) 
					return true;
			
		return false;
	}
		
	public static void main(String[] args) throws Exception
	{
		for ( int x=0; x< MapResiduesToIndex.NUM_VALID_RESIDUES; x++ ) 
		{
			System.out.println( MapResiduesToIndex.getChar(x) + " " + getMaxSurfaceArea(x) );
		}
	}
	
	public static float getMaxSurfaceArea(int index)
	{
		return maxSurfaceAreas[index];	
	}
	
	public static boolean isHydrophicChar( char inChar ) 
	{
		if ( inChar == 'A' || inChar == 'V' || inChar == 'L' 
				|| inChar == 'I' || inChar =='F' || inChar =='W' 
				|| inChar == 'M' || inChar == 'P') 
					return true;
					
		return false;
	}
	
	public static String stripSpaces( String inString ) 
	{
		StringBuffer buff = new StringBuffer();
		
		StringTokenizer sToken = new StringTokenizer( inString );
		
		while ( sToken.hasMoreElements() )
			buff.append(sToken.nextToken());
		
		return buff.toString();	
	}
	
	public static String stripTabs( String inString, String newString ) 
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x < inString.length(); x++)
		{
			char c= inString.charAt(x);
			
			if( c == '\t' )
				buff.append(newString);
			else 
				buff.append(c);
		}
		
		return buff.toString();	
	}
	
	public static boolean pairIsHydrophobic( char inChar1, char inChar2 ) 
	{
		if ( isHydrophicChar(inChar1) && isHydrophicChar(inChar2) ) 
			return true;
			
		return false;	
	}
	
	public static String buildFasta(String sequence1, String sequence2 ) 
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append(">1\n");
		buff.append( sequence1 + "\n" );
		buff.append(">2\n");
		buff.append( sequence2 + "\n" );
		
		
		return buff.toString();	
	}
	
	
	public static String fileToString( File file ) throws Exception
	{
		BufferedReader reader = new BufferedReader( new FileReader( file ));
		StringWriter writer = new StringWriter();
		
		char[] c = new char[2048];
        int bytesRead;
        
        while ( ( bytesRead = reader.read(c,0,c.length)) != -1 )
			writer.write( c, 0, bytesRead );
		
        writer.close();  reader.close();
		return writer.toString();
	}
	
	public static int threeToIndex( String inString ) throws Exception
	{
		return MapResiduesToIndex.getIndex( threeToOne( inString ) );
	}
	
	
	public static String threeToOne( String inString ) throws Exception
	{
		inString = inString.toUpperCase();
		
		if ( inString.equals("GLY") ) 
			return "G";
		
		if ( inString.equals("ALA" )) 
			return "A";
		
		if ( inString.equals("VAL")) 
			return "V";
		
		if ( inString.equals("LEU")) 
			return "L";
		
		if ( inString.equals("ILE" )) 
			return "I";
		
		if ( inString.equals("PHE" )) 
			return "F";
		
		if ( inString.equals("TYR" )) 
			return "Y";
		
		if ( inString.equals("TRP" )) 
			 return "W";
		
		if ( inString.equals("SER" )) 
			return "S";
		
		if ( inString.equals("THR" )) 
			return "T";
		
		if ( inString.equals("MET" )) 
			return "M";
		
		if ( inString.equals("CYS" )) 
			return "C";
		
		if ( inString.equals("ASN" )) 
			return "N";
		
		if ( inString.equals("GLN" )) 
			return "Q";
	
		if ( inString.equals("PRO" ))
			return "P";
		
		if ( inString.equals("ASP" )) 
			return "D";
		
		if ( inString.equals("GLU" ))
			 return "E";
		
		if ( inString.equals("LYS" )) 
			return "K";
		
		if ( inString.equals("ARG" ))
			return "R";
		
		if ( inString.equals("HIS" ))
			return "H";
		
		throw new Exception("Unexpected token '" + inString + "'" );
	}
}
