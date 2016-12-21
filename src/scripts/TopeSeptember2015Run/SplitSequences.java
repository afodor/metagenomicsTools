package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class SplitSequences
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = SampleMap.getPrimersToSampleMap();
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
		
		BufferedReader read1 = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( ConfigReader.getTopeSep2015Dir() + 
						File.separator + 
						"Humphries_R1_001.fastq.gz"))));
		
		BufferedReader tag2 = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream(ConfigReader.getTopeSep2015Dir() + 
						File.separator + 
						"Humphries_R2_001.fastq.gz"))));
		
		BufferedReader tag3 = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream(ConfigReader.getTopeSep2015Dir() + 
						File.separator + 
						"Humphries_R3_001.fastq.gz"))));
		
		BufferedReader read4 = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream(ConfigReader.getTopeSep2015Dir() + 
						File.separator + 
						"Humphries_R4_001.fastq.gz"))));
		
		long numRead =0;
		long numAssigned =0;
		for( FastQ read1FastQ = FastQ.readOneOrNull(read1); read1FastQ != null;
				read1FastQ = FastQ.readOneOrNull(read1))
		{
			numRead++;
			FastQ tag2FastQ = FastQ.readOneOrNull(tag2);
			FastQ tag3FastQ = FastQ.readOneOrNull(tag3);
			FastQ read4FastQ = FastQ.readOneOrNull(read4);
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(tag2FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(tag3FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(read4FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			String key = tag2FastQ.getSequence() + "@" + tag3FastQ.getSequence();
			
			if( map.containsKey(key))
			{
				numAssigned++;
				
				BufferedWriter writer1 = getOrCreate(map.get(key) + "_1", writerMap);
				BufferedWriter writer4 = getOrCreate(map.get(key) + "_4", writerMap);
				
				writer1.write(">" + read1FastQ.getFirstTokenOfHeader() + "\n");
				writer1.write(read1FastQ.getSequence() + "\n");
				writer1.flush();

				writer4.write(">" + read4FastQ.getFirstTokenOfHeader() + "\n");
				writer4.write(read4FastQ.getSequence() + "\n");
				writer4.flush();
			}
			
			if( numRead % 10000 == 0 )
				System.out.println(numRead + " " + numAssigned + " " + ((double)numAssigned/ numRead));
		}
		
		for( String s : writerMap.keySet())
		{
			BufferedWriter writer = writerMap.get(s);
			writer.flush();  writer.close();
		}
	
		if( FastQ.readOneOrNull(tag2) != null)
			throw new Exception("No");
		

		if( FastQ.readOneOrNull(tag3) != null)
			throw new Exception("No");
		
		if( FastQ.readOneOrNull(read4) != null)
			throw new Exception("No");
		
		System.out.println("finshed");
		System.out.println(numRead + " " + numAssigned + " " + ((double)numAssigned/ numRead));
		
	}
	
	private static BufferedWriter getOrCreate( String name, HashMap<String, BufferedWriter> map )
		throws Exception
	{
		BufferedWriter writer = map.get(name);
		
		if( writer == null)
		{
			writer = new BufferedWriter(
					new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(
						ConfigReader.getTopeSep2015Dir() + File.separator + 
						"splitSamples" + File.separator + name+ ".fasta.gz"))));
			
			map.put(name, writer);
		
		}
		
		return writer;
	}
}
