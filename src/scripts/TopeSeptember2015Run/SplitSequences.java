package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class SplitSequences
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = SampleMap.getPrimersToSampleMap();
		
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
		
		BufferedReader read2 = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream(ConfigReader.getTopeSep2015Dir() + 
						File.separator + 
						"Humphries_R1_001.fastq.gz"))));
		
		long numRead =0;
		long numAssigned =0;
		for( FastQ read1FastQ = FastQ.readOneOrNull(read1); read1FastQ != null;
				read1FastQ = FastQ.readOneOrNull(read1))
		{
			numRead++;
			FastQ tag2FastQ = FastQ.readOneOrNull(tag2);
			FastQ tag3FastQ = FastQ.readOneOrNull(tag3);
			FastQ read2FastQ = FastQ.readOneOrNull(read2);
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(tag2FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(tag3FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			if( ! read1FastQ.getFirstTokenOfHeader().equals(read2FastQ.getFirstTokenOfHeader() ))
					throw new Exception("No");
			
			String key = tag2FastQ.getSequence() + "@" + tag3FastQ.getSequence();
			
			if( map.containsKey(key))
			{
				numAssigned++;
			}
			
			if( numRead % 1000 == 0 )
				System.out.println(numRead + " " + numAssigned + " " + ((double)numAssigned/ numRead));
		}
	
		if( FastQ.readOneOrNull(tag2) != null)
			throw new Exception("No");
		

		if( FastQ.readOneOrNull(tag3) != null)
			throw new Exception("No");
		

		if( FastQ.readOneOrNull(read2) != null)
			throw new Exception("No");
	}
}
