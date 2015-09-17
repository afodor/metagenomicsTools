package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;
import utils.Translate;

public class CompareForwardBackwardPrimers
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + 
				"Copy of DHSV Illumina Sets 1 and 2 Metadata.txt")));
		
		reader.readLine();
		
		for( String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			set1.add( Translate.reverseTranscribe(splits[4]));
			set2.add( splits[9]);	
		}
		
		reader.close();
		
		System.out.println(set1);
		System.out.println(set2);
		
		File file1 = new File(ConfigReader.getTopeSep2015Dir() + File.separator + 
				"Humphries_R2_001.fastq.gz" );
		findHits(set1, file1);
		
		System.out.println("FILE 2");
		
		File file2 = new File(ConfigReader.getTopeSep2015Dir() + File.separator + 
				"Humphries_R3_001.fastq.gz" );
		findHits(set2, file2);
		
		set1.retainAll(set2);
		System.out.println("COMMON " + set1.size());
	}
	
	private static void findHits( HashSet<String> primerSet, File file ) throws Exception
	{
		BufferedReader reader =
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file))));
		long numHits =0;
		long numSeqs=0;
		
		for( FastQ fastQ = FastQ.readOneOrNull(reader); fastQ != null;
						fastQ = FastQ.readOneOrNull(reader))
		{
			numSeqs++;
			
			if( primerSet.contains(fastQ.getSequence()))
			{
				//System.out.println(fastQ.getSequence());
				numHits++;
				
			}
				
			if( numSeqs % 1000000 == 0 )
				System.out.println( numHits + " " + numSeqs + " " + ((double)numHits / numSeqs));
				
		}
		
		System.out.println( numHits + " " + numSeqs + " " + ((double)numHits / numSeqs));
		
		reader.close();
		
	}
}
