package classExamples.boundedBuffer;

import junit.framework.TestCase;

/*
 * Modded from Chapter 11 of Java Concurrency in Practice
 */
public class SingleThreadUnitTest extends TestCase
{
	public void testIfEmptyWhenConstructed()
	{
		SemaphoreBoundedBuffer<Integer> sbb = new SemaphoreBoundedBuffer<>(10);
		assertTrue(sbb.isEmpty());
		assertFalse(sbb.isFull());
	}	
	
	public void testOrderPreserved() throws Exception
	{
		int numPuts = 10;
		SemaphoreBoundedBuffer<Integer> sbb = new SemaphoreBoundedBuffer<>(numPuts);
		
		assertTrue(sbb.isEmpty());
		for( int x=0; x < numPuts-1; x++ )
		{
			sbb.put(x);
			assertFalse(sbb.isEmpty());
			assertFalse(sbb.isFull());
		}
		
		sbb.put(numPuts);
		assertFalse(sbb.isEmpty());
		assertTrue(sbb.isFull());
		
		for( int x=0; x < numPuts -1; x++)
		{
			assertEquals(sbb.take().intValue(), x);
			assertFalse(sbb.isEmpty());
			assertFalse(sbb.isFull());
		}
		
		assertEquals(sbb.take().intValue(), numPuts);
		assertTrue(sbb.isEmpty());
		assertFalse(sbb.isFull());

	}
}
