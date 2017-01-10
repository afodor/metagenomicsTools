package adenomasRelease;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;


import parsers.OtuWrapper;
import scripts.TopeSeptember2015Run.AddMetadataForKraken;
import utils.ConfigReader;

public class AddMetadataKraken
{
	
	public static void main(String[] args) throws Exception
	{
		String[] taxaArray = AddMetadataForKraken.TAXA_ARRAY;
		for( int x=0; x <taxaArray.length; x++)
		{
			String taxa = taxaArray[x];
			System.out.println(taxa);
			
			// these are mislabeled as 2015; should be 2012 
			File countFile = new File(ConfigReader.getAdenomasReleaseDir() 
					+ File.separator + "krakenSummary" +
					File.separator + "adenomas_2015_kraken_" + taxa + ".txt");
			
			OtuWrapper wrapper = new OtuWrapper( countFile );
							
			File outFile = new File( ConfigReader.getAdenomasReleaseDir() 
					+ File.separator + "krakenSummary" +
					File.separator + "adenomas_2012_kraken_" + taxa + "PlusMetada.txt");
			
			addMetadata(wrapper, countFile, outFile);
		}
		
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile) throws Exception
	{
		HashMap<String, String> caseControl = AddMetadata.getCaseControlMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tcaseControl");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "").replace("toRDP.txt", "");
			
			writer.write(key+ "\t" + caseControl.get(key) );
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
