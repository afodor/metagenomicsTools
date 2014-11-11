package scratch;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public class QuickSemaphore
{
	public static void main(String[] args) throws Exception
	{
		int numThreads = 4;
		int numJobs = 100;
		AtomicLong counter = new AtomicLong(0);
		
		Semaphore semaphore = new Semaphore(numThreads);
		
		for( int x=0; x < numJobs; x++)
		{
			semaphore.acquire();
			WorkAndRelease wr = new WorkAndRelease(semaphore, counter);
			new Thread(wr).start();
		}
		
		// get all the licenses back and you are done
		for( int x=0; x < numThreads;x++)
			semaphore.acquire();
		
		System.out.println("All " + counter.get() + " threads finished!");
		
	}
	
	private static class WorkAndRelease implements Runnable
	{
		private final Semaphore semaphore;
		private final AtomicLong counter;
		
		private WorkAndRelease(Semaphore semaphore, AtomicLong counter)
		{
			this.semaphore = semaphore;
			this.counter = counter;
		}
		
		public void run()
		{
			try
			{
				// simulate some work
				Thread.sleep(200);
				System.out.println("Thread finished " + counter.incrementAndGet());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				// one thread fails, they all fail!
				System.exit(1);
			}
			finally
			{
				semaphore.release();
			}
		}
	}
}
