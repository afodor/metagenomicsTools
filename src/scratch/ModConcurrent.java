package scratch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModConcurrent
{
	public static void main(String[] args) throws Exception
	{
		final Map<String, Integer> myMap =
				new ConcurrentHashMap<String, Integer>();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				for( int x=0; x < 10000000; x++)
					myMap.put("" + x, x);
			}
		}).start();
		
		Thread.sleep(50);
		
		int numGot =0;
		for( @SuppressWarnings("unused") String s : myMap.keySet())
			numGot++;
		
		System.out.println(numGot);
	}
}
