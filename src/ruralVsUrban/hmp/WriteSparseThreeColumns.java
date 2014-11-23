package ruralVsUrban.hmp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteSparseThreeColumns
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, BufferedWriter> threeColWriters= 
				new HashMap<String, BufferedWriter>();
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			BufferedWriter writer = new BufferedWriter(new FileWriter(
				ConfigReader.getChinaDir() + File.separator + "hmpSpreadsheets" 
					+ File.separator + "threeCol_" + level ));
			
			threeColWriters.put(level, writer);
		}
		
		
		for(BufferedWriter writer : threeColWriters.values())
		{
			writer.flush(); writer.close();
		}
	}
}
