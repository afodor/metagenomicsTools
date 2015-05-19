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
		for( int x=1; x <=6 ; x++)
		{
			System.out.println( CollapseToDifferentLevels.TAXA[x-1]);
			File loggedFile = new File(ConfigReader.getGoranTrialDir() + 
				File.separator + CollapseToDifferentLevels.TAXA[x-1] + "fromOTUsAsColumnLogNorm.txt");
			
			File outFile = new File( ConfigReader.getGoranTrialDir() + 
					File.separator + CollapseToDifferentLevels.TAXA[x-1] + "fromOTUsAsColumn.txt");
			
			addMetadata(loggedFile, outFile, false);
			
			loggedFile = new File(ConfigReader.getGoranTrialDir() + 
				File.separator + CollapseToDifferentLevels.TAXA[x-1] + "LogNorm_pcoa_.txt");
			
			addMetadata(loggedFile, outFile, true);
			
		}
	}
	
	private static String valueOrNA(Object o)
	{
		if( o==null)
			return "NA";
		else
			return o.toString();
	}
	
	public static void addMetadata(File logFileToNormalize, File wrapperFile, boolean fromR) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(wrapperFile);
		
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMetaMap();
		HashMap<Integer, PhenotypeDataLine> phenoMap = PhenotypeDataLine.getMap();
		HashMap<Integer, UpdatedPhenotypeParser> updateMap = UpdatedPhenotypeParser.getMetaMap();
		HashMap<Integer, Double> sug_Bev_updateMap2 = UpdatedPhenotypeParser2.getColumnMap(UpdatedPhenotypeParser2.SUGBEV_PLUS_JUICE);
		HashMap<Integer, Double> liverFat_updateMap2 = UpdatedPhenotypeParser2.getColumnMap(UpdatedPhenotypeParser2.HFFSOL3T);
		HashMap<Integer, Double> totalFat_udpateMap2 = UpdatedPhenotypeParser2.getColumnMap(UpdatedPhenotypeParser2.TOTAL_FAT);
		HashMap<Integer, Double> sex_udpateMap2 = UpdatedPhenotypeParser2.getColumnMap(UpdatedPhenotypeParser2.SEX);
		HashMap<Integer, Double> age_udpateMap2 = UpdatedPhenotypeParser2.getColumnMap(UpdatedPhenotypeParser2.AGE);
		
		BufferedReader reader = new BufferedReader(new FileReader(logFileToNormalize));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator 
					+ logFileToNormalize.getName().replace(".txt", "") + "plusMetadata" + (fromR ? "_PCOA" : "") +  ".txt")));
		
		writer.write("sample\tsanVsSol\tplq\trNumber\tfranceSequencePlasms\tnafld\tm_tsug\t");
		
		writer.write( "PNPLA3CODEDGRP\tmTotSugarMedianSplit\tmAddedSugarMedianSplit\tmFructoseMedianSplit\tsugbev_plusjuice\tupdatedsugbev_plusjuice\t"
				+ UpdatedPhenotypeParser2.HFFSOL3T + "\t" + UpdatedPhenotypeParser2.TOTAL_FAT + "\t" + UpdatedPhenotypeParser2.SEX + "\t"
					+ UpdatedPhenotypeParser2.AGE + "\t");
		
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
			writer.write( valueOrNA(mfl.getSanVsSol()) 
					+ "\t" + valueOrNA(mfl.getPlq3Orplq4()) + "\t" + 
					valueOrNA(mfl.getrNumber()) + "\t" );
			
			PhenotypeDataLine pdl = phenoMap.get(mfl.getPatientNumber());
			
			writer.write( valueOrNA(pdl.getFranceSequencePlasma()) + "\t" + 
			valueOrNA(pdl.getNafld()) + "\t" + valueOrNA(pdl.getmTotalSugar()) + "\t"+
						valueOrNA(pdl.getPNPLA3CODEDGRP()) + "\t" +
							valueOrNA(pdl.getmTotSugarMedianSplit()) + "\t" + 
							valueOrNA(pdl.getmAddedSugarMedianSplit()) + "\t" +
							valueOrNA(pdl.getmFructoseMedianSplit()) + "\t" + 
							valueOrNA(updateMap.get(mfl.getPatientNumber()).getSugbev_plusjuice()) + "\t" + 
							valueOrNA(sug_Bev_updateMap2.get(mfl.getPatientNumber()))+ "\t" + 
							valueOrNA(liverFat_updateMap2.get(mfl.getPatientNumber()))+ "\t" + 
							valueOrNA(totalFat_udpateMap2.get(mfl.getPatientNumber()))+ "\t" +
							valueOrNA(sex_udpateMap2.get(mfl.getPatientNumber()))+ "\t" +
							valueOrNA(age_udpateMap2.get(mfl.getPatientNumber()))+ "\t" +
						wrapper.getShannonEntropy(key) + "\t" + wrapper.getNumberSequences(key) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
