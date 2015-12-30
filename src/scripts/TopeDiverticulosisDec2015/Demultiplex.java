package scripts.TopeDiverticulosisDec2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
	// forward@reverse is the key
	private static HashMap<String, String> getBarcodeToSampleMap() throws Exception
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
			
			String primerKey = splits[11] + "@" + splits[12];
			
			if( map.containsKey(primerKey))
				throw new Exception("No");
			
			map.put(primerKey,id);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> primerMap = getBarcodeToSampleMap();
		
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
		
		BufferedReader reader2 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
						ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
						"local" + File.separator +  "scratch" + File.separator  + "mhumphrys" + 
								File.separator + 
									"APJR3_20151123_M01994_IL100064365_NoIndex_L001_R2.fastq.gz"))));
		
		BufferedReader reader3 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
						ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
						"local" + File.separator +  "scratch" + File.separator  + "mhumphrys" + 
								File.separator + 
									"APJR3_20151123_M01994_IL100064365_NoIndex_L001_R3.fastq.gz"))));
		
		long numDone = 0;
		long numMatched = 0;
		for( FastQ fastq2 = FastQ.readOneOrNull(reader2); fastq2 != null;
				fastq2 = FastQ.readOneOrNull(reader2))
		{
			boolean gotOne = false;
			
			FastQ fastq3 =  FastQ.readOneOrNull(reader3);
			
			if( fastq3 == null)
				throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq3.getFirstTokenOfHeader()))
					throw new Exception("No");
			
			for( String p3 : primer3Seqs)
				if( fastq3.getSequence().startsWith( p3 ))
				{
					for(String p2 : primer2Seqs)
					{
						if( fastq2.getSequence().startsWith( Translate.reverseTranscribe( p2)))

							gotOne = true;
					}
				}
					
			if( gotOne)
				numMatched++;
			
			
			numDone++;
			
			if( numDone % 10000 == 0)
				System.out.println( numMatched + " " +  numDone);
		}
		
		if(  FastQ.readOneOrNull(reader3) != null )
			throw new Exception("No");
	}
}
