package coPhylog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.FastQ;
import utils.ConfigReader;

/*
 * A brute force, naive initial attempt to implement
 * this algorithm:
 * 
 * http://www.ncbi.nlm.nih.gov/pubmed/23335788
 * 
 * "Co-phylog: an assembly-free phylogenomic approach for closely related organisms."
 */
public class MakeContextMap
{
	
	
	public static HashMap<String, ContextCount> 
		getContextMap( File fastQFile, int leftHashLength, 
						int rightHashLength) throws Exception
	{
		HashMap<String, ContextCount> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fastQFile));
		
		
		for(  FastQ fastq = FastQ.readOneOrNull(reader); 
					fastq != null; 
						fastq = FastQ.readOneOrNull(reader))
		{
			String seq = fastq.getQualScore();
			
			// todo: test to make sure we are getting to the end of the sequence
			// todo: add the reverse complement
			int stopPoint = seq.length() - (leftHashLength + rightHashLength + 1);
			
			for( int x=0; x < stopPoint; x++ )
			{
				String leftHash = seq.substring(x, x + leftHashLength);
				
				char insert = seq.charAt(x + leftHashLength);
				
				String rightHash = seq.substring(x + leftHashLength + 1, 
						x + leftHashLength + 1 + rightHashLength);
				
				if( rightHash.length() !=  rightHashLength)
					throw new Exception("NO");
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		File file = new File(ConfigReader.getBurkholderiaDir() + File.separator + 
				"AS130-2_ATCACG_s_2_1_sequence.txt");
		
		HashMap<String, ContextCount> 
			map = getContextMap(file, 15, 15);
	}
}
