package scripts.vanderbilt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.vanderbilt.kraken.PivotKrakenReportToTaxaAsColumns;
import utils.ConfigReader;

public class AddMetadata
{
	private static void addSomeMetadata( OtuWrapper wrapper,
				String inFile, String outFile, boolean rOutput )
		throws Exception
	{
		HashMap<String, PatientMetadata> metaMap = 
				PatientMetadata.getAsMap();
		
		HashMap<String, NewBiomarkerParser> bioMarkerMetaMap = 
				NewBiomarkerParser.getMetaMap();
		
		//System.out.println(outFile);
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				inFile)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				outFile));
		
		writer.write("sample\t" + 
				"run\tstoolOrSwab\tsubjectID\tgenotype\ttreatment\ttype\ttime\t" + 
				"bax_overall\tbax_surface\tbax_bottom\tbax_overall_quant\tbax_surface_quant\tbax_bottom_quant\t" + 
				"numSequencesPerSample\tunrarifiedRichness\tshannonDiversity\tshannonEveness");
		
		if( rOutput) 
		{
			writer.write("\t" + reader.readLine().replaceAll("\"", "") + "\n");
		}
		else
		{
			String[] lineSplits = reader.readLine().split("\t");
			
			for( int x=1; x < lineSplits.length; x++ )
			{
				writer.write("\t" + lineSplits[x]);
			}
			
			writer.write("\n");
		}
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String sampleKey = splits[0].replaceAll("\"", "");
			String sampleID = sampleKey.split("_")[0];
			
			if( metaMap.get(sampleID) != null )
			{
				writer.write(sampleKey + "\t");
				
				writer.write(sampleKey.split("_")[1] + "\t");
				
				if( sampleKey.startsWith("ST"))
					writer.write("stool\t");
				else if ( sampleKey.startsWith("SW"))
					writer.write("swab\t");
				else throw new Exception(" NO " );
			
				writer.write(metaMap.get(sampleID).getStudyID() + "\t");
				writer.write(metaMap.get(sampleID).getGenotype() + "\t");
				writer.write(metaMap.get(sampleID).getTreatment()+ "\t");
				writer.write(metaMap.get(sampleID).getType()+ "\t");
				
				String type = metaMap.get(sampleID).getType();
				
				String timepoint = getTimePoint(type);
				writer.write(timepoint + "\t");
				
				String bioKey = metaMap.get(sampleID).getStudyID()  + "_" + getLookupTime(type);
				
				NewBiomarkerParser nbp = bioMarkerMetaMap.get(bioKey);
				
				/*
				 * private final Integer bax_overall;
	private final Integer bax_surface;
	private final Integer bax_bottom;
	private final Double bax_overall_quant;
	private final Double bax_surface_quant;
	private final Double bax_bottom_quant;
				 */
				//"bax_overall\tbax_surface\tbax_bottom\tbax_overall_quant\tbax_surface_quant\tbax_bottom_quant\t
				
				if( nbp != null)
				{
					writer.write(nbp.getBax_overall() + "\t" + nbp.getBax_surface() + "\t" + nbp.getBax_bottom() + "\t" + 
											nbp.getBax_overall_quant() + "\t" + nbp.getBax_surface_quant() + "\t" + nbp.getBax_bottom_quant() + "\t");
					System.out.println("Found " + bioKey );
					
				}
				else
				{
					writer.write("NA"+ "\t" + "NA"+ "\t" + "NA"+ "\t" + 
							"NA"+ "\t" + "NA"+ "\t" + "NA"+ "\t");
					System.out.println("Could not find " + bioKey );
				}
					
			
				writer.write( wrapper.getCountsForSample(sampleKey) + "\t");
				writer.write(wrapper.getRichness(sampleKey) + "\t");
				writer.write(wrapper.getShannonEntropy(sampleKey) + "\t" );
				writer.write(wrapper.getEvenness(sampleKey) + "" );
			
				String[] lineSplits = s.split("\t");
			
				for( int x=1; x < lineSplits.length; x++)
					writer.write("\t" + lineSplits[x]);
			
				writer.write("\n");
			}
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
	
	private static String getLookupTime(String type) throws Exception
	{
		if( type.equals("stool_post") || type.equals("swab_post"))
			return "1";
		else if( type.equals("stool_pre") || type.equals("swab_pre"))
			return "3";
		else throw new Exception("Could not find timepoint " + type);
	}
	
	private static String getTimePoint(String type) throws Exception
	{
		if( type.equals("stool_post") || type.equals("swab_post"))
			return "post";
		else if( type.equals("stool_pre") || type.equals("swab_pre"))
			return "pre";
		else throw new Exception("Could not find timepoint " + type);
	}
	
	public static void main(String[] args) throws Exception
	{
		for(int x=0; x < PivotKrakenReportToTaxaAsColumns.RDP_LEVELS.length; x++)
		{
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + PivotKrakenReportToTaxaAsColumns.RDP_LEVELS[x] +"_taxaAsColumns.txt");
			
			File inFile = new File(ConfigReader.getVanderbiltDir() 
					+ File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + PivotKrakenReportToTaxaAsColumns.RDP_LEVELS[x]
							+"_taxaAsColumnsLogNorm.txt");
			
			File outFile = new File(ConfigReader.getVanderbiltDir() 
					+ File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + PivotKrakenReportToTaxaAsColumns.RDP_LEVELS[x]
							+"_taxaAsColumnsLogNormWithMetadata.txt"
					);
			addSomeMetadata(wrapper, inFile.getAbsolutePath(), 
								outFile.getAbsolutePath(), false);
		
			// couldn't do strains
			if( x < PivotKrakenReportToTaxaAsColumns.RDP_LEVELS.length - 1 )
			{

				File pcoaFile = new File(ConfigReader.getVanderbiltDir() + File.separator +
						"spreadsheets" + File.separator + 
						"krakenPcoa_" + PivotKrakenReportToTaxaAsColumns.RDP_LEVELS[x] + ".txt");
			
				File pcoaOutFile = new File(ConfigReader.getVanderbiltDir() + File.separator +
						"spreadsheets" + File.separator + 
						"krakenPcoa_" + PivotKrakenReportToTaxaAsColumns.RDP_LEVELS[x] + "withMetadata.txt");	
				
				addSomeMetadata(wrapper, pcoaFile.getAbsolutePath(), pcoaOutFile.getAbsolutePath(), true);
			}
			
		}
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			OtuWrapper wrapper = new OtuWrapper(
					ConfigReader.getVanderbiltDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File pcoaFile = new File(	ConfigReader.getVanderbiltDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt");
			
			File outPCOAFile = new File(	ConfigReader.getVanderbiltDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + "withMetadata.txt");
			
			
			addSomeMetadata(wrapper, pcoaFile.getAbsolutePath(), outPCOAFile.getAbsolutePath(), true);
			
			String taxaPath = ConfigReader.getVanderbiltDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt";
			
			String outPath =  ConfigReader.getVanderbiltDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalWithMetadata.txt";
			
			addSomeMetadata(wrapper, taxaPath, outPath, false);
			
			String mergedPath = ConfigReader.getVanderbiltDir()
					 + File.separator + "spreadsheets" + 
					 File.separator + "mergedKrakenRDP_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt";
			
			String outMergedPath = ConfigReader.getVanderbiltDir()
					 + File.separator + "spreadsheets" + 
					 File.separator + "mergedKrakenRDP_" + NewRDPParserFileLine.TAXA_ARRAY[x] + "_WithMetadata.txt";
			
			addSomeMetadata(wrapper, mergedPath, outMergedPath, false);
		}
	}
}
