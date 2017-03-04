package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class getColumnNumber
{
	public static void main(String[] args) throws Exception
	{
		//String toFind = "206494";
		String toFind = "174320";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
				"rg_results" + File.separator + "LyteSharon_r01_crDataOnlyTaxaAsColumnsLogNormPlusMetadata.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=0; x < topSplits.length; x++)
			if( topSplits[x].equals(toFind))
				System.out.println(x+1);
		
		reader.close();
		
	}
}
