package scripts.topeOneAtATime;

import java.io.File;

import parsers.OtuWrapper;
import scripts.TopeSeptember2015Run.AddMetadataForKraken;
import utils.ConfigReader;

public class MergeKraken
{
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < AddMetadataForKraken.TAXA_ARRAY.length; x++)
		{
			String taxa = AddMetadataForKraken.TAXA_ARRAY[x];
			System.out.println(taxa);
			
			File file3 = new File(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator +"krakenSummary" + 
					File.separator + "diverticulosis_file3_kraken_" + taxa + ".txt");
			
			File file4 = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator +"krakenSummary" + 
					File.separator + "diverticulosis_file4_kraken_" + taxa + ".txt");
			
			File outFile = new File(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "krakenMerged" + 
					File.separator + "pivoted_" + 
					"diverticulosis_merged_kraken_" + taxa + ".txt");
			
			OtuWrapper.merge(file3, file4, outFile);
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "krakenMerged" + 
					File.separator + "pivoted_" + 
					"diverticulosis_merged_kraken_" + taxa + "LogNormal.txt");
		}
	}
}
