package compress;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class EncodedSequence
{
	private final String encodedSequence;
	private final int originalLength;
	
	/*
	 * dnaSeq can only contain A,C,G,T
	 */
	public EncodedSequence(String dnaSeq) throws Exception
	{
		this.originalLength = dnaSeq.length();
		this.encodedSequence = EncodeACGT.encode(dnaSeq);
	}
	
	public int getOriginalLength()
	{
		return originalLength;
	}
	
	public String getEncodedSequence()
	{
		return encodedSequence;
	}
	
	public String decode() 
	{
		return EncodeACGT.decode(encodedSequence, originalLength);
	}
	
	@Override
	public String toString()
	{
		return decode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		EncodedSequence other = (EncodedSequence) obj;
		return this.encodedSequence.equals(other.encodedSequence) && this.originalLength == other.originalLength;
	}
	
	@Override
	public int hashCode()
	{
		return this.encodedSequence.hashCode() + this.originalLength;
	}
	
	public static void main(String[] args) throws Exception
	{

		EncodedSequence esZero = new EncodedSequence("");
		
		// should be 0
		System.out.println(esZero.decode().length());
		
		HashSet<EncodedSequence> set= new LinkedHashSet<EncodedSequence>();
		
		String s1 = "ACCCTTTTAACCCCCCCCCCCGGGTTTAAA";
		String s2 = "ACTGACGT";
		String s3 = "A";
		String s4 = "AA";
		
		EncodedSequence esA = new EncodedSequence(s3);
		EncodedSequence esAA = new EncodedSequence(s4);
		
		// should be "A" "AA"
		System.out.println(esA.toString() + " " + esAA.toString());
		
		
		//should be false
		System.out.println(esA.equals(esAA));
		
		set.add(new EncodedSequence(s1));
		set.add(new EncodedSequence(s1));
		set.add(new EncodedSequence(s2));
		set.add(new EncodedSequence(s2));
		set.add(esA);	
		set.add(esAA);
		
		// should be 4
		System.out.println(set.size());
		
		// should be true
		System.out.println(set.contains(new EncodedSequence(s1)));
		System.out.println(set.contains(new EncodedSequence(s2)));
		
		// should be false
		System.out.println(set.contains(new EncodedSequence("TTTTAAACC")));
		
	}
}
