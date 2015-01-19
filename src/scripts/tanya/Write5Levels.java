package scripts.tanya;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class Write5Levels
{
	public static void main(String[] args) throws Exception
	{
		// this file was edited to only the relevant data here..
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTanyaDir() + File.separator + 
				"SOLAR2AF011715Corrected.txt")));
		
		String firstLine = reader.readLine();
		String[] firstLineSplits = firstLine.split("\t");
		
		HashMap<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			writers.put(NewRDPParserFileLine.TAXA_ARRAY[x], 
					startAWriter(NewRDPParserFileLine.TAXA_ARRAY[x], firstLine));
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for( BufferedWriter writer : writers.values() )
				writer.write("" + splits[0] +"_" + splits[1]);
				
			for( int x=2; x < splits.length; x++)
			{
				String level = getTaxonomy(firstLineSplits[x]);
				BufferedWriter writer =  writers.get(level);
				writer.write("\t" + splits[x]);
			}
			

			for( BufferedWriter writer : writers.values() )
				writer.write("\n");
		}
		

		for( BufferedWriter writer : writers.values() )
		{
			writer.flush();  writer.close();
		}
			
	}
	
	private static BufferedWriter startAWriter(String level, String firstLine) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTanyaDir() + File.separator + level + "AsColumns.txt")));
		
		String[] splits = firstLine.split("\t");
		
		writer.write("" + splits[0] +"_" + splits[1]);
		
		for( int x=2; x < splits.length; x++)
			if(getTaxonomy(splits[x]).equals(level))
				writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		return writer;
	}
	
	private static String getTaxonomy( String key )
		throws Exception
	{
		key = key.toLowerCase();
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			if( key.indexOf(level) != -1 )
				return level;
		}
		
		if( key.endsWith("cla"))
			return NewRDPParserFileLine.CLASS;
		
		if( key.endsWith("ord"))
			return NewRDPParserFileLine.ORDER;
		
		if( key.endsWith("fami") || key.endsWith("fam") || key.endsWith("fa") ||
				key.equals("clostridiales_incertae_sedis_xif"))
			return NewRDPParserFileLine.FAMILY;
			
		throw new Exception("Could not find " + key);
	}
}
