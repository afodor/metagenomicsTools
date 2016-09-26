package classExamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadExampleFactorizer
{
	public static void main(String[] args) throws Exception
	{
		Random r = new Random();
		long startTime = System.currentTimeMillis();
		
		int numIterations = 25;
		
		for( int x=0; x < numIterations; x++)
		{
			FactorizerWorker bf =new FactorizerWorker(
					 Math.abs(r.nextInt()));
			
			// how NOT to kick off a thread; this will not be multi-threaded
			bf.run();
			System.out.println( (System.currentTimeMillis() - startTime) / 1000f );
		}
		
		System.out.println("Total time : " 
					+ (System.currentTimeMillis() - startTime) / 1000f );
	}
	
	
	
	private static class FactorizerWorker implements Runnable
	{
		private final int numberToFactor;

		public FactorizerWorker(int someBigNum)
		{
			this.numberToFactor = someBigNum;
		}
		
		@Override	
		public void run()
		{
			List<Long> factors =new ArrayList<Long>();
			
			int half = numberToFactor /2 + 1;
			
			for( long x=2 ; x < half; x++)
				if( numberToFactor % x == 0 )
					factors.add(x);
			
			System.out.print(numberToFactor + " ");
			
			if( factors.size() == 0 )
				System.out.println(" prime ");
			else
				System.out.println( factors.toString());
		}
	}
}
