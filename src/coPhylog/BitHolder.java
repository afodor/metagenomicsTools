package coPhylog;

public class BitHolder
{
	// the leftmost 32 bits hold the left context
	// the rightmost 32 bits hold the right context
	private long bits = 0x0l;
	
	public long getBits()
	{
		return bits;
	}
	
	
	private char middleChar ='A'; 
	
	public char getMiddleChar()
	{
		return middleChar;
	}
	
	// the index of the current string
	private int index =0;
	
	public int getIndex()
	{
		return index;
	}
	
	private int numValidChars=0;
	
	public int getNumValidChars()
	{
		return numValidChars;
	}
	
	private int stopPoint;
	private final int contextSize;
	

	private static final Long A_LONG = new Long(0x0000l);
	private static final Long C_LONG = new Long(0x0001l);
	private static final Long G_LONG = new Long(0x0002l);
	private static final Long T_LONG = new Long(0x0003l);
	
	private static final long RIGHT_MASK = 0x00000000FFFFFFFFl;
	private static final long LEFT_MASK = 0xFFFFFFFF00000000l;
	

	private static final long MIDDLE_CHAR_C_MASK= 0x1l << 30; 

	private static final long MIDDLE_CHAR_G_MASK= 0x1l << 31; 
	
	private static final long MIDDLE_CHAR_T_MASK= 0x3l << 30; 
	
	private final Long A_LONG_SHIFT;
	private final Long C_LONG_SHIFT;
	private final Long G_LONG_SHIFT;
	private final Long T_LONG_SHIFT;
		
	private final int shiftSize;
	
	public void setToString(String s) throws Exception
	{
		int targetChars = contextSize*2 + 1;
		
		stopPoint = s.length() - targetChars;
		
		while( index < stopPoint && numValidChars < targetChars  )
		{
			boolean isValidChar = add( s.charAt(index) );
			index++;
			
			if( ! isValidChar )
			{
				numValidChars=0;
				bits = 0x0000;
			}
			else
			{
				numValidChars++;
			}
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
	
	private boolean add(char newChar) throws Exception
	{
		Long mask = getMaskOrNull(newChar);
		
		if( mask == null)
			return false;
		
		long rightShifted = bits << 2;
		long leftShifted = rightShifted;
		
		rightShifted = rightShifted & RIGHT_MASK;
		rightShifted = rightShifted | mask;
		
		// what was in the middle goes to the left
		leftShifted = leftShifted & LEFT_MASK;
		Long toPush = getMaskOrNull(middleChar);
		leftShifted = leftShifted | (toPush.longValue() << 32) ;
		
		// the new middle character
		long middleVal = bits & MIDDLE_CHAR_T_MASK;
		
		if( middleVal == 0 )
			middleChar = 'A';
		else if ( middleVal == MIDDLE_CHAR_C_MASK)
			middleChar = 'C';
		else if (middleVal == MIDDLE_CHAR_G_MASK)
			middleChar = 'G';
		else if ( middleVal == MIDDLE_CHAR_T_MASK)
			middleChar = 'T';
		else
			throw new Exception("Logic error");
		
		bits = leftShifted | rightShifted;
		
		return true;
	}
	
	private Long getMaskOrNull(char c)
	{
		if( c == 'A' || c =='a' )
			return A_LONG_SHIFT;
		
		if( c == 'C' || c =='c' )
			return C_LONG_SHIFT;
		
		if( c == 'G' || c =='g' )
			return G_LONG_SHIFT;
		
		if( c == 'T' || c == 't')
			return T_LONG_SHIFT;
		
		return null;
		
	}
	
	public static void main(String[] args)
	{
		System.out.println( Long.toBinaryString(LEFT_MASK));
	}
}
