package mbqc.rdp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteFirstColumns
{
	private static final int NUM_COLUMNS=25;
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x] );
			BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + 
					File.separator + "rdpAnalysis" + File.separator + 
						"bigSpreadsheet" + File.separator + 
					"pcoa_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +".txt"));
						
			BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getMbqcDir() + 
					File.separator +  "rdpAnalysis" +  File.separator 
					+ "pcoa_" +  NUM_COLUMNS + "_" + NewRDPParserFileLine.TAXA_ARRAY[x] +".txt"
						));
			
			writer.write("sample");
			String[] splits = reader.readLine().split("\t");
			
			for( int y=0; y < NUM_COLUMNS; y++)
			{
				writer.write("\t" + splits[y].replaceAll("\"", ""));
			}
			
			writer.write("\n");
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				splits = s.split("\t");
				
				for( int y=0; y <= NUM_COLUMNS; y++)
					writer.write(splits[y].replaceAll("\"", "") + ( y==NUM_COLUMNS ? "\n" : "\t" ));
				
				writer.write("\n");
			}
			
			reader.close();
		}
	}
}
