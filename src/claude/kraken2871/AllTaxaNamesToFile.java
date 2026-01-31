package claude.kraken2871;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;

public class AllTaxaNamesToFile
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper(
			"C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\paired_counts_table.tsv");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\sampleNames.txt")));
		
		writer.write("Full name\n");
		
		for(String s : wrapper.getOtuNames())
			writer.write(s + "\n");
		
		writer.flush();  writer.close();
	}
}
