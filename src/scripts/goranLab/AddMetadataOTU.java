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
		for( int x=1; x <=4 ; x++)
		{
			System.out.println( CollapseToDifferentLevels.TAXA[x-1]);
			File loggedFile = new File(ConfigReader.getGoranTrialDir() + 
				File.separator + CollapseToDifferentLevels.TAXA[x-1] + "fromOTUsAsColumnLogNorm.txt");
			
			File outFile = new File( ConfigReader.getGoranTrialDir() + 
					File.separator + CollapseToDifferentLevels.TAXA[x-1] + "fromOTUsAsColumn.txt");
			
			addMetadata(loggedFile, outFile, false);
		}
	}
	
	public static void addMetadata(File logFileToNormalize, File wrapperFile, boolean fromR) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(wrapperFile);
		
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMetaMap();
		HashMap<Integer, PhenotypeDataLine> phenoMap = PhenotypeDataLine.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(logFileToNormalize));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator 
					+ logFileToNormalize.getName().replace(".txt", "") + "plusMetadata.txt")));
		
		writer.write("sample\tsanVsSol\tplq\trNumber\tfranceSequencePlasms\tnafld\t");
		
		writer.write( "PNPLA3CODEDGRP\tmTotSugarMedianSplit\tmAddedSugarMedianSplit\tmFructoseMedianSplit\t");
		
		writer.write("shannonDiversity\tnumSequences");
		
		String[] topHeaders = reader.readLine().split("\t");
		
		for( int x=(fromR ? 0 : 1); x < topHeaders.length; x++)
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
						pdl.getPNPLA3CODEDGRP() + "\t" + pdl.getmTotSugarMedianSplit() + "\t" + 
								pdl.getmAddedSugarMedianSplit() + "\t" + pdl.getmFructoseMedianSplit() + "\t" + 
						wrapper.getShannonEntropy(key) + "\t" + wrapper.getNumberSequences(key) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
