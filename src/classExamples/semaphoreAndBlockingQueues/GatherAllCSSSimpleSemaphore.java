package classExamples.semaphoreAndBlockingQueues;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class GatherAllCSSSimpleSemaphore
{
	public static final File SEQUENCE_DIR = new File( "C:\\AdenomasForRoshonda\\OriginalFiles");
	
	private static final List<Float> resultsList = Collections.synchronizedList(new ArrayList<Float>());
	private static final int NUM_WORKERS = 4;
	
	private static FutureTask<List<Float>> getResultsListAsFuture(File inputFile, Semaphore semaphore)
	{
		return new FutureTask<>( new Callable<List<Float>>()
		{
			public List<Float> call() throws Exception 
			{
				System.out.println(inputFile.getAbsolutePath());
				List<Float> threadLocalList = new ArrayList<>();
				
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inputFile);
				
				for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
					threadLocalList.add(fs.getGCRatio());
				
				semaphore.release();
				return threadLocalList;
				
			};
		}
		);
	}
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		Semaphore semaphore = new Semaphore(NUM_WORKERS);
		
		String[] fileNames = SEQUENCE_DIR.list();
		
		List<FutureTask<List<Float>>> tasks = new ArrayList<>();
		
		for(  String fileName : fileNames )
		{	
			if( fileName.endsWith(".fas"))
			{
				semaphore.acquire();
				File seqFile = new File(SEQUENCE_DIR.getAbsolutePath() + File.separator + fileName);
				FutureTask<List<Float>> fTask = getResultsListAsFuture( seqFile,semaphore);
				tasks.add(fTask);
				new Thread(fTask).start();;
			}
		}
		
		for( FutureTask<List<Float>> fTask : tasks)
			resultsList.addAll(fTask.get());
		
		System.out.println("Finished with " + resultsList.size());
		System.out.println("Time " + ((System.currentTimeMillis() - startTime) / 1000f));
	}
}
