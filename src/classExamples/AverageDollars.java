package classExamples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class AverageDollars
{
	public static void main(String[] args) throws Exception
	{
		Random random = new Random();
		long numFlips = 1000;
		
		int numTrials = 10000000;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\temp.txt")));
		
		double sum =0;
		
		for( int x=0; x < numTrials; x++)
		{
			int numDollars =0;
			
			for( long y=0; y < numFlips; y++)
			{
				if( random.nextFloat() <= .5)
					numDollars++;
				else
					numDollars--;
			}
			
			writer.write(numDollars + "\n");
			writer.flush();
			
			sum += Math.sqrt( numDollars * numDollars);

			System.out.println( x + " " +  sum/ (x+1) +   " " + Math.sqrt( 2* (numFlips) / Math.PI) );
		}
		
		writer.flush();  writer.close();
	}
}
