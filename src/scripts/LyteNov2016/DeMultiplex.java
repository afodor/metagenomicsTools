package scripts.LyteNov2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.FastQ;
import utils.ConfigReader;
import utils.Translate;

public class DeMultiplex
{
	
	private static HashMap<String, String> getBarcodeMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigReader.getLyteNov2016Dir() + File.separator + "Lyte_mapping_11102016.txt"	));
			
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String seq = splits[1];
			
			if( map.containsKey(seq))
				throw new Exception("No");
			
			map.put(seq,splits[0]);
		}
		
		return map;
		
	}
	
	// can't seem to find the barcodes in the sequences
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> demap = getBarcodeMap();
		for(String s : demap.keySet())
			System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLyteNov2016Dir() + File.separator + "Lyte_seqs_R1.fastq"	)));
		
		int numFound =0;
		int numSearched =0;
		
		for( FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; fastq = FastQ.readOneOrNull(reader))
		{
			boolean foundOne = false;
			numSearched++;
			String match = null;
			String reverse = Translate.safeReverseTranscribe(fastq.getSequence());
			for( String s : demap.keySet())
			{
				if( reverse.indexOf(s) != -1)
				{
					foundOne = true;
					match = s;
				}
					
			}
			
			if( foundOne)
			{
				numFound++;
				System.out.println(match + " " + reverse.substring("CCGGACTACHVGGGTWTCTAAT".length()));
			}
				
			if( numSearched % 1000 ==0 )
				System.out.println(numFound + " " + numSearched );
		}
		
		System.out.println(numFound + " " + numSearched);
	}
}
