package qCompute;

public class BVProblem
{
	private static final int numBits = 3;
	
	public static void main(String[] args)
	{
		System.out.println( getVal(3, 2^0));
		System.out.println( getVal(3, 2^1));		
		System.out.println( getVal(3, 2^2));
	}
	
	private static int getVal(int a , int x)
	{
		int[] aBits = getBits(a);
		int[] xBits = getBits(x);
		
		printBits(aBits);
		printBits(xBits);
		
		int val = 1;
		
		for( int y=aBits.length-1; y >=0; y--)
			val = val ^ ( aBits[y] * xBits[y] );
		
		return val;
	}
	
	private static void printBits(int[] bits)
	{
		for( int x=bits.length-1; x >=0; x--)
			System.out.print(bits[x] +  " ");
		
		System.out.println();
	}
	
	private static int[] getBits(int val)
	{
		
		int[] a = new int[numBits];
		
		
		for( int x=0; x < a.length; x++)
			a[x] = getBit(val, x);
		
		return a;
	}
	
	private static int getBit(int n, int k) {
	    return (n >> k) & 1;
	}
}
