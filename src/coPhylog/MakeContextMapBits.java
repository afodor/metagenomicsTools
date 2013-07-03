package coPhylog;


public class MakeContextMapBits
{
	
	
	private static final int CONTEXT_SIZE = 12;

	public static void main(String[] args) 
	{
		long contextMask = getContextMask();
		
		System.out.println(Long.toBinaryString(contextMask));
	}
	
	private static long getContextMask()
	{
		int one = 0x0001;
		int start= one;
		
		for( int x=1; x < CONTEXT_SIZE ; x++)
		{
			start = start << 1;
			start = start | one;
		}
		
		return start;
	}
}
