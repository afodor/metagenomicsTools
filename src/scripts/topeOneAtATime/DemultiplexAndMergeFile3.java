package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.zip.GZIPInputStream;

import merger.MergerWorker;
import parsers.FastQ;
import utils.ConfigReader;

public class DemultiplexAndMergeFile3
{
	private static BufferedWriter getFromMap(HashMap<String, BufferedWriter> map, String key) throws Exception
	{
		BufferedWriter writer = map.get(key);
		
		if( writer == null)
		{
			writer = new BufferedWriter(new FileWriter(new File( 
					ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"file3" + File.separator + 
						"mergedOut" + File.separator + 
					key + ".fasta")));
			
			map.put(key, writer);
		}
		
		return writer;
	}
	
	public static void main(String[] args) throws Exception
	{
		int numKeys = 4;
		Semaphore semaphore = new Semaphore(numKeys);
		HashMap<String, String> primerMap = DemultiplexFile3.getAllMap(3);
		
		HashSet<String> primer3Seqs = new HashSet<String>();
		
		for(String s : primerMap.keySet())
			primer3Seqs.add(new StringTokenizer(s,"@").nextToken());
		
		for(String s : primerMap.keySet())
			System.out.println(s + " "+ primerMap.get(s));
		
		HashSet<String> primer2Seqs = new HashSet<String>();
		
		for(String s : primerMap.keySet())
		{
			String[] splits = s.split("@");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			primer2Seqs.add(splits[1]);
		}
		
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();

		BufferedReader reader1 = 
				new BufferedReader(new InputStreamReader( new GZIPInputStream(
						new FileInputStream(
								ConfigReader.getTopeOneAtATimeDir() + File.separator + 
								"file3" + File.separator + 
								"DBCreads_R1_001.fastq.gz"))));
		
		BufferedReader reader2 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
								ConfigReader.getTopeOneAtATimeDir() + File.separator + 
								"file3" + File.separator + 
								File.separator +  "DBCreads_R2_001.fastq.gz"))));
		
		BufferedReader reader3 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
								ConfigReader.getTopeOneAtATimeDir() + File.separator + 
								"file3" + File.separator + 
							File.separator + 	"DBCreads_R3_001.fastq.gz"))));
		
		BufferedReader reader4 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
								ConfigReader.getTopeOneAtATimeDir() + File.separator + 
								"file3" + File.separator + 
										"DBCreads_R4_001.fastq.gz"))));
		
		long numDone = 0;
		long numMatched = 0;
		for( FastQ fastq2 = FastQ.readOneOrNull(reader2); fastq2 != null;
				fastq2 = FastQ.readOneOrNull(reader2))
		{
			boolean gotOne = false;
			
			FastQ fastq1 = FastQ.readOneOrNull(reader1);
			FastQ fastq3 =  FastQ.readOneOrNull(reader3);
			FastQ fastq4 = FastQ.readOneOrNull(reader4);
			

			if( fastq1 == null)
				throw new Exception("No");

			if( fastq3 == null)
				throw new Exception("No");
			
			if( fastq4 == null)
				throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq3.getFirstTokenOfHeader()))
					throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq1.getFirstTokenOfHeader()))
				throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq4.getFirstTokenOfHeader()))
				throw new Exception("No");
			
			for( String p3 : primer3Seqs)
				if( fastq3.getSequence().startsWith( p3 ))
				{
					for(String p2 : primer2Seqs)
					{
						if( fastq2.getSequence().startsWith( p2))
						{
							
							String key = p3 + "@" + p2;
							
							String id = primerMap.get(key);
							
							if( id != null)
							{
								semaphore.acquire();
								gotOne = true;
								
								BufferedWriter writer= 
										getFromMap(writerMap, id +"_merged" + "_set3");
								
								MergerWorker worker =new MergerWorker(
										fastq1, fastq4, writer, semaphore, key,70);
								
								new Thread(worker).start();
								
							}		
						}
					}
				}
					
			if( gotOne)
				numMatched++;
			
			numDone++;
			
			if( numDone % 10000 == 0)
				System.out.println( "Attempted " +  numMatched + " " +  numDone + " " + 
						((double)numMatched / numDone));
		}
		
		for( int x=0; x < numKeys; x++)
			semaphore.acquire();
		
		System.out.println( "Attempted " +  numMatched + " " +  numDone + " " + 
				((double)numMatched / numDone));
		
		if(  FastQ.readOneOrNull(reader3) != null )
			throw new Exception("No");

		if(  FastQ.readOneOrNull(reader1) != null )
			throw new Exception("No");

		if(  FastQ.readOneOrNull(reader4) != null )
			throw new Exception("No");
		
		for( BufferedWriter writer : writerMap.values())
		{
			writer.flush();  writer.close();
		}
		
		reader1.close();
		reader2.close();
		reader3.close();
		reader4.close();
		
		System.out.println("Finished");
	}
}
