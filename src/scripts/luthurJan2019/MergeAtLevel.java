package scripts.luthurJan2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeAtLevel
{
	public static void main(String[] args) throws Exception
	{
		for(String level : SequenceToTaxaParser.TAXA_LEVELS )
			writeForALevel(level);
	}
	
	private static void writeForALevel(String s) throws Exception
	{
		HashMap<String, SequenceToTaxaParser> taxaMap = SequenceToTaxaParser.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir() + File.separator + 
				"data" + File.separator + "luther.sequence.table.nochim.SILVA.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
		{
			SequenceToTaxaParser stp= taxaMap.get(topSplits[x]);
			
			if( stp == null)
				throw new Exception("Could not find " + topSplits[x]);
			
			topSplits[x] = stp.getForALevel(s).replaceAll("\"", "");
		}
	}
}
