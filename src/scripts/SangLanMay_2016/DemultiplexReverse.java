package scripts.SangLanMay_2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class DemultiplexReverse
{
	private static BufferedWriter getFromMap(HashMap<String, BufferedWriter> map, String key) throws Exception
	{
		BufferedWriter writer = map.get(key);
		
		if( writer == null)
		{
			writer = new BufferedWriter(new FileWriter(new File( 
					ConfigReader.getSangLabMay2016Dir() + File.separator + 
					"fastaOut" + File.separator +  key + "_read2"+  ".fasta")));
			
			map.put(key, writer);
		}
		
		return writer;
	}

	
	public static void main(String[] args) throws Exception
	{
		long numRead =0 ;
		long numMatch=0;
		
		HashMap<String, String> barcodeMap = BarcodeFileLine.getBarcodeToSampleMap();
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
		
		BufferedReader reader1 = 
				new BufferedReader(new InputStreamReader( new GZIPInputStream(
						new FileInputStream(
								ConfigReader.getSangLabMay2016Dir() + File.separator + 
								"SAMPL1-21_S4_L001_R2_001.fastq.gz"))));
		
		for( FastQ fq =  FastQ.readOneOrNull(reader1); fq != null;  fq =FastQ.readOneOrNull(reader1)  )
		{
			numRead++;
			String seq = fq.getSequence();
			
			for(String s : barcodeMap.keySet())
			{
				if( seq.startsWith(s))
				{
					BufferedWriter writer =getFromMap(writerMap, barcodeMap.get( s) );
					
					writer.write(">" + fq.getHeader() + "\n");
					writer.write(seq.substring(s.length() + "AGRGTTTGATCMTGGCTCAG".length() + 1) + "\n");
					writer.flush();
					
					numMatch++;
					
				}
			}
					
			if( numRead % 10000 == 0 )
				System.out.println(numMatch + " " + numRead + " " + ((double)(numMatch)) / numRead );
		}
			
		for(BufferedWriter writer : writerMap.values())
		{
			writer.flush(); writer.close();
		}
	}
}
