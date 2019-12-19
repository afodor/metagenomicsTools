package scripts.Pierce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		
		File pivotFileLogNorm = new File(ConfigReader.getPierce2019Dir() + File.separator + 
				"taxaAsColumns_OTULogNorm.txt");
		
		File pivotFileMetadata =
				new File(ConfigReader.getPierce2019Dir() + File.separator + 
						"taxaAsColumns_OTULogNormPlusMeta.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(pivotFileLogNorm));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pivotFileMetadata));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0]);
		
		String[] headerSplits = MetadataParser.getTopMetaLine().split("\t");
		
		for( int x=1; x < headerSplits.length; x++)
			writer.write("\t" + headerSplits[x]);
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			System.out.println(s);
			String[] splits= s.split("\t");
			
			writer.write(splits[0]);
			
			if( splits.length != topSplits.length)
				throw new Exception();
			
			MetadataParser mp = metaMap.get(splits[0]);
			
			if( mp == null)
			{	
				for( int x=1; x < headerSplits.length; x++)
					writer.write("\tNA");
			}
			else
			{
				String[] moreSplits = mp.getFileLine().split("\t");
				
				if( moreSplits.length != headerSplits.length)
					throw new Exception();

				for( int x=1; x < moreSplits.length; x++)
					writer.write("\t" + moreSplits[x]);
			}
				
			
			for( int x=1 ; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		
		reader.close();
				
	}
}
