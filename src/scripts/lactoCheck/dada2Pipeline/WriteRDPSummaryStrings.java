package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteRDPSummaryStrings
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getLactoCheckDir() + File.separator + 
				"allTogetherFastaToRDP2_12SummaryString.txt")));
		
		HashMap<String, NewRDPParserFileLine> fileLines = NewRDPParserFileLine.getAsMapFromSingleThread(
				ConfigReader.getLactoCheckDir() + File.separator + 
							"allTogetherFastaToRDP2_12.txt");
		
		writer.write("id\trdpSummaryString\n");
		
		for( String s : fileLines.keySet())
			writer.write(s + "\t" + fileLines.get(s).getSummaryString() + "\n");
		
		writer.flush();  writer.close();
	}
}
