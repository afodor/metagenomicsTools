package scratch;

import java.io.File;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class ClassExample 
{
	public static void main(String[] args) throws Exception
	{
		File myFile = new File("c:\\pathToSomeFastaFile.txt");
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(myFile);
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; 
						fs =fsoat.getNextSequence())
		{
			new Thread(new SequenceWorker(fs)).start();
		}
		
		fsoat.close();
	}
	
	private static class SequenceWorker implements Runnable
	{
		private final FastaSequence aSequence;
		
		public SequenceWorker(FastaSequence seq) 
		{
			this.aSequence = seq;
		}
		
		@Override
		public void run() 
		{
			// do something slow with aSequence
		}
	}
}
