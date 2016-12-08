package scripts.LyteNov2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.FastQ;
import utils.ConfigReader;

public class DeMultiplex
{
	
	public static void main(String[] args) throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLyteNov2016Dir() + File.separator + "Lyte_seqs_R1.fastq"	)));
		
		int numFound =0;
		
		HashMap<String, BufferedWriter> map = new HashMap<String, BufferedWriter>();
		
		for( FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; fastq = FastQ.readOneOrNull(reader))
		{
			String sampleID = fastq.getFirstTokenOfHeader().replace("@", "");
			
			sampleID = new StringTokenizer(sampleID, "_").nextToken();
			
			BufferedWriter writer = map.get(sampleID);
			
			if( writer == null)
			{
				writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getLyteNov2016Dir() + File.separator + 	
					"forwardReadsDemultiplexed" + File.separator + 
						sampleID + ".fasta"
						)));
				map.put(sampleID, writer);
			}
			
			writer.write(">" + fastq.getFirstTokenOfHeader().replace("@", "") + "\n");
			writer.write(fastq.getSequence() + "\n");
			
			numFound++;
			
			if( numFound % 10000 ==0)
				System.out.println(numFound + " " + map.size());
		}
		
		for( BufferedWriter w : map.values())
		{
			w.flush();  w.close();
		}
	}
}
