package ruralVsUrban;

import java.io.File;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class NormalizeRDPs
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getChinaDir() +
					File.separator + NewRDPParserFileLine.TAXA_ARRAY[x] +
					"_taxaAsColumns.txt");
			
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getChinaDir() +
					File.separator + NewRDPParserFileLine.TAXA_ARRAY[x] + 
					"_taxaAsColumnsLogNorm.txt");
		}
	}
}
