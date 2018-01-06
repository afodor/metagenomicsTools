package scripts.topeOneAtATime.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class CompareFastaFastQ
{
	public static void main(String[] args) throws Exception
	{
		HashMap<File, File> map3 = get3Files();
		
		for(File f : map3.keySet())
			checkFastQToFastA(f, map3.get(f));
	}
	
	private static void checkFastQToFastA(File fastQ, File fasta) throws Exception
	{
		System.out.println(fastQ.getAbsolutePath() + " " + fasta.getAbsolutePath());
		BufferedReader fastqReader = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( fastQ))));
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(fasta, true);
		
		for( FastQ fq = FastQ.readOneOrNull(fastqReader); fq != null; fq = FastQ.readOneOrNull(fastqReader))
		{
			FastaSequence fs = fsoat.getNextSequence();
			
			if( ! fq.getFirstTokenOfHeader().equals(fs.getFirstTokenOfHeader()))
				throw new Exception("No " + fq.getFirstTokenOfHeader() + " " + fs.getFirstTokenOfHeader());
			
			if( ! fq.getSequence().equals(fs.getSequence()))
				throw new Exception("No " + fq.getSequence() + " " + fs.getSequence());
			
		}
		
		if( fsoat.getNextSequence() != null)
			throw new Exception("Extra sequence");
		
		fastqReader.close();
		fsoat.close();
	}
	
	private static HashMap<File, File> get3Files() throws Exception
	{
		HashMap<File, File> map = new HashMap<>();
		String fastqDir = "C:\\topeOneAtATime\\fastqOut";
		String[] fastqIn = new File(fastqDir).list();
		
		for( String s : fastqIn)
			if( s.indexOf("file3") != -1)
			{
				File fastqFile = new File(fastqDir + File.separator + s);
				
				if( s.indexOf("_1") != -1)
				{
					String firstToken = new StringTokenizer(s, "_").nextToken();
					
					File fastaFile = new File("C:\\topeOneAtATime\\file3\\fastaOut\\" 
						+ firstToken	+ "_1_set3.fasta.gz");
					
					if( ! fastaFile.exists())
						throw new Exception("Could not find " + fastaFile.getAbsolutePath());
					
					map.put(fastqFile, fastaFile);
				}
				else if( s.indexOf("_2") != -1)
				{
					String firstToken = new StringTokenizer(s, "_").nextToken();
					
					File fastaFile = new File("C:\\topeOneAtATime\\file3\\fastaOut\\" 
						+ firstToken	+ "_4_set3.fasta.gz");
					
					if( ! fastaFile.exists())
						throw new Exception("Could not find " + fastaFile.getAbsolutePath());
					
					map.put(fastqFile, fastaFile);
				}
					
			}
		
		return map;
	}
}
