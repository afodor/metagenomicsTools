package scripts.topeOneAtATime;

import java.io.File;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class Merge
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			File file3 = new File(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File file4 = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file4" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File outFile = new File(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "merged" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			OtuWrapper.merge(file3, file4, outFile);
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "merged" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
		}
	}
}
