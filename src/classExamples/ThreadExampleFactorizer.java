package classExamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ThreadExampleFactorizer
{
	public static void main(String[] args) throws Exception
	{
			
		Random r = new Random();
		long startTime = System.currentTimeMillis();
		
		int numIterations = 100;
		int numThreadsAtATime = 6;
		
		Semaphore s = new Semaphore(numThreadsAtATime);
		
		for( int x=0; x < numIterations; x++)
		{
			s.acquire();
			FactorizerWorker fw =new FactorizerWorker(Math.abs(r.nextInt()), s);
			
			
			// kick off the threads one at a time - CPU won't go to 100%
			//fw.run();
			
			 //kick off the threads in parallel use all my CPU and it will be faster
			new Thread( fw).start();
			
			
			System.out.println( "Started a worker " + (System.currentTimeMillis() - startTime) / 1000f );
		}
		
		for( int x=0; x < numThreadsAtATime; x++)
			s.acquire();
		
		System.out.println("Total time : " 
					+ (System.currentTimeMillis() - startTime) / 1000f );
	}
	
	
	
	private static class FactorizerWorker implements Runnable
	{
		private final int numberToFactor;
		private Semaphore semaphore;
		
		
		public FactorizerWorker(int someBigNum, Semaphore semaphore)
		{
			this.numberToFactor = someBigNum;
			this.semaphore = semaphore;
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
			
			System.out.println("Worker finishing ");
			semaphore.release();
			
		}
	}
}
