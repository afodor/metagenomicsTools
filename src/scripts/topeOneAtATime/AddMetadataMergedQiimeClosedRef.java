package scripts.topeOneAtATime;

import java.io.File;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataMergedQiimeClosedRef
{
	private static final String[] IN_QIIME = {"k", "p", "c", "o", "f", "g","s", "otu"};

	private static final String[] OUT_RDP= {"kingdom",
				"phylum", "class", "order", "family", "genus", "species", "otu"};

	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMergedKraken.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMergedKraken.getFileSet(4);
		
		for( int x=0; x < IN_QIIME.length; x++)
		{
			String taxa=  IN_QIIME[x];
			System.out.println(taxa);
			
			File unloggedFile = new File(ConfigReader.getTopeOneAtATimeDir() + 
				File.separator + "qiimeSummary" + File.separator + 
				"diverticulosis_closed_" + taxa + "_AsColumns.txt");
			
			File loggedFile = new File(ConfigReader.getTopeOneAtATimeDir() + 
							File.separator + "qiimeSummary" + File.separator + 
							"diverticulosis_closed_" + OUT_RDP[x] + "_AsColumnsLogNormal.txt");
			
			OtuWrapper wrapper = new OtuWrapper( unloggedFile);
			wrapper.writeNormalizedDataToFile(loggedFile);
			
			File outFile = new File( ConfigReader.getTopeOneAtATimeDir() + 
					File.separator + "qiimeSummary" + File.separator + 
					"diverticulosis_closed_" + OUT_RDP[x] + "_AsColumnsLogNormalWithMetadata.txt");
			
			AddMetadataMergedKraken.addMetadata(wrapper, loggedFile, outFile,false, fileSet3, fileSet4);
		}
		
	}
	
		
}
