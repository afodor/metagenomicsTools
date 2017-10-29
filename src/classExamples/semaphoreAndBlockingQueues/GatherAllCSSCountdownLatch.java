package classExamples.semaphoreAndBlockingQueues;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class GatherAllCSSCountdownLatch
{
	public static final File SEQUENCE_DIR = new File( "C:\\AdenomasForRoshonda\\OriginalFiles");
	
	private static final List<Float> resultsList = Collections.synchronizedList(new ArrayList<Float>());

	private static class Worker implements Runnable
	{
		private final File file;
		private final CountDownLatch cdl;
		
		public Worker(File file, CountDownLatch cdl)
		{
			this.file = file;
			this.cdl = cdl;
		}
		
		@Override
		public void run()
		{
			try
			{
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(file);
				
				for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
					resultsList.add(fs.getGCRatio());
				
				cdl.countDown();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		int numThreads =0;
		
		String[] fileNames = SEQUENCE_DIR.list();
		
		for(  String fileName : fileNames )
		{	
			if( fileName.endsWith(".fas"))
				numThreads++;
		}
		
		System.out.println("Trying with " + numThreads);
		
		CountDownLatch cdl = new CountDownLatch(numThreads);
		
		for(  String fileName : fileNames )
		{	
			if( fileName.endsWith(".fas"))
			{
				File seqFile = new File(SEQUENCE_DIR.getAbsolutePath() + File.separator + fileName);
				Worker w = new Worker(seqFile, cdl);
				new Thread(w).start();;
			}
		}
		
		cdl.await();
		
		System.out.println("Count down latch finished with " + resultsList.size());
		System.out.println("Time " + ((System.currentTimeMillis() - startTime) / 1000f));
	}
}
