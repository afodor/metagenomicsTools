package scripts.AliBariatricSurgery_July2022;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.OtuWrapper;

public class WriteToOTUTable
{
	public static final String[] LEVELS = { "phylum" , "class", "order", "family", "genus" };
	
	public static void main(String[] args) throws Exception
	{
		for( String level : LEVELS)
		{
			System.out.println(level);
			File inFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\" + level + "_rawCounts_MetaPhlAn2.tsv");
			File outFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\metaphlan_raw_" + level + ".txt");
			
			writeOTUFile(inFile, outFile,32);
			
			OtuWrapper wrapper= new OtuWrapper(outFile);
			
			for(String s : wrapper.getSampleNames())
				System.out.println(s + " " + wrapper.getCountsForSample(s));
			
			File logFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\metaphlan_logged_" + level + ".txt");
			
			wrapper.writeNormalizedLoggedDataToFile(logFile);
			
			inFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\" + level + "_rawCounts_Kraken2.tsv");
			outFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\kraken2_raw_" + level + ".txt");
			
			writeOTUFile(inFile, outFile,61);
			
			wrapper= new OtuWrapper(outFile);
			
			for(String s : wrapper.getSampleNames())
				System.out.println(s + " " + wrapper.getCountsForSample(s));
			
			logFile = new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\kraken2_logged_" + level + ".txt");
			
			wrapper.writeNormalizedLoggedDataToFile(logFile);
			
			
		}
		
	}
	
	private static final void writeOTUFile(File inFile, File outFile,int startNum) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0]);
		
		for( int x=startNum; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( ! splits[startNum].equals("NA"))
			{

				writer.write(splits[0]);
				
				for( int x=startNum; x < topSplits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
		
			}	
		}
		
		
		writer.flush();  writer.close();
		reader.close();
	}
}
