package chapelHillWorkshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class PreprocessGeneFamilies
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
			"humann2_genefamilies.LABELS2.tsv")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
				"humann2_genefamilies-RemovedQuotes.tsv")));
		
		writer.write(reader.readLine() + "\n");
		
		int x=0;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			x++;
			String[] splits = s.split("\t");
			
			if( splits.length != 21)
				throw new Exception("unexpected tokens");
			
			String firstToken = splits[0].replaceAll("\"", "").
					replaceAll("\"", "").replaceAll(".", "")
					.replaceAll(":", "_").replaceAll(" ", "_").replaceAll("|", "_");
			
			
			writer.write(firstToken + "_" + x );
				
			for(int y=1; y < splits.length; y++)
				writer.write("\t" + splits[y]);
				
			writer.write("\n");
		}
		writer.flush(); writer.close();
		reader.close();
	}	
}
