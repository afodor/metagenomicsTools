package scripts.tanyaQuickCheck;

import java.io.File;

import parsers.OtuWrapper;

public class QuickMerge
{
	public static String[] LEVELS = {"phylum", "class", "order", "family", "species"};
	
	public static void main(String[] args) throws Exception
	{
		for(String level : LEVELS)
		{
			System.out.println(level);
			OtuWrapper wrapper = new OtuWrapper("C:\\tanyaQuickRep\\tanya_kraken_"+ level +  ".txt");
			
			wrapper.writeLoggedDataWithTaxaAsColumns( new File( "C:\\tanyaQuickRep\\tanya_kraken_"+ level +  "logNorm.txt"));
		}
	}
}
