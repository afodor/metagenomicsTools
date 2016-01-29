package scripts.TopeDiverticulosisJan2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;
import utils.Translate;

public class Demultiplex
{
	private static BufferedWriter getFromMap(HashMap<String, BufferedWriter> map, String key) throws Exception
	{
		BufferedWriter writer = map.get(key);
		
		if( writer == null)
		{
			writer = new BufferedWriter(new FileWriter(new File( 
					ConfigReader.getTopJan2016Dir() + File.separator + 
						"fastaOut" + File.separator + 
					key + ".fasta")));
			
			map.put(key, writer);
		}
		
		return writer;
	}
	
	// forward@reverse is the key
	public static HashMap<String, String> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
						"Mapping file for Demux.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 13)
				throw new Exception("No");
			
			String id = splits[2];
			
			if(map.containsValue(id))
				throw new Exception("No");
			
			String primerKey = splits[11] + "@" + Translate.reverseTranscribe(splits[12]);
			
			if( map.containsKey(primerKey))
				throw new Exception("No");
			
			map.put(primerKey,id);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> primerMap = MergeTwo.getMergedMap();
		
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
				new BufferedReader(new FileReader(
						ConfigReader.getTopJan2016Dir() + File.separator + 
								"hyusc.dbcreadsr1"));
		
		BufferedReader reader2 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
								ConfigReader.getTopJan2016Dir() +
								File.separator +  "ofiad.dbcreadsr2.gz"))));
		
		BufferedReader reader3 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
								ConfigReader.getTopJan2016Dir() +
							File.separator + 	"nqxdy.dbcreadsr3.gz"))));
		
		// this file got truncated!
		//BufferedReader reader4 = 
			//	new BufferedReader(new InputStreamReader( 
				//		new GZIPInputStream( new FileInputStream(
					//			ConfigReader.getTopJan2016Dir() +""))));
		
		long numDone = 0;
		long numMatched = 0;
		for( FastQ fastq2 = FastQ.readOneOrNull(reader2); fastq2 != null;
				fastq2 = FastQ.readOneOrNull(reader2))
		{
			boolean gotOne = false;
			
			FastQ fastq1 = FastQ.readOneOrNull(reader1);
			FastQ fastq3 =  FastQ.readOneOrNull(reader3);
			//FastQ fastq4 = FastQ.readOneOrNull(reader4);
			

			if( fastq1 == null)
				throw new Exception("No");

			if( fastq3 == null)
				throw new Exception("No");
			
			//if( fastq4 == null)
				//throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq3.getFirstTokenOfHeader()))
					throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq1.getFirstTokenOfHeader()))
				throw new Exception("No");
			
			//if( ! fastq2.getFirstTokenOfHeader().equals(fastq4.getFirstTokenOfHeader()))
				//throw new Exception("No");
			
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
								gotOne = true;
								
								BufferedWriter forwardWriter = 
										getFromMap(writerMap, id +"_1");
								
								forwardWriter.write(">" + fastq1.getFirstTokenOfHeader() + "\n");
								forwardWriter.write(fastq1.getSequence() + "\n");
								forwardWriter.flush();
					
								/*
								BufferedWriter backwardsWriter = 
										getFromMap(writerMap, id+ "_4");
								
								backwardsWriter.write(">" + fastq4.getFirstTokenOfHeader() + "\n");
								backwardsWriter.write(fastq4.getSequence() + "\n");
								backwardsWriter.flush();
								*/
								
							}
								
						}
							
					}
				}
					
			if( gotOne)
				numMatched++;
			
			
			numDone++;
			
			if( numDone % 10000 == 0)
				System.out.println( numMatched + " " +  numDone + " " + 
						((double)numMatched / numDone));
		}
		
		if( numDone % 10000 == 0)
			System.out.println( numMatched + " " +  numDone + " " + 
					((double)numMatched / numDone));
		
		if(  FastQ.readOneOrNull(reader3) != null )
			throw new Exception("No");

		if(  FastQ.readOneOrNull(reader1) != null )
			throw new Exception("No");

		//if(  FastQ.readOneOrNull(reader4) != null )
			//throw new Exception("No");
		
		for( BufferedWriter writer : writerMap.values())
		{
			writer.flush();  writer.close();
		}
		
		reader1.close();
		reader2.close();
		reader3.close();
		///reader4.close();
		
		System.out.println("Finished");
	}
}
