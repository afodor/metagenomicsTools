package classExamples;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayTest
{
	public static void main(String[] args)
	{
		List<Integer> list =  new CopyOnWriteArrayList<Integer>();
		
		long startTime = System.currentTimeMillis();
		
		for( int x=0; x < 50000; x++)
			list.add(x);
		
		long endTime = System.currentTimeMillis();
		System.out.println( (endTime - startTime)/1000f );
		
		System.out.println(list.size());
	}
}
