package qCompute;

public class BVProblem
{
	private static final int NUMBER_BITS = 3;
	
	private static final int a =7;
	
	public static void main(String[] args)
	{
		System.out.println(u(1));
		System.out.println(u(2));
		System.out.println(u(4));
	}
	
	private static final int u(int x)
	{
		String aBits = getBits(a);
		String xBits = getBits(x);
		//System.out.println(aBits);
		//System.out.println(xBits);
		
		int aBit = Integer.parseInt("" + aBits.charAt(NUMBER_BITS-1));
		int xBit = Integer.parseInt("" + xBits.charAt(NUMBER_BITS-1));
		int val = aBit*xBit;
		
		for( int i = NUMBER_BITS-2; i >=0; i--)
		{
			aBit = Integer.parseInt("" + aBits.charAt(i));
			xBit = Integer.parseInt("" + xBits.charAt(i));
			val = val ^ (aBit*xBit);
		}
		
		return val;
	}
	
	private static final String getBits(int x)
	{
		String val = Integer.toBinaryString(x);
		
		while( val.length() < NUMBER_BITS)
			val = "0" + val;
		
		return val;
	}
}
