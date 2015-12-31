package scripts.TopeDiverticulosisDec2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeDiverticulosisDec2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getTopeDiverticulosisDec2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File outFile = new File( ConfigReader.getTopeDiverticulosisDec2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile);
		}
		
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tnumberSequencesPerSample\tshannonEntropy");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			writer.write(key+ "\t" + wrapper.getNumberSequences(splits[0]) 
						+ "\t" + wrapper.getShannonEntropy(splits[0]) );
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
