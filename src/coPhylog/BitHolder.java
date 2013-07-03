package coPhylog;

public class BitHolder
{
	// the leftmost 32 bits hold the left context
	// the rightmost 32 bits hold the right context
	private long bits = 0x0000;
	
	private char c; // the character in the middle
	
	// the index of the current string
	private int index =0;
	
	private int numValidChars=0;
	
	private final int stopPoint;
	
	public BitHolder(String s, int contextSize)
	{
		int targetChars = contextSize*2 + 1;
		
		stopPoint = s.length() - targetChars;
		
		while( index < stopPoint && numValidChars < targetChars  )
		{
			
		}
	}
	
	private final Long A_LONG = new Long(0x0000);
	private final Long C_LONG = new Long(0x0001);
	private final Long G_LONG = new Long(0x0002);
	private final Long T_LONG = new Long(0x0003);
	
	
	private Long getMaskOrNull(char c)
	{
		if( c == 'A' || c =='a' )
			return A_LONG;
		
		if( c == 'C' || c =='c' )
			return C_LONG;
		
		if( c == 'G' || c =='g' )
			return G_LONG;
		
		if( c == 'T' || c == 't')
			return T_LONG;
		
		return null;
		
	}
}
