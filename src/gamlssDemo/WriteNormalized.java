package gamlssDemo;

import java.io.File;

import parsers.OtuWrapper;

public class WriteNormalized
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper(
						"C:\\Users\\corei7\\git\\metagenomicsTools\\src\\gamlssDemo\\genusPivotedTaxaAsColumns.txt");
		
		wrapper.writeNormalizedDataToFile(
				new File(
		"C:\\Users\\corei7\\git\\metagenomicsTools\\src\\gamlssDemo\\genusPivotedTaxaAsColumnsNorm.txt"));
	}
}
