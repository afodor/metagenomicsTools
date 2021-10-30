package scratch;

public class SS_Estimate
{

	public static void main(String[] args) throws Exception
	{
		double b= 250000;
		
		for( int x=1; x<=15;x++)
		{
			System.out.println( x + "\t" + (b/420) + "\t" +  estimateSS(b));
			b+= 20000;
		}
	}
	
	private static double estimateSS(double val) 
	{
		val = val /420;
		
		double sum =0;
		
		sum += 926 * .9;
		
		val = val - 926;
		
		if( val <= (5583-926))
		{
			sum += val*.32;
		}
		else
		{
			sum += (5583-926)*.32;
		}
		
		val= val- (5583-926);
		
		double last = val * .15;
		
		if( last > 0 )
			sum += last;
			
		return sum;
	}
}

