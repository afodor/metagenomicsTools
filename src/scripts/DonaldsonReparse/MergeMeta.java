package scripts.DonaldsonReparse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;

public class MergeMeta
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			
			File inFile = new File( "C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\rdpOut"+ File.separator 
					+ level + "asColumnsLogNorm.txt");
			
			File outFile =new File( "C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\rdpOut"+ File.separator 
					+ level + "asColumnsLogNormPlusMeta.txt");
			 
			writeMetaFile(inFile, outFile);
		}
	}
	
	private static void writeMetaFile(File inFile, File outFile)  throws Exception
	{
		HashMap<String, String> barcodeMap = BarcodeToSampleMap.getBarcodeToSampleMap();	
		HashMap<String, MetaMapFileLine>  metaMap = MetaMapFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write(topSplits[0] + "\tsubjectID\tvisitType");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		
		for(String s= reader.readLine();  s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String sampleID = barcodeMap.get(splits[0]);
			MetaMapFileLine mfl = metaMap.get( sampleID );
			
			if(mfl == null)
			{
				System.out.println("Could not find " + sampleID );	
			}
			else
			{
				writer.write(splits[0] + "\t" + mfl.getSubjectID() + "\t" + mfl.getVisitType());
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
				System.out.println("found " + sampleID);
			}
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
