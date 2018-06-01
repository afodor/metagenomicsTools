package scripts.laura.sleeveGastroProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class StripQuotes
{
	public static void main(String[] args) throws Exception
	{
		String[] taxa = { "phyla","genus" };
		
		for(String s : taxa)
		{
			File inFile = new File(ConfigReader.getLauraDir() + 
				File.separator + "SleeveGastroProject" + File.separator +s+ 
				"withMDS.txt.txt");
			
			File outFile = new File(ConfigReader.getLauraDir() + 
				File.separator + "SleeveGastroProject" + File.separator +s+ 
				"withMDSNoQuotes.txt");
			
			stripQuotes(inFile, outFile);
		}
	}
	
	private static void stripQuotes(File inFile, File outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			writer.write(s.replaceAll("\"", "").replaceAll("No Value", "NA") + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
