package scripts.topeOneAtATime;

import java.io.File;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataForCombinedReadRDP
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMergedKraken.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMergedKraken.getFileSet(4);
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(taxa);
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "bothReadsCombined" + File.separator + 
					"spreadsheets" + File.separator +
					taxa + "_taxaAsColumns.txt");
			
			File logNormalizedFile = 
				new File( ConfigReader.getTopeOneAtATimeDir()
						+ File.separator + "bothReadsCombined" + File.separator + 
						"spreadsheets" + File.separator +
						"pivoted_" + taxa + "asColumnsLogNormal.txt" );
								
			File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "bothReadsCombined" + File.separator + 
					"spreadsheets" + File.separator +
					taxa + "_taxaAsColumnsLogNormalPlusMetadata.txt" );
			
			AddMetadataSwarm.addMetadata(wrapper, logNormalizedFile, outFile,false, fileSet3, fileSet4);
		}	
	}
	
	
	
	
}
