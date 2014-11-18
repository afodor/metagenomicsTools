
package ruralVsUrban;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;

public class FastQToFastA
{
	public static void main(String[] args) throws Exception
	{
		if( args.length != 2)
			throw new Exception("Usage fastq fasta");
		
		File inFile = new File(args[0]);
		
		if( ! inFile.exists())
			throw new Exception("fastq file does not exist");
		
		File outFile = new File(args[1]);
		
		if( outFile.exists())
			throw new Exception("fasta file already exists");
		
		BufferedReader reader =
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( inFile))));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		HashSet<String> ids = new HashSet<String>();
		for(FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; 
				fastq = FastQ.readOneOrNull(reader))
		{
			StringTokenizer sToken = new StringTokenizer(fastq.getHeader());
			String id = sToken.nextToken();
			if( ids.contains(id))
				throw new Exception("Duplicate " + id);
			ids.add(id);
			writer.write(">" + id + "\n");
			writer.write(fastq.getSequence() + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
