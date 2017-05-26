package scripts.lactoCheck;

import java.io.File;
import java.util.List;


import parsers.FastaSequence;
import utils.ConfigReader;
import utils.Translate;

public class CheckPrimer
{
	private static final String INERS_FORWARD_PRIMER = "AGTCTGCCTTGAAGATCGG";
	private static final String INERS_BACKWARDS_PRIMER = "AGTCTGCCTTGAAGATCGG";
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = 
				FastaSequence.readFastaFile(ConfigReader.getLactoCheckDir() + File.separator + 
							"arb-silva.de_2017-05-26_id435560_tax_silva.fasta");
		
		for(FastaSequence fs : list)
		{
			String seq =fs.getSequence().replaceAll("U", "T");
			
			if(seq.indexOf(INERS_FORWARD_PRIMER) == -1 )
				throw new Exception("No");
			
			if( seq.indexOf( INERS_BACKWARDS_PRIMER) == -1)
				throw new Exception("no");
		}
	}
}
