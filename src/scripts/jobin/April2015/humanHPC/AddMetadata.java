package scripts.jobin.April2015.humanHPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMapBySampleID();
		HashMap<String, Double> quantMap = MergeQA_QC_Map.getQuantEstimates();
		
		OtuWrapper baseWrapper = new OtuWrapper(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"hpc_taxaAsColumns_mergedF_R_phyla.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator 
			+ "hpc_taxaAsColumns_mergedF_R_phyla.txt")));
		//+ 	"hpc_pcoa_phyla.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + 
				"hpc_taxaAsColumns_mergedF_R_phylaWithMetadata.txt")));
			//+ "hpc_pcoa_phylaWithMetadata.txt")));
		
		writer.write("sample\treadNumber\tnumSequences\tshannonDiversity\tdiseaseGroup\tquant");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			MetadataFileLine mfl = metaMap.get(splits[0].replaceAll("\"", "").split("_")[0]);
			
			writer.write( splits[0].replaceAll("\"", "").split("_")[0] + "\t" );
			writer.write( splits[0].charAt(splits[0].length() -1) + "\t");
			//System.out.println(splits[0].replaceAll("\"", "").split("_")[0]);
			writer.write(baseWrapper.getNumberSequences(splits[0].replaceAll("\"", "")) + "\t");
			writer.write(baseWrapper.getShannonEntropy(splits[0].replaceAll("\"", "")) + "\t");
			
			
			writer.write(mfl.getDiagnostic() + "\t");
			
			Double quant = quantMap.get(mfl.getRgSampleName());
			
			if( quant == null)
				throw new Exception("No " +mfl.getRgSampleName() ) ;
			
			writer.write(quant + "");
			
			for( int x=1; x < splits.length; x++)
				writer.write( "\t" + splits[x]);
			
			writer.write("\n");
			
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
