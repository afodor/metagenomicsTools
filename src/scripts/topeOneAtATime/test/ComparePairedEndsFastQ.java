package scripts.topeOneAtATime.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;

public class ComparePairedEndsFastQ
{
	public static void main(String[] args) throws Exception
	{
		HashMap<File, File> pairedMap = getPairedMap();
		
		for(File f : pairedMap.keySet())
			checkPairedEnds(f, pairedMap.get(f));
	}
	
	private static void checkPairedEnds(File fastQ1, File fastQ2) throws Exception
	{
		System.out.println(fastQ1.getAbsolutePath() + " " + fastQ2.getAbsolutePath());
		BufferedReader fastqReader1 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( fastQ1))));
		
		BufferedReader fastqReader2 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( fastQ2))));
		
		for( FastQ fq1 = FastQ.readOneOrNull(fastqReader1); 
				fq1 != null; fq1 = FastQ.readOneOrNull(fastqReader1))
		{
			FastQ fq2 = FastQ.readOneOrNull(fastqReader2); 
			
			if( ! fq1.getFirstTokenOfHeader().equals(fq2.getFirstTokenOfHeader()))
				throw new Exception("No " + fq1.getFirstTokenOfHeader() + " " + fq2.getFirstTokenOfHeader());
		}
		
		if( FastQ.readOneOrNull(fastqReader2) != null)
			throw new Exception("Extra sequence");
		
		fastqReader1.close();
		fastqReader2.close();
	}
	
	private static HashMap<File, File> getPairedMap() throws Exception
	{
		HashMap<File, File>  map = new HashMap<>();
		
		String fastqDir = "C:\\topeOneAtATime\\fastqOut";
		String[] fastqIn = new File(fastqDir).list();
		
		for(String s : fastqIn)
		{
			if( s.indexOf("_1.fastq") != -1)
			{
				File fasta1 = new File( fastqDir + File.separator + s );
				File fasta2 = new File(fastqDir + File.separator + s.replace("_1.fastq", "_2.fastq"));
				
				if( !fasta2.exists())
					throw new Exception("No");
				
				if( fasta1.getName().equals(fasta2.getName()))
					throw new Exception("No");
				
				map.put(fasta1, fasta2);
						
			}
		}
		
		return map;
	}
}
