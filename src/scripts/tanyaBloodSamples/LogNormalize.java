package scripts.tanyaBloodSamples;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class LogNormalize
{
	private final static String[] TAXA_LEVELS = 
		{"Phylum", "Class", "Order", "Family", "Genus", "Species", "OTU" };

	
	public static void main(String[] args) throws Exception
	{
		for(String s : TAXA_LEVELS)
		{
			File file = new File(ConfigReader.getTanyaBloodDir() + File.separator + 
							"Blood " + s +" META 18July16.txt");
			
			OtuWrapper wrapper = new OtuWrapper(file);
			
			wrapper.writeLoggedDataWithTaxaAsColumns(new File(
					ConfigReader.getTanyaBloodDir() + File.separator + 
					"Blood " + s +" META 18July16LogNorm.txt"));
		}
	}
}
