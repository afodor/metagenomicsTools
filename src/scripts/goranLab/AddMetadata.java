package scripts.goranLab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	private static HashMap<String, Double> quickDiversityMap(File inFile) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null && s.trim().length() >0; s= reader.readLine())
		{
			double sum =0;
			
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			for( int x=1; x < splits.length; x++)
			{
				Double d = Double.parseDouble(splits[x])/100.0;
				
				if( d > 0 )
					sum += d * Math.log(d);
			}
			
			map.put(splits[0], -sum);
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		String level = "phylum";
		File inFile = new File(
				ConfigReader.getGoranTrialDir() + File.separator + 
				level + "AsColumns.txt");
		
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMetaMap();
		HashMap<Integer, PhenotypeDataLine> phenoMap = PhenotypeDataLine.getMap();
		HashMap<String, Double> diversityMap = quickDiversityMap(
				new File(ConfigReader.getGoranTrialDir() + File.separator + 
				level + "AsColumns.txt"));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator + 
					level + "_taxa_withMetadata.txt")));
		
		writer.write("sample\tsanVsSol\tplq\trNumber\tfranceSequencePlasms\tnafld\tshannonDiversity\t" 
					+   reader.readLine() + "\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{ 
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			writer.write( key + "\t");
			
			MetadataFileLine mfl = metaMap.get(key);
			
			writer.write(mfl.getSanVsSol() + "\t" + mfl.getPlq3Orplq4() + "\t" + mfl.getrNumber() + "\t" );
			
			PhenotypeDataLine pdl = phenoMap.get(mfl.getPatientNumber());
			
			writer.write(pdl.getFranceSequencePlasma() + "\t" + pdl.getNafld() + "\t" + diversityMap.get(key) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
