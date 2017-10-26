package classExamples.semaphoreAndBlockingQueues;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class WriteAllGCSingleThread
{
	public static final File SEQUENCE_DIR = new File( "C:\\AdenomasForRoshonda\\OriginalFiles");
	
	private static List<Float> resultsList = new ArrayList<Float>();
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		String[] fileNames = SEQUENCE_DIR.list();
		
		for(  String fileName : fileNames )
		{
			
			if( fileName.endsWith(".fas"))
			{
				File seqFile = new File(SEQUENCE_DIR.getAbsolutePath() + File.separator + fileName);
				System.out.println(seqFile.getAbsolutePath());
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(seqFile);
				
				for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
					resultsList.add(fs.getGCRatio());
			}
		}
		
		System.out.println("Finished with " + resultsList.size());
		System.out.println("Time " + ((System.currentTimeMillis() - startTime) / 1000f));
	}
}
