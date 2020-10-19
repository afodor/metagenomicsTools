package compress;

import java.util.HashSet;

public class MemTest
{
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		HashSet<EncodedSequence> set = new HashSet<EncodedSequence>();
		
		while(true)
		{
			set.add( new EncodedSequence( EncodeACGT.getRandomKMer(1000)));
			
			if( set.size() % 1000 == 0)
			{
				float elapsedTime = (System.currentTimeMillis() -startTime) / 1000f;
				System.out.println(set.size() + " " + elapsedTime/set.size());
			}
		}
	}
}
