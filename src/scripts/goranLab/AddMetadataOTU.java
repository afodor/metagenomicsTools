package scripts.goranLab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataOTU
{
	
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				ConfigReader.getGoranTrialDir() + File.separator +  "otuCountsAsColumnsLogNormal.txt");
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getGoranTrialDir() + File.separator + 
				"otuCountsAsColumns.txt");
		
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMetaMap();
		HashMap<Integer, PhenotypeDataLine> phenoMap = PhenotypeDataLine.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator 
					+ "otu_withMetadata.txt")));
		
		writer.write("sample\tsanVsSol\tplq\trNumber\tfranceSequencePlasms\tnafld\tshannonDiversity\tnumSequences");
		
		String[] topHeaders = reader.readLine().split("\t");
		
		for( int x=1; x < topHeaders.length; x++)
			writer.write("\t" + topHeaders[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{ 
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			writer.write( key + "\t");
			
			MetadataFileLine mfl = metaMap.get(key);
			System.out.println(key);
			writer.write(mfl.getSanVsSol() + "\t" + mfl.getPlq3Orplq4() + "\t" + mfl.getrNumber() + "\t" );
			
			PhenotypeDataLine pdl = phenoMap.get(mfl.getPatientNumber());
			
			writer.write(pdl.getFranceSequencePlasma() + "\t" + pdl.getNafld() + "\t" + 
						wrapper.getShannonEntropy(key) + "\t" + wrapper.getNumberSequences(key) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
