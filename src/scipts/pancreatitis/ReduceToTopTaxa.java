package scipts.pancreatitis;

import java.io.File;
import java.util.HashMap;

import parsers.OtuWrapper;

import utils.ConfigReader;

public class ReduceToTopTaxa
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(
			ConfigReader.getPancreatitisDir() + File.separator + "erinHannaHuman_raw_phyForR.txt");
		
		HashMap<String, Double> taxaMap = 
				wrapper.getTaxaListSortedByNumberOfCounts();
		
		for(String s : taxaMap.keySet())
			System.out.println(s + " " + taxaMap.get(s));
		
		int numToInclude = 6;
		
		File outFile = new File(ConfigReader.getPancreatitisDir() + File.separator + 
				"phylaTop6TaxaAsColumns.txt");
		
		wrapper.writeReducedOtuSpreadsheetsWithTaxaAsColumns( outFile
				, numToInclude);
		
		OtuWrapper wrapper2 = new OtuWrapper(outFile);
		wrapper2.writeunLoggedDataWithSamplesAsColumns(new File( 
				ConfigReader.getPancreatitisDir() + File.separator + 
				"phylaTop6SamplesAsColumns.txt"));
		
	}
}
