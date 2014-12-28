package mbqc.rdp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import mbqc.MBQC_SampleParser;
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
					HashMap<String, List<RawDesignMatrixParser>> metaMap,
					HashMap<String, MBQC_SampleParser> mbqcSampleMetaMap) throws Exception
	{
		writer.write("fromRDPsampleID\tmbqcSampleID\tr1OrR2\thasLookup\tmbqcID\twetlabID\twetlabExtractionID\t");
		writer.write("NCI_Label\tNCI_Subj\tUC_ID\tSample_ID\tSampletype\tHealthstatus\tVisit\tSex\tAge\tBMI_Extracted_DNA");
		
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
				
				writer.write( "true\t" +  rdmp.getMbqcID() + "\t" + rdmp.getSequecingWetlab() + "\t" + 
								rdmp.getExtractionWetlab() + "\t");
			}
			else
			{
				writer.write("false\t\t\t\t");
			}
			
			MBQC_SampleParser mbq = null;
			
			if( list != null)
			{
				RawDesignMatrixParser rdmp = list.get(0);
				
				if( rdmp.getMbqcID() != null && rdmp.getMbqcID().trim().length() > 0)
					mbq = mbqcSampleMetaMap.get(rdmp.getMbqcID());
			}
			
			if( mbq != null  )
			{
				//writer.write("BMI_Extracted_DNA");
				writer.write( mbq.getNCI_Label() + "\t");
				writer.write( mbq.getNCI_Subj() + "\t");
				writer.write( mbq.getUC_ID() + "\t");
				writer.write( mbq.getSample_ID() + "\t");
				writer.write( mbq.getSample_type() + "\t");
				writer.write( mbq.getHealth_status() + "\t");
				writer.write(mbq.getVisit() + "\t");
				writer.write( mbq.getSex() + "\t");
				writer.write( mbq.getAge() + "\t");
				writer.write( mbq.getExtracted_DNA() );
			}
			else
			{
				writer.write("\t\t\t\t\t\t\t\t\t");
				
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
		HashMap<String, MBQC_SampleParser> mbqcSampleMetaMap = 
					MBQC_SampleParser.getMetaMap();
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
			
			addSomeMetadata(reader, writer,metaMap, mbqcSampleMetaMap);
		}
	}
}
