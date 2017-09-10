package classExamples.incrementOnFloatingPoint;

public class DoubleToLong
{
	public static final Long START = Long.MAX_VALUE-100000000000000l;
	
	public static void main(String[] args) throws Exception
	{
		
		System.out.println(Double.MAX_VALUE);
		long l= START;
		double d = START;
		
		for( long x=START; x < Long.MAX_VALUE; x++)
		{
			l++;
			d++;
			
			if( l != d )
			{
				System.out.println( x + " " + l + " " + d);
				System.exit(1);
			}
		}
		System.out.println("Finished");
	}
}
