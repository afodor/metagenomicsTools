package scripts.topeOneAtATime;

import java.io.File;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataAbundantOTU
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMergedKraken.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMergedKraken.getFileSet(4);
		
		OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "abundantOTU" + File.separator + 
						"abundantOTUsAsColumns.txt");
			
		File logNormalizedFile = 
				new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "abundantOTU" + File.separator + 
						"abundantOTUsAsColumnsLogNorm.txt");
								
		wrapper.writeNormalizedLoggedDataToFile(logNormalizedFile);
		
		File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
				+ File.separator + "abundantOTU" + File.separator + 
				"abundantOTUsAsColumnsLogNormPlusMetadata.txt");
			
		AddMetadataSwarm.addMetadata(wrapper, logNormalizedFile, outFile,false, fileSet3, fileSet4);
			
	}
	
}
