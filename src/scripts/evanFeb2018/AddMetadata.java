package scripts.evanFeb2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			File inFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +".txt");
			
			File outFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +"WithMetadata.txt");
			
			addMetadata(inFile, outFile);
			
		}
	}
	
	private static void addMetadata(File inFile, File outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer=  new BufferedWriter(new FileWriter(outFile));
		
		writer.write("fileID\tsampleID\tbatch\t" + reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s !=null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			StringTokenizer sToken = new StringTokenizer(splits[0],"_");
			writer.write(splits[0] + "\t" 
					+ sToken.nextToken() + "\t" + sToken.nextToken().charAt(0) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
