package classExamples.boundedBuffer;

import junit.framework.TestCase;

/*
 * Modded from Chapter 11 of Java Concurrency in Practice
 */
public class MultiThreadedTestUnits extends TestCase
{
	private final int LOCKUP_DETECTION_TIMEOUT = 1000;
	
	public void testTakeBlocksWhenEmpty() throws Exception
	{
		final SemaphoreBoundedBuffer<Integer> sbb = 
				new SemaphoreBoundedBuffer<Integer>(20);
		
		Thread taker = new Thread()
				{
					@Override
					public void run() 
					{
						try
						{
							sbb.take();
							fail();
						}
						catch(InterruptedException success)
						{
							System.out.println("Succesfully interrupted");
						}
					};
				};
				
		taker.start();
		Thread.sleep(LOCKUP_DETECTION_TIMEOUT);
		taker.interrupt();
		taker.join(LOCKUP_DETECTION_TIMEOUT);
		assertFalse(taker.isAlive());
	}
}
