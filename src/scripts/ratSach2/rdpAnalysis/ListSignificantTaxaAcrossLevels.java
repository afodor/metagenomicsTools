package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class ListSignificantTaxaAcrossLevels
{
	public static void main(String[] args) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		
		nf.setMinimumFractionDigits(2);
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pValuesForTime_taxa_Cecal Content_" + level + ".txt")));
			
			reader.readLine();
			
			for(String s = reader.readLine() ; s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				if( splits.length != 5)
					throw new Exception("Parsing error");
				
				String key = splits[0].replaceAll("\"","");
				
				if( Double.parseDouble(splits[4]) < 0.10 )
				{
					String higher = "higher in high sac";
					
					if( Double.parseDouble(splits[3] ) > Double.parseDouble(splits[2]))
						higher = "higher in low sac";
					
					System.out.println( key + " " 
							+ nf.format(Double.parseDouble(splits[4]))  + " " +  higher );
				}
			}
		}
	}
}
