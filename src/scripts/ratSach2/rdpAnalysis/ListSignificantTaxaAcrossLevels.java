package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;

import utils.ConfigReader;

public class ListSignificantTaxaAcrossLevels
{
	public static void main(String[] args) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		
		nf.setMinimumFractionDigits(3);
		
		String[] tissues = { "Cecal Content", "Colon content" };
		
		for(String tissue: tissues)
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pValueTaxaSummary" + tissue +  ".txt")));
			
			writer.write("taxa\tfdrPValue\tupIn\n");
			
			String[] levels = { "phylum","class","order","family","genus", "otu" };
				for( String level: levels)
			{
				System.out.println(level);
				writer.write("\n" + level + "\n");
				
				BufferedReader reader = new BufferedReader(new FileReader(new File(
						ConfigReader.getRachSachReanalysisDir()
						+ File.separator + "rdpAnalysis" 
						+ File.separator + "pValuesForTime_taxa_"+ tissue +  "_" + level + ".txt")));
				
				reader.readLine();
				
				for(String s = reader.readLine() ; s != null; s= reader.readLine())
				{
					String[] splits = s.split("\t");
					if( splits.length != 5)
						throw new Exception("Parsing error");
					
					String key = splits[0].replaceAll("\"","");
					
					if( Double.parseDouble(splits[4]) < 0.10 )
					{
						String higher = "high sac";
						
						if( Double.parseDouble(splits[3] ) > Double.parseDouble(splits[2]))
							higher = "low sac";
						
						writer.write( key + "\t" 
								+ nf.format(Double.parseDouble(splits[4]))  + "\t" +  higher + "\n");
						writer.flush();
					}
				}
			}
				
			writer.flush();  writer.close();
		}
	}
}
