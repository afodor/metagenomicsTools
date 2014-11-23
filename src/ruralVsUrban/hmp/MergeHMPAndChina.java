package ruralVsUrban.hmp;

import java.io.File;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeHMPAndChina
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];

			System.out.println(level);
			
			File file1= new File(
			ConfigReader.getChinaDir() 
			+ File.separator + "hmpSpreadsheets" 
			+ File.separator + level + "_AsColumns.txt");
			
			File file2= new File(
					ConfigReader.getChinaDir() +
					File.separator + NewRDPParserFileLine.TAXA_ARRAY[x] +
					"_taxaAsColumns.txt"
					);
			
			File mergedOut = new File(ConfigReader.getChinaDir() 
					+ File.separator + "hmpSpreadsheets" 
					+ File.separator + level + "_AsColumnsMergedChinaHMP.txt");
			
			OtuWrapper.merge(file1, file2, mergedOut);
			
			OtuWrapper mergedWrapper = new OtuWrapper(mergedOut);
			mergedWrapper.writeNormalizedLoggedDataToFile(
					ConfigReader.getChinaDir() 
					+ File.separator + "hmpSpreadsheets" 
					+ File.separator + level + "_AsColumnsMergedChinaHMPLogNormal.txt"
					);
		}
	}
}
