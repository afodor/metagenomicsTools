package jobinLabRNASeq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class RewriteFastaSequence
{
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = 
			new FastaSequenceOneAtATime( 
				ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"SequenceFiles" + File.separator + "648276667.fna"
				);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"SequenceFiles" + File.separator + "648276667Renamed.fna")));
		
		for( FastaSequence fs = fsoat.getNextSequence();
				fs != null;
					fs = fsoat.getNextSequence())
		{
			StringTokenizer sToken = new StringTokenizer(fs.getHeader());
			
			writer.write(sToken.nextToken().replaceAll("NZ_", "") + ".1");
			
			while(sToken.hasMoreTokens())
				writer.write( " " + sToken.nextToken());
			
			writer.write("\n");
			
			writer.write(fs.getSequence() + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
