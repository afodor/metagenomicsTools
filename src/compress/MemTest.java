package compress;

import java.util.HashSet;

public class MemTest
{
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		HashSet<String> set = new HashSet<String>();
		
		while(true)
		{
			set.add( EncodeACGT.getRandomKMer(100000));
			
			if( set.size() % 100 == 0)
			{
				float elapsedTime = (System.currentTimeMillis() -startTime) / 1000f;
				System.out.println(set.size() + " " + elapsedTime/set.size());
			}
		}
	}
}
