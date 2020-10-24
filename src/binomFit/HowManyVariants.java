package binomFit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;

public class HowManyVariants
{
	private static final Random RANDOM = new Random();
	
	private static final char[] ALPHABET = {'A','C','G','T'};
	
	private static final int SIZE = 250;
	
	public static String getAs(int size)
	 {
		  StringBuffer buff = new StringBuffer();
		  
		  for( int x=0; x < size; x++)
			  buff.append("A");
		  
		  return buff.toString();
	  }
	
	
	private static HashSet<String> getAllChildren(String s )
	{
		HashSet<String> set = new LinkedHashSet<String>();
		
		StringBuffer buff = new StringBuffer(s);
		
		for( int x=0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			
			for( char c2 : ALPHABET)
			{
				if( c != c2)
				{
					buff.replace(x, x+1, "" + c2);
					set.add(buff.toString());
				}
			}
			
			buff.replace(x, x+1, "" + c);
		}
		
		return set;
	}
	
	private static String copyWithErrorRate(String s, double p)
	{
		StringBuffer buff = new StringBuffer();
		
		for(int x=0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			
			if( RANDOM.nextDouble() >= p)
			{
				buff.append(c);
			}
			else
			{
				char newC = ALPHABET[RANDOM.nextInt( ALPHABET.length )];
				
				while(newC == c)
					newC = ALPHABET[RANDOM.nextInt( ALPHABET.length )];
				
				buff.append(newC);
			}
		}
		
		return buff.toString();
	}
	
	private static int getNumberMutations( String originalS, HashSet<String> originalSet, long depth, double errorRate)
	{
		HashSet<String> newSet = new HashSet<String>(originalSet);
		
		long numNovel =0;
		
		for( int x=0; x < depth; x++)
		{
			String newSequence = copyWithErrorRate(originalS, errorRate);
			newSet.remove(newSequence);
			
			if( !newSequence.equals(originalS)  && ! originalSet.contains(newSequence))
				numNovel++;
		}
		
		System.out.println("Found " + numNovel + " " +  (((double) numNovel) / depth) + " double or more SNPS" );
		return originalSet.size() - newSet.size();
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\depth.txt")));
		
		String s=  getAs(SIZE);
		HashSet<String> allChildren = getAllChildren(s);
		
		writer.write("depth\tnumMutations\terrorRate\n");

		double errorRate = 0.00015;
		
		for( long d = 100; d <= 190100; d +=1000)
		{
			writer.write(d + "\t" + getNumberMutations(s, allChildren, d, errorRate) + "\t" + errorRate + "\n");
			
			System.out.println(d);
			writer.flush();
		}
		
		writer.flush();  writer.close();
	}
	
	
	
	
}
