package classExamples.semaphoreAndBlockingQueues;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class GatherAllCSSSimpleSemaphore
{
	public static final File SEQUENCE_DIR = new File( "C:\\AdenomasForRoshonda\\OriginalFiles");
	
	private static final List<Float> resultsList = Collections.synchronizedList(new ArrayList<Float>());
	private static final int NUM_WORKERS = 4;

	private static class Worker implements Runnable
	{
		private final File file;
		private final Semaphore semaphore;
		
		public Worker(File file, Semaphore semaphore)
		{
			this.file = file;
			this.semaphore = semaphore;
		}
		
		@Override
		public void run()
		{
			try
			{
				System.out.println(file.getAbsolutePath());
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(file);
				
				for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
					resultsList.add(fs.getGCRatio());
				
				semaphore.release();
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
		
		Semaphore semaphore = new Semaphore(NUM_WORKERS);
		
		String[] fileNames = SEQUENCE_DIR.list();
		
		for(  String fileName : fileNames )
		{	
			if( fileName.endsWith(".fas"))
			{
				semaphore.acquire();
				File seqFile = new File(SEQUENCE_DIR.getAbsolutePath() + File.separator + fileName);
				Worker w = new Worker(seqFile, semaphore);
				new Thread(w).start();;
			}
		}
		
		int numAcquired =0;
		
		while( numAcquired < NUM_WORKERS )
		{
			semaphore.acquire();
			numAcquired++;
		}
		
		System.out.println("Finished with " + resultsList.size());
		System.out.println("Time " + ((System.currentTimeMillis() - startTime) / 1000f));
	}
}
