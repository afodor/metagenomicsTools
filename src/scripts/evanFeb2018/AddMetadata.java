package scripts.evanFeb2018;

import java.io.File;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			File inFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +".txt");
			
			File outFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +"WithMetadata.txt");
			
		}
	}
	
	private static void addMetadata(File inFile, File outFile) throws Exception
	{
		
	}
}
