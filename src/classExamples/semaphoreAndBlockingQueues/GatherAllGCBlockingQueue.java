package classExamples.semaphoreAndBlockingQueues;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class GatherAllGCBlockingQueue
{
	public static final File SEQUENCE_DIR = new File( "C:\\AdenomasForRoshonda\\OriginalFiles");
	
	private static final List<Float> resultsList = Collections.synchronizedList(new ArrayList<Float>());
	private static final AtomicLong numberDone = new AtomicLong();
	private static final AtomicLong numJobsSubmitted = new AtomicLong(0);
	private static final int NUM_WORKERS = 4;
	
	private static class Worker implements Runnable
	{
		private final BlockingQueue<File> queue;
		
		public Worker(BlockingQueue<File> queue)
		{
			this.queue = queue;
		}
		
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					File file = queue.take();
					System.out.println(file.getAbsolutePath());
					FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(file);
					
					for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
						resultsList.add(fs.getGCRatio());
					
					numJobsSubmitted.decrementAndGet();
				}				
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
		
		BlockingQueue<File> queue = new ArrayBlockingQueue<File>(1000);
		
		for( int x=0; x < NUM_WORKERS; x++)
			new Thread(new Worker(queue)).start();
		
		String[] fileNames = SEQUENCE_DIR.list();
		
		for(  String fileName : fileNames )
		{	
			if( fileName.endsWith(".fas"))
			{
				File seqFile = new File(SEQUENCE_DIR.getAbsolutePath() + File.separator + fileName);
				numJobsSubmitted.incrementAndGet();
				queue.put(seqFile);
			}
		}
		
		while( ! queue.isEmpty() || numJobsSubmitted.get() > 0  )
			Thread.yield();
		
		System.out.println("Finished with " + resultsList.size());
		System.out.println("Time " + ((System.currentTimeMillis() - startTime) / 1000f));
	}
}
