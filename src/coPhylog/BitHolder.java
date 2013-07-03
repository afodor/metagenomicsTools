package coPhylog;

public class BitHolder
{
	// the leftmost 32 bits hold the left context
	// the rightmost 32 bits hold the right context
	private long bits = 0x0000;
	
	private char middleChar; 
	
	// the index of the current string
	private int index =0;
	
	private int numValidChars=0;
	
	private int stopPoint;
	private final int contextSize;
	

	private static final Long A_LONG = new Long(0x0000);
	private static final Long C_LONG = new Long(0x0001);
	private static final Long G_LONG = new Long(0x0002);
	private static final Long T_LONG = new Long(0x0003);
	
	
	private final Long A_LONG_SHIFT;
	private final Long C_LONG_SHIFT;
	private final Long G_LONG_SHIFT;
	private final Long T_LONG_SHIFT;
	
	private final int shiftSize;
	
	public void setToString(String s)
	{
		int targetChars = contextSize*2 + 1;
		
		stopPoint = s.length() - targetChars;
		
		while( index < stopPoint && numValidChars < targetChars  )
		{
			
		}
		
	}
	
	public BitHolder(int contextSize) throws Exception
	{
		this.contextSize = contextSize;
		shiftSize = 32 - contextSize*2;
		
		if(shiftSize < 0 )
			throw new Exception("Maximum supported context size is 16");
		
		this.A_LONG_SHIFT = A_LONG << shiftSize;
		this.C_LONG_SHIFT = C_LONG << shiftSize;
		this.G_LONG_SHIFT = G_LONG << shiftSize;
		this.T_LONG_SHIFT = T_LONG << shiftSize;
		
		
	}
	
	private void add(char newChar)
	{
		
	}
	
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
