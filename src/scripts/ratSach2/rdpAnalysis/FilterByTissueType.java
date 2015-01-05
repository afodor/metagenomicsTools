package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class FilterByTissueType
{
	private static void writeLine(String s, BufferedWriter writer) throws Exception
	{
		String[] splits = s.split("\t");
		
		writer.write(splits[0]);
		
		for( int x=8; x < splits.length; x++)
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
						File.separator + "sparseThreeColumn_" + level +    "_AsColumnsLogNormalizedPlusMetadata.txt")));
			
			BufferedWriter cecumWriter=  new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
							File.separator + "sparseThreeColumn_" + level +    "_AsColumnsLogNormalized_" + 
							"Cecal Content" + ".txt")));
			
			BufferedWriter colonWriter=  new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
							File.separator + "sparseThreeColumn_" + level +    "_AsColumnsLogNormalized_" + 
							"Colon content" + ".txt")));
			
			String firstLine = reader.readLine();
			
			writeLine(firstLine, cecumWriter);
			writeLine(firstLine, colonWriter);
			
			
			for(String s= reader.readLine(); s != null; s =reader.readLine())
			{
				String[] splits = s.split("\t");
				
				if( splits[2].equals("Cecal Content"))
					writeLine(s, cecumWriter);
				else if ( splits[2].equals("Colon content"))
					writeLine(s, colonWriter);
			}
			
			cecumWriter.flush(); cecumWriter.close();
			colonWriter.flush(); colonWriter.close();
		}
	}
}
