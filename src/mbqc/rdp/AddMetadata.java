package mbqc.rdp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import mbqc.RawDesignMatrixParser;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	private static String r1OrR2(String rdpID ) throws Exception
	{
		String[] splits = rdpID.split("_");
		return splits[2].substring(0,2);
	}
	
	private static void addSomeMetadata(BufferedReader reader, 
					BufferedWriter writer,
					HashMap<String, List<RawDesignMatrixParser>> metaMap) throws Exception
	{
		writer.write("fromRDPsampleID\tmbqcSampleID\tr1OrR2\thasLookup\tmbqcID\twetlabID");
		
		String[] splits = reader.readLine().split("\t");
			for( int x=1; x < splits.length; x++)
				writer.write("\t"  + splits[x]);
			
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			String id = attemptCompoundID(splits[0]);
			
			List<RawDesignMatrixParser> list= metaMap.get(id);
			
			writer.write(splits[0]+ "\t");
			writer.write(id + "\t");
			writer.write(r1OrR2(splits[0]) + "\t");
			
			if( list != null)
			{
				RawDesignMatrixParser rdmp = list.get(0);
				
				// leave off leading tab
				writer.write( "true\t" +  rdmp.getMbqcID() + "\t" + rdmp.getExtractionWetlab() );
			}
			else
			{
				// leave off leading tab..
				writer.write("false\t\t");
			}
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	private static String attemptCompoundID(String id) throws Exception
	{
		id = id.replace("_R1.fastq.gz", "");
		id = id.replace("_R2.fastq.gz", "");
		
		StringTokenizer sToken = new StringTokenizer(id, "_");
		
		String firstToken = sToken.nextToken();
		String secondToken = sToken.nextToken().substring(2);
		
		return firstToken + "." + secondToken;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<RawDesignMatrixParser>> metaMap =  RawDesignMatrixParser.getByLastTwoTokens();
				
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++ )
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			BufferedReader reader = new BufferedReader(new FileReader(new File( 
					ConfigReader.getMbqcDir() + 
					File.separator +  "rdpAnalysis" +  File.separator 
					+ "pcoa_" +  WriteFirstColumns.NUM_COLUMNS 
					+ "_" + NewRDPParserFileLine.TAXA_ARRAY[x] +".txt"
					)));
			
			BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
					ConfigReader.getMbqcDir() + 
					File.separator +  "rdpAnalysis" +  File.separator 
					+ "pcoa_" +  WriteFirstColumns.NUM_COLUMNS 
					+ "_" + NewRDPParserFileLine.TAXA_ARRAY[x] +"_withMetadata.txt")));
			
			addSomeMetadata(reader, writer,metaMap);
		}
	}
}
