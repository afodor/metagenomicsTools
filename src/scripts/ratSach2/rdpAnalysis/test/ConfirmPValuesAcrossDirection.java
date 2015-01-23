package scripts.ratSach2.rdpAnalysis.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class ConfirmPValuesAcrossDirection
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
							File.separator +
					"pValuesAcrossMixed_taxa_" +  level + ".txt")));
			
			reader.readLine();
			
			
		}
	}
	
	private static double getAPValue(String taxa, String level, String tissue) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(	
				ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
							File.separator + "pValuesForTime_taxa_" +  tissue + "_" + level +".txt")));
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[0].equals(taxa))
			{
				double returnVal = Math.log10(Double.parseDouble(splits[4]));
				
				if( Double.parseDouble(splits[4]) > Double.parseDouble(splits[3]))
					returnVal = -returnVal;
				
				return returnVal;
			}
		}
		
		reader.close();
		
		throw new Exception("Could not find " + taxa + " "+ level + " " + tissue);
	}
	
	
}
