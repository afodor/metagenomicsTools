package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CountSignificantHits
{
	private static final double THRESHOLD = 0.05;
	
	public static void main(String[] args) throws Exception
	{
		addSignifiant("caseControl", 19, 6);
		
	}
	
	private static void addSignifiant(String text, int sigColumn, int rSquaredColumn)
		throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY_PLUS_OTU.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY_PLUS_OTU[x];
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getTopeOneAtATimeDir() + File.separator + 
						"merged" + File.separator + "metapValuesFor_" + taxa + "_read1_.txt")));
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s = reader.readLine())
			{
				String[] splits = s.split("\t");
				
				Double pValue = Double.parseDouble(splits[sigColumn]);
				
				if( pValue <= THRESHOLD)
				{
					System.out.println( text + " " + taxa + " "+  splits[0] +  " " + pValue + " " + Double.parseDouble(splits[rSquaredColumn]) );
				}
			}
			
			reader.close();
		}
	}
}
