package scripts.TanyaFeb2018;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class TransposeAndAddMetadata
{
	public static void main(String[] args) throws Exception
	{
		String[] TAXA_NAMES = {"Phylum", "Class", "Order", "Family", "Genus"};
		
		for(String s : TAXA_NAMES)
		{
			String inFile = ConfigReader.getTanyaFeb2018Directory() + File.separator + 
				"deblur-seqs-tax-blood-rarefied-400-" + s + ".from_biom.txt";
			
			String outFile = ConfigReader.getTanyaFeb2018Directory() + File.separator + 
					"deblur-seqs-tax-blood-rarefied-400-" + s + ".from_biom_taxaAsColumns.txt";
			
			OtuWrapper.transpose(inFile, outFile, false);
			
		}
	}
}
