package mbqc;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotWrapper
{
	public static void main(String[] args) throws Exception
	{
		String prefix = "jpetrosino";
		OtuWrapper.transpose(ConfigReader.getMbqcDir() + 
				File.separator + "dropbox" + File.separator +  "merged_otu_filtered_"+ prefix + ".txt", 
				ConfigReader.getMbqcDir() + 
				File.separator + "dropbox" + File.separator +  "merged_otu_filtered_"+ prefix + "TaxaAsColumns.txt", false);
	}
}
