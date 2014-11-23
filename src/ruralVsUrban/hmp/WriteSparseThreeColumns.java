package ruralVsUrban.hmp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteSparseThreeColumns
{
	private static int THRESHOLD_FOR_HMP= 50;
	
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
		
		File rdpResultsDir = new File(ConfigReader.getChinaDir() + File.separator + 
				"hmpRdpResults");
		
		int x=0;
		for(String s : rdpResultsDir.list())
		{
			System.out.println(s + " " + x++ );
			File rdpFile = new File(rdpResultsDir.getAbsolutePath() + File.separator + s);
			List<NewRDPParserFileLine> rpdList = NewRDPParserFileLine.getRdpListSingleThread(rdpFile);
			addToWriters(rpdList, threeColWriters, s);
		}
		
		for(BufferedWriter writer : threeColWriters.values())
		{
			writer.flush(); writer.close();
		}
	}
	
	// sample taxa count
	private static void addToWriters(List<NewRDPParserFileLine> rpdList, 
			HashMap<String, BufferedWriter> threeColWriters, String sampleName
					) throws Exception
	{
		for(String level : threeColWriters.keySet())
		{
			HashMap<String, Integer> countMap = 
					NewRDPParserFileLine.getCountsForLevel(rpdList, level, THRESHOLD_FOR_HMP);
			
			BufferedWriter writer = threeColWriters.get(level);
			
			for(String taxa : countMap.keySet())
			{
				writer.write(sampleName + "\t" + taxa + "\t" + countMap.get(taxa) + "\n");
			}
			
			writer.flush();
		}
	}
}
