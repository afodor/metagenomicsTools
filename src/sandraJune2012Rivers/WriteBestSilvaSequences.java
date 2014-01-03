package sandraJune2012Rivers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import parsers.HitScores;
import utils.ConfigReader;

public class WriteBestSilvaSequences
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getSandraRiverJune2012Dir()  
				+File.separator + "silvaHitsToSandraStreamJune2012.fasta" 	)));
		
		List<HitScores> hits = HitScores.getTopHits(ConfigReader.getSandraRiverJune2012Dir()  
				+File.separator +"otuToSivla108ByBlastn.txt" );
		
		HashSet<String> targetIds = new HashSet<String>();
		
		for(HitScores hs : hits)
			targetIds.add(hs.getTargetId());
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(
				"c:\\Silva_108\\SSURef_108_NR_tax_silva_v2.fasta");
		
		long numRead=0;
		float numMatched =0;
		
		for( FastaSequence fs = fsoat.getNextSequence();
					fs != null; 
						fs = fsoat.getNextSequence())
		{
			numRead++;
			if( targetIds.contains( fs.getFirstTokenOfHeader()) )
			{
				numMatched++;
				
				writer.write(fs.getHeader() + "\n");
				writer.write(fs.getSequence() + "\n");
			}
			
			if(numRead % 10000 ==0 )
				System.out.println(numRead + " " + numMatched);
		}
		
		writer.flush(); writer.close();
	}
}
