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
		
		int numIterations = 10;
		
		for( int x=0; x < numIterations; x++)
		{
			BigNumberFactorizerWorker bf =new BigNumberFactorizerWorker(
					 Math.abs(r.nextInt()));
			bf.run();
			System.out.println( (System.currentTimeMillis() - startTime) / 1000f );
		}
		
		System.out.println("Total time : " 
					+ (System.currentTimeMillis() - startTime) / 1000f );
	}
	
	
	
	private static class BigNumberFactorizerWorker implements Runnable
	{
		private final int someBigNum;

		public BigNumberFactorizerWorker(int someBigNum)
		{
			this.someBigNum = someBigNum;
		}
		
		@Override	
		public void run()
		{
			List<Long> factors =new ArrayList<Long>();
			
			int half = someBigNum /2 + 1;
			
			for( long x=2 ; x < half; x++)
				if( someBigNum % x == 0 )
					factors.add(x);
			
			System.out.print(someBigNum + " ");
			
			if( factors.size() == 0 )
				System.out.println(" prime ");
			else
				System.out.println( factors.toString());
		}
	}
}
