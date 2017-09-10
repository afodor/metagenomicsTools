package classExamples.incrementOnFloatingPoint;

public class FloatToInt
{
	public static final int START = Integer.MAX_VALUE-100000;
	
	public static void main(String[] args) throws Exception
	{
		
		System.out.println(Float.MAX_VALUE);
		int i= START;
		float f  = START;
		
		for( int x=START; x < Integer.MAX_VALUE; x++)
		{
			i++;
			f++;
			
			if( f != i)
			{
				System.out.println( x + " " + i + " " + f);
				System.exit(1);
			}
		}
		System.out.println("Finished");
	}
}
