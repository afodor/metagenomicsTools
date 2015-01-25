package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteTaxaRemovingStoolSamplesInSwab
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			writeALevel(level);
		}
	}
	
	private static void writeALevel(String level) throws Exception
	{
		String filepath = ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + File.separator + 
					"mergedKrakenRDP_" + level + "_WithMetadata.txt";
		
		HashSet<String> idsToExclude = idsToExcludeForStool(filepath);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				filepath
				)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + File.separator + 
				"mergedKrakenRDP_" + level + "_WithMetadataDupesRemovedFromStool.txt"
				));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			if( splits[2].equals("swab") || ! idsToExclude.contains(splits[3]))
				writer.write(s + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	
	private static HashSet<String> idsToExcludeForStool(String filepath) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		HashSet<String> inStool = new HashSet<String>();
		HashSet<String> inSwab = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				filepath
				)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[2].equals("stool") )
				inStool.add(splits[3]);
			else if ( splits[2].equals("swab"))
				inSwab.add(splits[3]);
			else throw new Exception("No");
		}
		
		reader.close();
		
		for(String s : inStool)
			if( inSwab.contains(s))
				set.add(s);
		
		return set;
	}
}
