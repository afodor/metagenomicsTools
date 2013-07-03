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
			System.out.println(fastq.getSequence());
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
