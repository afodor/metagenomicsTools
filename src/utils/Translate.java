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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

public class Translate
{
	private static final int NUM_PROTEIN_RESIDUES_PER_LINE=15;
	//private static final Random random= new Random(39425435);
	private static final Random random= new Random(System.currentTimeMillis());
	
	private static String translationString = 
	 "TTT Phe TCT Ser TAT Tyr TGT Cys " + 
	 "TTC Phe TCC Ser TAC Tyr TGC Cys " + 
     "TTA Leu TCA Ser TAA STOP TGA STOP " + 
     "TTG Leu TCG Ser TAG STOP TGG Trp " + 
	 "CTT Leu CCT Pro CAT His CGT Arg " + 
     "CTC Leu CCC Pro CAC His CGC Arg " + 
     "CTA Leu CCA Pro CAA Gln CGA Arg " + 
     "CTG Leu CCG Pro CAG Gln CGG Arg " + 
     "ATT Ile ACT Thr AAT Asn AGT Ser " + 
     "ATC Ile ACC Thr AAC Asn AGC Ser " + 
     "ATA Ile ACA Thr AAA Lys AGA Arg " + 
     "ATG Met ACG Thr AAG Lys AGG Arg " + 
     "GTT Val GCT Ala GAT Asp GGT Gly " + 
     "GTC Val GCC Ala GAC Asp GGC Gly " + 
     "GTA Val GCA Ala GAA Glu GGA Gly " + 
     "GTG Val GCG Ala GAG Glu GGG Gly ";
	
	private static HashMap<String,String> translationMap = new HashMap<String,String>();
	
	public static void dumpAllFramesToConsole(String inString ) throws Exception
	{
	    String[] frames = Translate.getAllFrames(inString);
	    
	    for ( int x=0; x < frames.length; x++ )
	    {
	        System.out.println(x + " " + frames[x]);
	    }
	    
	}
	
	public static Collection<String> getKeySet() throws Exception
	{
		return Collections.unmodifiableCollection(translationMap.keySet());
	}
	
	static
	{
		StringTokenizer sToken = new StringTokenizer( translationString );
		
		while ( sToken.hasMoreTokens() ) 
		{
			String key = sToken.nextToken();
			String value = sToken.nextToken().toUpperCase();
			//System.out.println( key + " " + value );
			
			translationMap.put(key, value);
		}
	}
	
	public static char getSafeAA(String codon) throws Exception
	{
		
		if ( ! isValidCodon(codon) ) 
				return 'X';
		else if ( ! isStopCodon(codon ) ) 
			return SequenceUtils.threeToOne( 
				(String) translationMap.get( codon)).charAt(0);
			
		return '*';
	}
	
	public static boolean isValidCodon( String codon ) throws Exception
	{
		String returnVal = (String) translationMap.get(codon);
		
		if ( returnVal == null ) 
			return false;
			
		return true;
	}
	
	public static boolean isStopCodon( String codon ) throws Exception
	{
		String returnVal = (String) translationMap.get(codon);
		
		if ( returnVal.equals("STOP")  ) 
			return true;
			
		return false;
	
	}
	
	public static String changeCodonRandomly(String inCodon) throws Exception
	{
		if ( inCodon.length() != 3)
			throw new Exception("Invalid codon");
		
		int randomPos = random.nextInt(3);
		
		if ( randomPos < 0 || randomPos > 2)
			throw new Exception("Logic error");
			
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x <= 2; x++ )
		{
			if ( x == randomPos)
			{
				char c = getRandomNucleotide();
				
				while ( c == inCodon.charAt(x))
					c = getRandomNucleotide();
					
				buff.append(c);
			}
			else
			{
				buff.append(inCodon.charAt(x));
			}
		}	
		
