package scripts.JobinCardio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, MetadataFileLine> metaMap = 
				MetadataFileLine.getMetaMap();
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "pivoted_" +  taxa +  "asColumns.txt");
		
			File inFile = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "pcoa_" + taxa + ".txt");
			
			File outFile = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "pcoa_" + taxa + "PlusMetadata.txt");
			
			addSomeMetadata(wrapper, inFile, outFile, true, metaMap);
			
			File inFileTaxa = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "pivoted_" + taxa +  "asColumnsLogNormal.txt");
			
			File outFileTaxa = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "pivoted_" + taxa +  "asColumnsLogNormalPlusMetadata.txt");
			
			addSomeMetadata(wrapper, inFileTaxa, outFileTaxa, false, metaMap);
			
			File inCVsHPcoa = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "mds_" + taxa + "_H_vs_C.txt");
			
			File outCVsHPcoa = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheetsMerged" + File.separator + "mds_" + taxa + "_H_vs_CPlusMetadata.txt");
			
			addSomeMetadata(wrapper, inCVsHPcoa, outCVsHPcoa, true, metaMap);
			
		}
	}
	
	private static void addSomeMetadata(OtuWrapper unnormalizedWrapper, File inFile, File outFile, boolean fromR,
			HashMap<Integer, MetadataFileLine> metaMap) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sampleID\tsampleIndex\treadNumber\texperimentString\texperimentInt\tgroup\tisRun2\tnumSequencesPerSample\tshannonDiversity");
		if( fromR)
		{
			writer.write( "\t" + reader.readLine() + "\n");
		}
		else
		{
			String[] splits = reader.readLine().split("\t");
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
			
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			StringTokenizer sToken = new StringTokenizer(splits[0].replaceAll("\"", ""), "_");
			sToken.nextToken();
			
			int sampleId = Integer.parseInt(sToken.nextToken());
			int readNum = Integer.parseInt(sToken.nextToken().replace("R", ""));
			
			if( sToken.hasMoreTokens())
				throw new Exception("No");
			
			MetadataFileLine mfl = metaMap.get(sampleId);
			
			writer.write(splits[0].replaceAll("\"", "") + "\t" + sampleId + "\t" + readNum + "\t" + 
							mfl.getExperimentString() + "\t" + mfl.getExperimentInt() + "\t" + 
									mfl.getGroup() + "\t" + mfl.getIsrun2() 
									+ "\t" + unnormalizedWrapper.getNumberSequences(splits[0].replaceAll("\"", "")) + 
										"\t" + unnormalizedWrapper.getShannonEntropy(splits[0].replaceAll("\"", "")));	
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		reader.close();
	}
}
