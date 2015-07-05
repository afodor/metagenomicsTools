package scripts.JobinCardio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
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
			
			File inFile = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_" + taxa + ".txt");
			
			File outFile = new File(ConfigReader.getJobinCardioDir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_" + taxa + "PlusMetadata.txt");
			
			addSomeMetadata(inFile, outFile, true, metaMap);
		}
	}
	
	private static void addSomeMetadata(File inFile, File outFile, boolean fromR,
			HashMap<Integer, MetadataFileLine> metaMap) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(outFile));
		
		if( fromR)
		{
			writer.write("sampleID\tsampleIndex\treadNumber\texperimentString\texperimentInt\tgroup\t" + reader.readLine() + "\n");
		}
		else
			throw new Exception("Not implemented");
			
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			sToken.nextToken();
			
			int sampleId = Integer.parseInt(sToken.nextToken());
			int readNum = Integer.parseInt(sToken.nextToken());
			
			if( sToken.hasMoreTokens())
				throw new Exception("No");
			
			MetadataFileLine mfl = metaMap.get(sampleId);
			
			writer.write(splits[0] + "\t" + sampleId + "\t" + readNum + "\t" + 
							mfl.getExperimentString() + "\t" + mfl.getExperimentInt() + "\t" + 
									mfl.getGroup() + "\n");
			
		}
		
		
		writer.flush();  writer.close();
		reader.close();
	}
}
