package sandraJune2012Rivers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.AbundOTUClustParser;
import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

import utils.ConfigReader;

public class WriteOTUFile
{
	public static void main(String[] args) throws Exception
	{
		writeOTUs();
		
		for( int x=1; x <= 5; x++)
			writeOTUs( NewRDPParserFileLine.TAXA_ARRAY[x]);
	}
	
	private static void writeOTUs(String level) throws Exception
	{
		HashMap<String, NewRDPParserFileLine> rdpMap = 
				NewRDPParserFileLine.getAsMapFromSingleThread(ConfigReader.getSandraRiverJune2012Dir() + File.separator + 
						"sandraOTUtoRDP_byRDP2_4.txt"
						);
		
		BufferedWriter writer = new BufferedWriter( new FileWriter(
				ConfigReader.getSandraRiverJune2012Dir()  
				+ File.separator +"sandra_" + level + "_ToSampleFilePrimerTrimmed.txt"));
		
		writer.write("sequence\tsample\totu\n");
		
		HashMap<String, String> map= AbundOTUClustParser.getSequenceToOtuMap(
				ConfigReader.getSandraRiverJune2012Dir()  
				+ File.separator +  "abundantOTUSandraTrimmed.clust");
		
		for(String s : map.keySet())
		{
			StringTokenizer sToken = new StringTokenizer(s, "_");
			NewRDPNode node = rdpMap.get("Consensus" + map.get(s)).getTaxaMap().get(level);
			
			if( node != null && node.getScore() >=80 )
			{
				writer.write(sToken.nextToken() + "\t");
				writer.write(sToken.nextToken() + "\t");
				writer.write( node.getTaxaName() + "\n");
			}
		}
		
		
		writer.flush();  writer.close();
	}
	
	private static void writeOTUs() throws Exception
	{
		BufferedWriter writer = new BufferedWriter( new FileWriter(
				ConfigReader.getSandraRiverJune2012Dir()  
				+ File.separator +"sandra_Otu_ToSampleFilePrimerTrimmed.txt"));
		
		writer.write("sequence\tsample\totu\n");
		
		HashMap<String, String> map= AbundOTUClustParser.getSequenceToOtuMap(
				ConfigReader.getSandraRiverJune2012Dir()  
				+ File.separator +  "abundantOTUSandraTrimmed.clust");
		
		for(String s : map.keySet())
		{
			StringTokenizer sToken = new StringTokenizer(s, "_");
			writer.write(sToken.nextToken() + "\t");
			writer.write(sToken.nextToken() + "\t");
			writer.write(map.get(s) + "\n");
		}
		
		
		writer.flush();  writer.close();
	}
}
