package scripts.jobin.April2015.humanHPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMapBySampleID();
		HashMap<String, Double> quantMap = MergeQA_QC_Map.getQuantEstimates();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + 	"hpc_pcoa_phyla.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + "hpc_pcoa_phylaWithMetadata.txt")));
		
		writer.write("sample\treadNumber\tdiseaseGroup\tquant\t");
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			MetadataFileLine mfl = metaMap.get(splits[0].replaceAll("\"", "").split("_")[0]);
			
			writer.write( splits[0].replaceAll("\"", "").split("_")[0] + "\t" );
			writer.write( splits[0].charAt(splits[0].length() -2) + "\t");
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
