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
		return this.encodedSequence.equals(other.encodedSequence);
	}
	
	@Override
	public int hashCode()
	{
		return this.encodedSequence.hashCode();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<EncodedSequence> set= new LinkedHashSet<EncodedSequence>();
		
		String s1 = "ACCCTTTTAACCCCCCCCCCCGGGTTTAAA";
		String s2 = "ACTGACGT";
		
		
		set.add(new EncodedSequence(s1));
		set.add(new EncodedSequence(s1));
		set.add(new EncodedSequence(s2));
		set.add(new EncodedSequence(s2));
		
		// should be 2
		System.out.println(set.size());
		
		// should be true
		System.out.println(set.contains(new EncodedSequence(s1)));
		System.out.println(set.contains(new EncodedSequence(s2)));
		
		// should be false
		System.out.println(set.contains(new EncodedSequence("TTTTAAACC")));
		
	}
}
