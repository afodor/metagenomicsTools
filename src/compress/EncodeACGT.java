package compress;

import java.util.HashMap;
import java.util.Random;

public class EncodeACGT
{
	private static HashMap<Character, String> charTo8MerMap = new HashMap<Character, String>();
	private static HashMap<String, Character> eightMerToCharMap= new HashMap<String, Character>();
	
	private static Random RANDOM = new Random();
	
	static
	{
		try
		{
			charTo8MerMap = getCharTo8MerMap();
			
			for( Character c : charTo8MerMap.keySet() )
			{
				String s = charTo8MerMap.get(c);
				
				if( eightMerToCharMap.containsKey(s))
					throw new Exception("Logic error");
				
				eightMerToCharMap.put(s, c);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Fatal logic error");
			throw new RuntimeException(ex);
		}
	}
	
	/*
	 * s must only contain {A,C,G,T} 
	 */
	public static String encode(String s ) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x< s.length(); x+=8)
		{
			String sub = s.substring(x, Math.min(x+8, s.length()));
			
			while(sub.length() < 8)
				sub = "A" + sub;
			
			Character c = eightMerToCharMap.get(sub);
			
			if( c== null)
				throw new Exception("Could not find " + sub);
			
			buff.append(c);
		}
		
		return buff.toString();
	}
	
	public static String decode(String s, int length)
	{
		StringBuffer buff = new StringBuffer();
		
		int emitted =0;
		
		for( int x=0; x < s.length(); x++)
		{
			String eightMer = charTo8MerMap.get(s.charAt(x));
			
			int left = length - emitted;
			
			//System.out.println(x + " " + eightMer + " " + left);
			
			if( left < 8 )
			{
				while( left > 0 )
				{
					buff.append( eightMer.charAt(8-left));
					left--;
				}
				
				return buff.toString();
			}
			
			buff.append(eightMer);
			
			emitted += eightMer.length();
			
		}
			
		return buff.toString();
	}
	
	private static HashMap<Character, String> getCharTo8MerMap() throws Exception
	{
		HashMap<Character, String> map = new HashMap<Character, String>();
		
		for( int i =0; i <=65535; i++)
		{
			map.put((char)i, get8MerFromChar((char)i));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		double totalUncompressed = 0;
		double totalCompressed =0;
		
		for( int x=0; x< 1000; x++)
		{
			String s = getRandomKMer(RANDOM.nextInt(1000) +250);
			String s2 = encode(s);
			String s3 = decode(s2, s.length());
			
			System.out.println(s.length() + " " + s2.length() );
			
			if( ! s.equals(s3))
				throw new Exception("Failure ");
			
			totalUncompressed += s.length();
			totalCompressed += s2.length();
		}
		
		System.out.println( totalUncompressed + " " + totalCompressed + " " + (totalCompressed/totalUncompressed));
		
	}
	
	

	  private static String getRandomKMer(int size)
	  {
		  StringBuffer buff = new StringBuffer();
		  
		  char[] c = {'A','C','G','T'};
		  
		  for( int x=0; x < size; x++)
			  buff.append(c[RANDOM.nextInt(4)]);
		  
		  return buff.toString();
	  }
	
	private static String get8MerFromChar(char c) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		String vals = Integer.toBinaryString(c);
		
		while( vals.length() < 16)
			vals = "0" + vals;
			
		for( int x=0; x <=15; x =x +2)
		{
			String subString = vals.substring(x, x+2);
			
			if( subString.equals("00"))
				buff.append("A");
			else if ( subString.equals("01"))
				buff.append("C");
			else if ( subString.equals("10"))
				buff.append("G");
			else if ( subString.equals("11"))
				buff.append("T");
			else throw new Exception("Logic error " + subString);
		}
		
		return buff.toString();
	}
}
