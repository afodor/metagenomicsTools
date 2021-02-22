package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;

public class WriteTaxaPlusMeta
{
	public static void main(String[] args) throws Exception
	{
		String[] levels = { "Phylum", "Class", "Order", "Family", "Genus" };
		
		for( String level : levels)
		{	
			writeTaxaTablesForLevel(level);
			addMetadata(level);
		}
	}
	
	private static void addMetadata(String level ) throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		
		File logNormalFile = getLogNormalFile(level);
		
		BufferedReader reader =new BufferedReader(new FileReader(logNormalFile));
		
		File metaFile = 
				new File(
						"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\AF_OUT\\taxa_" + 
								level + "_beefSup2017_logNormPlusMeta.txt"	);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
		
		writer.write("sampleID\textentOfStress\tdateOfExperiment\texperiment\tsex\tcageID\tdiet");
		
		String [] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine();s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			MetadataParser mp = metaMap.get(splits[0]);
			
			writer.write(splits[0] + "\t" + mp.getExtentOfStress() + "\t" + mp.getDateOfExperiment() + "\t" + 
							mp.getExperiment() + "\t" + mp.getSex() + "\t" + mp.getCageID() + "\t" + 
									mp.getDiet() );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static File getBaseTaxaFile(String level) throws Exception
	{
		return new File(
				"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\AF_OUT\\taxa_" + 
						level + "_beefSup2017.txt"	);
		
	}
	
	private static File getLogNormalFile(String level) throws Exception
	{
		return new File(
				"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\AF_OUT\\taxa_" + 
						level + "_beefSup2017_logNorm.txt"	);
	}
	
	public static void writeTaxaTablesForLevel(String level)  throws Exception
	{
		System.out.println(level);
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\input\\" + 
							level+  "_table.txt"));
		
		File outFile = getBaseTaxaFile(level);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			
		String[] topSplits = reader.readLine().split("\t");
			
		List<String> taxaNames = new ArrayList<String>();
			
		boolean foundId = false;
		int index=0;
			
		while( ! foundId)
		{
			taxaNames.add(topSplits[index]);
				
			index++;
				
			if( topSplits[index].equals("Argonne Sequence Number"))
				foundId =true;
		}
		
		writer.write("sampleID");
		
		for( String s : taxaNames)
			writer.write("\t" + s );
		
		writer.write("\n");
			
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			MetadataParser mp = metaMap.get(key);
			
			if( mp == null)
				throw new Exception("Could not find " + key);
			
			if( mp.getExperiment().equals("Beef Supplementation") && mp.getDateOfExperiment().endsWith("17") ) 
			{
				writer.write(key);
				
				for( int x=0;x < taxaNames.size(); x++)
					writer.write("\t" + splits[x+1]);
				
				writer.write("\n");
			}
		}
			
		writer.flush();  writer.close();	 
		
		OtuWrapper wrapper = new OtuWrapper(outFile);
		
		File outFileLogNorm = getLogNormalFile(level);
		wrapper.writeNormalizedLoggedDataToFile(outFileLogNorm);
	}
}
