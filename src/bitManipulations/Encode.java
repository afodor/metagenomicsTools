package bitManipulations;

public class Encode
{
	private static final Long A_INT = new Long(0x0000l);
	private static final Long C_INT = new Long(0x0001l);
	private static final Long G_INT = new Long(0x0002l);
	private static final Long T_INT = new Long(0x0003l);
	
	public static String getKmer(Long i, long kmerLength)  throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		long start = T_INT;
		
		while(kmerLength > 0)
		{
			long myChar = i & start;
			
			if( myChar == A_INT )
				buff.append("A");
			
			if( myChar == C_INT)
				buff.append("C");
			
			if( myChar == G_INT)
				buff.append("G");
			
			if( myChar == T_INT)
				buff.append("T");
			
			i = i >> 2;
			kmerLength--;
		}
		
		return buff.toString();
	}
	
	public static Long makeLong( String s) throws Exception
 	{
		long val =0;
		
		if( s.length() > 32)
			throw new Exception("Can't encode");
		
		for(int x=0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			
			if( c== 'A')
			{
				val =val | ( A_INT << (x*2));
			} 
			else if ( c== 'C')
			{
				val = val | (C_INT << (x*2) );
			}
			else if ( c== 'G')
			{
				val = val | (G_INT << (x*2) );
			}
			else if ( c == 'T')
			{
				val = val | (T_INT << (x*2));
			}
			else return null;
		}
	

		return val;
	}
	
	public static void main(String[] args) throws Exception
	{
	
		String s = "CTAGCTACTATGCGACTACCCTACTATGCAAA";
		long aLong = makeLong(s);
		System.out.println( Long.toBinaryString(aLong));
		
		String kmer = getKmer(aLong, s.length());
		System.out.println(kmer);
		
		if( ! kmer.equals(s) )
			throw new Exception("FAIL!!!!");
		
		System.out.println("pass");
	
	}
}
