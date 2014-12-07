package scripts.vanderbilt;

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

public class CheckWholeGenomeMetagenome
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = getMatchingSequences();
		System.out.println(set);
		System.out.println(set.size());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( ConfigReader.getVanderbiltDir() + 
						File.separator + "MSHRM1" + File.separator + "lane1_Undetermined_L001_R1_001.fastq.gz"))));
		
		int numChecked=0;
		int numMatched=0;
		for(FastQ fq = FastQ.readOneOrNull(reader); fq != null; fq = FastQ.readOneOrNull(reader))
		{
			numChecked++;
			
			String aSeq = fq.getSequence().trim();
			String backSeq = Translate.safeReverseTranscribe(fq.getSequence().trim());
			
			boolean match = false;
			for(String s : set)
			{
				if( aSeq.startsWith(s) || aSeq.endsWith(s)  || backSeq.startsWith(s) || backSeq.endsWith(s))
					match = true;
			}
			
			if( match)
				numMatched++;
			
			if(numChecked % 100000 ==0)
				System.out.println(numChecked + " " + numMatched);
		}
	}
	
	private static HashSet<String> getMatchingSequences() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getVanderbiltDir() + File.separator + "mapping file 1.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			set.add(splits[1].trim());
			set.add(splits[2].trim());
			set.add( Translate.reverseTranscribe(splits[1].trim()));
			set.add( Translate.reverseTranscribe(splits[2].trim()));
		}
		
		reader.close();
		
		return set;
	}
}
