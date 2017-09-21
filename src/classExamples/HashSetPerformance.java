package classExamples;

import java.util.HashSet;

public class HashSetPerformance
{
	private static class MyObject
	{
		@Override
		public int hashCode()
		{
			// constant hashCode; legal but will degrade performance
			return -42;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		int numTrials =20000;
		
		HashSet<Object> set = new HashSet<>();
		
		long startTime = System.currentTimeMillis();
		
		for( int x=0; x < numTrials; x++)
			set.add(new Object());
		
		System.out.println("Time using objects "  + (System.currentTimeMillis()-startTime)/1000f);
		
		set = new HashSet<>();
		
		startTime = System.currentTimeMillis();
		
		for( int x=0; x < numTrials; x++)
			set.add(new MyObject());
		
		System.out.println("Time using slow hashCode  "  + (System.currentTimeMillis()-startTime)/1000f);
		
	}
	
}
