package scripts.farnaz.dada2;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class LogNormalize
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getFarnazDada2Directory() + File.separator + 
					"PivotedForwardReads.txt");
		
		File logNormFile = new File(ConfigReader.getFarnazDada2Directory() + File.separator + 
				"PivotedForwardReadsLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
		
		System.out.println("Done");
	}
}
