package classExamples;

import java.util.HashMap;
import java.util.Random;

public class TestMultinomial
{	
	private static final Random random = new Random();
	
	public static void main(String[] args) throws Exception
	{
		long numTrials = 5000000000l;
		int numRolls = 10;
		double[] probs = { 0.2, 0.1, 0.1, 0.1, 0.1, 0.5};
		
		long numMatch = 0;
		
		HashMap<Integer, Integer> expected =getExpected();
		
		for ( long x=0; x < numTrials; x++)
		{
			if( roll(probs, numRolls).equals(expected))
				numMatch++;
			
			if( x % 10000000 == 0 )
			{
				System.out.println( ((double)numMatch) / (x+1));
			}	
		}
		
		System.out.println( (double)(numMatch) / numTrials);
	}
	
	private static HashMap<Integer, Integer> getExpected() throws Exception
	{
		HashMap<Integer, Integer> map = new HashMap<>();
		
		map.put(1, 3);
		map.put(2, 1);
		map.put(3, 1);
		//map.put(4, 0);
		map.put(5, 1);
		map.put(6, 4);
		
		return map;
	}
	
	private static HashMap<Integer, Integer> roll( double[] probs,  int numRolls )
	{
		HashMap<Integer, Integer> map = new HashMap<>();
		
		for( int x=0; x < numRolls; x++)
		{
			int roll = rollTheDice(probs);
			
			Integer count = map.get(roll);
			
			if( count == null)
				count =0;
			
			count++;
			map.put(roll, count);
		}
		
		return map;
	}
	
	private static int rollTheDice( double[] probs )
	{
		double sum = 0;
		double d = random.nextDouble();
		
		for( int x=0; x < probs.length;x++)
		{
			sum = sum + probs[x];
			
			if( d <= sum )
				return (x+1);
		}
		
		return probs.length;
	}
}
