package scripts.laura.sleeveGastroProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class WriteMergedMeta
{
	public static void main(String[] args) throws Exception
	{
		
		String[] prefix = { "phyla","genus" };
		
		for( String p : prefix)
		{
			writeCagesToMeta(p);
		}
	}
	
	private static void writeCagesToMeta(String prefix) throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLauraDir()+
				File.separator + "SleeveGastroProject" + File.separator +prefix + "_tax_data.txt")));
		
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(ConfigReader.getLauraDir() + 
				File.separator + "SleeveGastroProject" + File.separator +prefix + "_tax_dataWithCages.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0] + "\tcage");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[4];
			
			MetadataParserFileLine mpfl = metaMap.get(key);
			System.out.println(key);
			
			String cage = "NA";
			
			if( mpfl != null)
				cage ="" +  mpfl.getCageNumber();
			
			writer.write(key + "\t" + cage );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
