package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class BarcodeFileLine
{
	private static BufferedWriter getOrAddToCache( HashMap<String, BufferedWriter> map, String key )
		throws Exception
	{
		BufferedWriter writer = map.get(key);
		
		if( writer == null)
		{
			writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(
					ConfigReader.getIanNov2015Dir() + File.separator + 
					"fastaFiles" + File.separator + key + ".fasta.gz"))));
			map.put(key, writer);
		}
		
		return writer;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
		
		HashMap<String, DeMultiplex> sampleIDs =DeMultiplex.getBarcodeMap();
		
		BufferedReader leftReader = new BufferedReader(new FileReader(new File(
			ConfigReader.getIanNov2015Dir() + 
			File.separator + "behavbugs_noindex_l001_r1_001" + File.separator + 
				"azzps" + File.separator + 	"azzps.behavbugsn")));
		
		BufferedReader middleReader = new BufferedReader(
				new FileReader(new File(
			ConfigReader.getIanNov2015Dir() + File.separator + 
			"behavbugs_noindex_l001_r2_001" +  File.separator + "mcsmm" + 
					File.separator + "mcsmm.behavbugsn")));
	
		BufferedReader rightReader = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( 
					ConfigReader.getIanNov2015Dir() + File.separator + 
					 "behavbugs_noindex_l001_r3_001" + File.separator + "pdpaj" 
					 	+ File.separator + 
					 		"pdpaj.behavbugsn.gz"))));
		
		int numDone =0;
		int numSkipped =0;
		
		for( FastQ leftSeq = FastQ.readOneOrNull(leftReader) ; leftSeq != null;
				leftSeq = FastQ.readOneOrNull(leftReader) )
		{

			FastQ middleSeq = FastQ.readOneOrNull(middleReader);
			FastQ rightSeq = FastQ.readOneOrNull(rightReader);
			
			String firstToken = leftSeq.getFirstTokenOfHeader();
			
			if( ! firstToken.equals(middleSeq.getFirstTokenOfHeader()))
					throw new Exception("No " + leftSeq.getHeader());
			
			if( ! firstToken.equals(rightSeq.getFirstTokenOfHeader()))
					throw new Exception("No " + leftSeq.getHeader());

			Integer sample = null;
			
			for( String s : sampleIDs.keySet())
				if( sample == null &&  middleSeq.getSequence().startsWith(s))
					sample = sampleIDs.get(s).getSampleID();
			
			if( sample != null)
			{
				String read1Key = "r1_" + sample;
				String read2Key =  "r2_" + sample;
				
				BufferedWriter writer1 = getOrAddToCache(writerMap, read1Key);
				BufferedWriter writer2 = getOrAddToCache(writerMap, read2Key);
				
				writer1.write(">" + firstToken + "_1\n");
				writer1.write(leftSeq.getSequence() + "\n");
				writer2.write(">" + firstToken + "_2\n");
				writer2.write(rightSeq.getSequence() + "\n");
			}
			else
			{
				numSkipped++;
			}
			
			
			if( ++numDone % 10000 == 0 )
				System.out.println(numDone +  " "+ writerMap.size() + " " + numSkipped);
		}
		
		for(BufferedWriter writer : writerMap.values())
		{
			writer.flush();  writer.close();
		}
		
		leftReader.close();
		middleReader.close();
		rightReader.close();
		System.out.println(numDone +  " "+ writerMap.size());
	}
}