		return buff.toString();
	}
	
	/**
	 *  finds a frame or returns -1 if no frame can be found.
	 *   
	 *  Throws if two frames are found
	 */
	public static int findAFrame(String dnaSeq, String proteinGuideSeq)
		throws Exception
	{
		int frame = -1;
		
		String[] frames = getAllFrames(dnaSeq);
		
		for ( int x=0; x < frames.length; x++ )
		{
			if ( proteinGuideSeq.indexOf(frames[x] ) != -1)
			{
				if ( frame != -1 )
					throw new Exception("Found two frames for " 
						+ proteinGuideSeq);
					
				frame = x;
			}
		}
		
		return frame;
	}
	
	public static String getRandomCodon() throws Exception
	{
		return "" + getRandomNucleotide() + getRandomNucleotide() + 
							getRandomNucleotide();
	}
	
	public static char getRandomNucleotide() throws Exception
	{	
		int randomInt = random.nextInt(4);
		
		if ( randomInt == 0)
			return 'A';
			
		if ( randomInt == 1)
			return 'C';
			
		if ( randomInt == 2)
			return 'G';
			
		if ( randomInt == 3)
			return 'T';
			
		throw new Exception("unexpected " + randomInt);
	}
	
	public static String translate( String codon ) throws Exception
	{
		codon = codon.toUpperCase();
		
		String returnVal = (String) translationMap.get(codon);
		
		if ( returnVal == null ) 
			throw new Exception( "Unknown codon " + codon );
			
		return returnVal;
	}
	
	public static String safeReverseTranscribe(String inString ) throws 
		Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x= inString.length() - 1; x >=0; x-- ) 
		{
			char c = inString.charAt(x);
			
			if ( c == 'A' ) 
				buff.append( 'T' );
			else if ( c == 'T' ) 
				buff.append( 'A' );
			else if ( c == 'C' ) 
				buff.append( 'G' );
			else if ( c == 'G' )
				buff.append ( 'C' );
			else if ( c == 'N') 
				buff.append( 'N') ;
			else if ( c== '-' ) 
				buff.append('-');
			else buff.append(c);
		}
		
		return buff.toString();
		}
	
	public static String reverseTranscribe(String inString) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x= inString.length() - 1; x >=0; x-- ) 
		{
			char c = inString.charAt(x);
			
			if ( c == 'A' ) 
				buff.append( 'T' );
			else if ( c == 'T' ) 
				buff.append( 'A' );
			else if ( c == 'C' ) 
				buff.append( 'G' );
			else if ( c == 'G' )
				buff.append ( 'C' );
			else if ( c == 'N') 
				buff.append( 'N') ;
			else if ( c== '-' ) 
				buff.append('-');
			else throw new Exception("Unexpected character " + c );
		}
		
		return buff.toString();
	}
	
	public static String getSafeProteinSequence(String dnaSequence )
		throws Exception
	{
		dnaSequence = dnaSequence.toUpperCase();
		StringBuffer buff = new StringBuffer();
		
		int currentPos = 0;
		
		while( dnaSequence.length() -3 >= currentPos ) 
		{
			String symbol = "" + dnaSequence.charAt(currentPos) + 
							dnaSequence.charAt(currentPos + 1) + 
							dnaSequence.charAt(currentPos + 2);
			currentPos += 3;
			
			String value = (String) translationMap.get( symbol);
			
			if ( value == null ) 
				buff.append("X");
			else if ( ! value.equals("STOP" ) ) 
				buff.append( SequenceUtils.threeToOne( value ));
			else if ( value.equals("STOP") ) 
				buff.append("*");
			else buff.append("X");
		}
		
		return buff.toString();	
	}
	
	/**  Ignores the last part of the sequence if it does not form a full codon.
	 *   * is a STOP
	 */
	public static String getProteinSequence( String dnaSequence ) throws Exception
	{
		dnaSequence = dnaSequence.toUpperCase();
		StringBuffer buff = new StringBuffer();
		
		int currentPos = 0;
		
		while( dnaSequence.length() -3 >= currentPos ) 
		{
			String symbol = "" + dnaSequence.charAt(currentPos) + 
							dnaSequence.charAt(currentPos + 1) + 
							dnaSequence.charAt(currentPos + 2);
			currentPos += 3;
			
			String value = (String) translationMap.get( symbol);
			
			//if ( value == null ) 
			//	throw new Exception("Unkown codon " + symbol );
			
			if ( value == null ) 
				buff.append("X");
			else if ( ! value.equals("STOP" ) ) 
				buff.append( SequenceUtils.threeToOne( value ));
			else if ( value.equals("STOP") ) 
				buff.append("*");
			else throw new Exception("Unexpected codon " + symbol );
		}
		
		return buff.toString();	
	}
	
	public static String getDnaSequenceFromFrame(String inSequence, int frame) throws Exception
	{
		if ( frame == 0 )
			return inSequence;
		
		if ( frame == 1 )		
			return inSequence.substring(1);
			
		if ( frame == 2)
			return inSequence.substring(2);
				
		if ( frame == 3) 
			return reverseTranscribe(inSequence);
			
		if ( frame ==4 )
				return reverseTranscribe(inSequence.substring(0, inSequence.length() -1 ));
				
		if ( frame == 5 )		
				return reverseTranscribe(inSequence.substring(0, inSequence.length() -2));
		
		throw new Exception("Unknown frame");
	}
	
	public static String[] getSafeAllFrames(String inSequence ) throws Exception
	{
		String[] allFrames = new String[6];
		allFrames[0] = getSafeProteinSequence(inSequence);
		allFrames[1] = getSafeProteinSequence(inSequence.substring(1));
		allFrames[2] = getSafeProteinSequence(inSequence.substring(2));
		allFrames[3] = getSafeProteinSequence(
		        	safeReverseTranscribe(inSequence));
		allFrames[4] = getSafeProteinSequence(
		        	safeReverseTranscribe(inSequence.substring(0, inSequence.length() -1 )));
		allFrames[5] = getSafeProteinSequence(
		        safeReverseTranscribe(inSequence.substring(0, inSequence.length() -2)));
		
		return allFrames;
	}
	
	public static String[] getAllFrames( String inSequence ) throws Exception
	{
		String[] allFrames = new String[6];
		allFrames[0] = getProteinSequence(inSequence);
		allFrames[1] = getProteinSequence(inSequence.substring(1));
		allFrames[2] = getProteinSequence(inSequence.substring(2));
		allFrames[3] = getProteinSequence(reverseTranscribe(inSequence));
		allFrames[4] = getProteinSequence(reverseTranscribe(inSequence.substring(0, inSequence.length() -1 )));
		allFrames[5] = getProteinSequence(reverseTranscribe(inSequence.substring(0, inSequence.length() -2)));
		
		return allFrames;
	}
	
	/**  Frame should be 0, 1 or 2
	 */
	public static void dumpToHtml( File file, String dnaSequence, int frame ) 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter( new FileWriter( file ));
		writer.write( "<html><body><pre> " );
		
		int currentPos = 0;
		
		for ( int x=frame; x >0; x-- ) 
		{
			writer.write( dnaSequence.charAt(currentPos));
			currentPos++;		
		}
		
		System.out.println( currentPos );
		
		writer.write("\n\n");
		
		while ( currentPos < dnaSequence.length() - 1 ) 
		{
			writeProteinLine(writer, dnaSequence, currentPos);
			writeDnaLine(writer, dnaSequence,currentPos);
			currentPos += NUM_PROTEIN_RESIDUES_PER_LINE *3;
			System.out.println( currentPos );
			writer.write("\n");
		}	
		
		writer.write("</pre></body></html>");
		writer.flush();  writer.close();
	}
	
	private static void writeProteinLine(BufferedWriter writer, String dnaSequence, int startPos)
		throws Exception
	{
		int numToDo = NUM_PROTEIN_RESIDUES_PER_LINE;
		int currentPos = startPos;
		
		while ( numToDo > 0 && currentPos < dnaSequence.length() - 3 ) 
		{
			String symbol = "" + dnaSequence.charAt(currentPos) + 
							dnaSequence.charAt(currentPos + 1) + 
							dnaSequence.charAt(currentPos + 2);
			currentPos += 3;
			numToDo--;
			
			String value = (String) translationMap.get( symbol);
			
			if ( ! value.equals("STOP" ) ) 
				writer.write( "  " + SequenceUtils.threeToOne(value) + " ");
			else if ( value.equals("STOP") ) 
				writer.write("  * ");
			else throw new Exception("Unexpected codon " + symbol );
		}
		
		writer.write("\n");
	}
	
	private static void writeDnaLine(BufferedWriter writer, String dnaSequence, int startPos ) 
		throws Exception
	{
		int numToDo = NUM_PROTEIN_RESIDUES_PER_LINE;
		int currentPos = startPos;
		
		writer.write( " " );
		
		while ( numToDo > 0 && currentPos < dnaSequence.length() - 3 ) 
		{
			writer.write( "" + dnaSequence.charAt(currentPos) + 
							dnaSequence.charAt(currentPos + 1) + 
							dnaSequence.charAt(currentPos + 2) + " ");
			currentPos += 3;
			numToDo--;
		}
		
		writer.write( " " + currentPos + "\n");
	
	}

	
	/**  Frame should be 0, 1 or 2
	 */
	public static void dumpToFile( File file, String dnaSequence, int frame )  throws Exception
	{
		BufferedWriter writer = new BufferedWriter( new FileWriter( file ));
		
		int currentPos = 0;
		
		for ( int x=frame; x >0; x-- ) 
		{
			writer.write( dnaSequence.charAt(currentPos));
			currentPos++;		
		}
		
		writer.write("\n\n");
		
		while ( dnaSequence.length() -3 >= currentPos )
		{
			String symbol = "" + dnaSequence.charAt(currentPos) + 
							dnaSequence.charAt(currentPos + 1) + 
							dnaSequence.charAt(currentPos + 2);
			currentPos += 3;
			
			String value = (String) translationMap.get( symbol);
			
			if ( ! value.equals("STOP" ) ) 
				writer.write( SequenceUtils.threeToOne( value ) + " " + value + "\n");
			else if ( value.equals("STOP") ) 
				writer.write("*\n");
			else throw new Exception("Unexpected codon " + symbol );
			
			writer.write( symbol + "\n");
			writer.write( currentPos + "\n\n" );
		}
		
		writer.flush();  writer.close();	
	}
	
	public static String readSequenceFromFastaFile(File inFile ) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		BufferedReader reader = new BufferedReader( new FileReader( inFile ));
		
		reader.readLine();
		String nextLine = reader.readLine();
		
		while ( nextLine != null ) 
		{
			buff.append( nextLine.trim() );
			nextLine = reader.readLine();
		}
			
		return buff.toString();	
	}
	
	
	/*
	public static void main(String[] args) throws Exception
	{
		//String sequence = readSequenceFromFastaFile( new File("C:\\vectorSequences\\mbr5_ss.txt" ));
		String sequence= readSequenceFromFastaFile(new File("C:\\vectorSequences\\dslo-Adelman-DNA.1.txt"));
		String upPrimer = "GGCACGGGCACACAGAATC";
		String downPrimer = "AGAGTTATCATCCTTGTTGGA";
		downPrimer = reverseTranslate(downPrimer);
		
		System.out.println( getProteinSequence(downPrimer) );
		
		System.out.println ( sequence.indexOf(downPrimer) - sequence.indexOf(upPrimer) );
		
		
		
	}
	
	/*
	public static void main(String[] args) throws Exception
	{
		String sequence = readSequenceFromFastaFile( new File("C:\\vectorSequences\\mbr5_ss.txt" ));
		
		//String sequence = "TATTATTATTATTATTATTATTAT";
		
		String[] allFrames = getAllFrames(sequence);
		
		dumpToFile(new File("c:\\temp\\mrb5_SS.txt"), sequence, 2 );
		dumpToHtml(new File("c:\\temp\\mrb5_SS.html"), sequence, 2 );
		
		String queryString = "ANQINQYKSTSSLIPPIREVEDEC";
		
		
		for ( int x=0; x< allFrames.length; x++ ) 
		{
			if ( allFrames[x].indexOf(queryString) != -1 ) 
			{
				System.out.println("Frame " + x );
				System.out.println( allFrames[x] + "\n\n\n" );
				System.out.println( "" + allFrames[x].indexOf(queryString) );
			}
		}
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String sequence = readSequenceFromFastaFile( new File("C:\\vectorSequences\\mbr5_ss.txt" ));
		
		//String subSequence = "GAATTC";
		//String subSequence = "TAGTGGAAGAAAGCACATTGTAGTCTGT";
		//String subSequence = "gaattctagtggaagaaagcacattgtagtctgt";
		String subSequence = "tagtggaagaaagcacattgtagtctgt";
		
		subSequence = subSequence.toUpperCase();
		System.out.println( subSequence );
		
		System.out.println( sequence.indexOf(subSequence));
	}
	*/
	
	public static float getGCContent(String inString ) 
	{
		int gcNum =0;
		int num= 0;
		
		inString = inString.toUpperCase();
		
		for ( int x=0; x< inString.length(); x++ ) 
		{
			num++;
			
			if ( inString.charAt(x) == 'G' || inString.charAt(x) == 'C' ) 
				gcNum++;
		}
		
		return (( float) gcNum )/ num;
	}
	
	/*
	public static void main(String[] args) throws Exception
	{
		String sequence = readSequenceFromFastaFile( new File(
				"C:\\Documents and Settings\\Anthony\\Desktop\\mouseRefSeqRna.txt"));
				
		System.out.println( sequence );
		System.out.println( getProteinSequence(sequence));
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String aSequence = readSequenceFromFastaFile(new 
			File("C:\\Documents and Settings\\Anthony\\Desktop\\VectorStuff\\July28Sequences\\89-M9-10-T7_T7-A12.seq"));
			
		System.out.println( reverseTranscribe(aSequence) );	
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		
		String aSequence = readSequenceFromFastaFile( new
			File("C:\\Documents and Settings\\Anthony\\Desktop\\VectorStuff\\1963\\53-M7-8-7_UBACUP2691-E07.seq"));
		
		
		System.out.println( reverseTranscribe(aSequence));
		
		ClustalAlignment cAlignment = new ClustalAlignment(new File( "c:\\cygwin\\clustalw\\Seq2.aln"));
		cAlignment.writeHtmlFile(new File("c:\\cygwin\\clustalw\\Seq2.html"));
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String aSequence = readSequenceFromFastaFile( new File(
			"C:\\Documents and Settings\\Anthony\\Desktop\\VectorStuff\\mbr5ss_protein.txt"));
			
		String subSequence = "IPPIREVED";
		System.out.println( (aSequence.indexOf(subSequence)) + 1 + subSequence.length() - 1 );
		System.out.println( aSequence.length() );
		//System.out.println( (aSequence.indexOf(subSequence)) + 1 );
			
				
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String aSequence = readSequenceFromFastaFile( new
			File("C:\\Documents and Settings\\Anthony\\Desktop\\VectorStuff\\sequences In pAcG2T\\MFull_inpAcG2T.txt"));
			
		int fivePrimePos = aSequence.indexOf("CTGGTTCCGCGTGGATCC");
		aSequence = aSequence.substring(fivePrimePos);
		
		String downstreamPrimer = Translate.reverseTranscribe( "CTAATGATGATGATGATGATGATCTTCAACTTCTCTGATTGG");
		
		int threePrimePos = aSequence.indexOf( downstreamPrimer);
				
		aSequence = aSequence.substring(0, threePrimePos + downstreamPrimer.length());
		
		System.out.println( Translate.getProteinSequence(aSequence) );
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		//String vectorSequence = readSequenceFromFastaFile( new File("C:\\vectorSequences\\mbr5_ss.txt" ));
		//String vectorSequence = readSequenceFromFastaFile(new File("C:\\vectorSequences\\dslo-Adelman-DNA.1.txt"));
	
		
		String aSequence = "AGAGTTATCATCCTTGTTGGA";
		aSequence = aSequence.toUpperCase();
		aSequence = reverseTranslate(aSequence);
				
		System.out.println( aSequence );
		System.out.println( Translate.getProteinSequence(aSequence));
		//String[] allFrames = getAllFrames(aSequence);
		//System.out.println( allFrames[0] );
		//System.out.println( getGCContent(aSequence));
		//System.out.println( vectorSequence.indexOf(aSequence));
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String vectorSequence = readSequenceFromFastaFile( new File("C:\\vectorSequences\\mbr5_ss.txt" ));
		
		
		String primer1 = "CATCAAGCTGGTGAATCTGC";
		
		String primer2 = "CACTCTCCAGAGTAATGTGTCCA";
		vectorSequence = vectorSequence.toUpperCase();
		
		System.out.println( vectorSequence.indexOf(primer1));
		System.out.println ( Translate.reverseTranscribe( vectorSequence).indexOf(primer2));
		
		//aSequence = reverseTranslate(aSequence);
				
		//System.out.println( aSequence );
		//System.out.println( Translate.getProteinSequence(aSequence));
		//String[] allFrames = getAllFrames(aSequence);
		//System.out.println( allFrames[0] );
		//System.out.println( getGCContent(aSequence));
		//System.out.println( vectorSequence.indexOf(aSequence));
	}
	*/
	
	/*
	public static void main(String[] args) throws Exception
	{
		String aSeq = 
		  "CCAAGATGTCCATCTACAAGAGAATGAAACTGGCATGTTGTTTTGATTGCGGACGCTCTGAGCGTGACTGCTCTTGCATGTCAGGCAGTGTACATAGTAACATGGACACCCTTGAGAGAGCCTTCCCACTTTCTTCTGTCTCTGTTAATGATTGCTCCACCAGTTTACGTGCCTTT";
		  
		String[] frames = Translate.getAllFrames(aSeq);
		
		for ( int x=0; x < frames.length; x++ ) 
			System.out.println( frames[x] );
			
		TranslateAlignedSequences tas = new TranslateAlignedSequences( aSeq.substring(2)
				, aSeq.substring(2) );
		
		System.out.println( "\n\n" +  tas.getTranslatedSequence() );
		  
		 
	}
	
	/*
	public static void main(String[] args) throws Exception
	{	
		String sequence = readSequenceFromFastaFile(new File("C:\\vectorSequences\\dslo-Adelman-DNA.1.txt"));
		dumpToHtml(new File("c:\\temp\\dslo.html"), sequence, 2);
	}
	/*
	/*
	public static void main(String[] args) throws Exception
	{
		String sequence = readSequenceFromFastaFile(new File("C:\\vectorSequences\\dslo-Adelman-DNA.1.txt"));
		String queryString = "EHGKRHIVVCGHITYESVSHFLKDFLHEDREDVDVEVVFLHRKPPDLEL";
		String[] allFrames = getAllFrames(sequence);
		
		
		for ( int x=0; x< allFrames.length; x++ ) 
		{
			if ( allFrames[x].indexOf(queryString) != -1 ) 
			{
				System.out.println("Frame " + x );
				System.out.println( allFrames[x] + "\n\n\n" );
				System.out.println( "" + allFrames[x].indexOf(queryString) );
			}
		}	
	}
	*/	
}
