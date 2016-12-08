package scripts.LyteNov2016.mattFiles;

import java.io.File;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class LogNormalize
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(taxa);
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "mattFiles" + File.separator + 
						taxa + "File.txt");
			
			File loggedFile = new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "mattFiles" + File.separator + 
						taxa + "logNorm.txt");
			
			wrapper.writeNormalizedLoggedDataToFile(loggedFile);
		}
	}
}
