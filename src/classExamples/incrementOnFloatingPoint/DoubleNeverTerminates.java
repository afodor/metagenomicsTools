package classExamples.incrementOnFloatingPoint;

public class DoubleNeverTerminates
{
	public static void main(String[] args)
	{
		for( double d= 9223272036854776320l; d < 9223272036854776321l; d++)
		{
			
		}
		
		System.out.println("We never get here");
	}
}
