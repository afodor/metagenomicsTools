package scripts.vanderbilt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class addMetadata
{
	private static void addSomeMetadata( OtuWrapper wrapper,
				String inFile, String outFile, boolean rOutput )
		throws Exception
	{
		System.out.println(outFile);
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				inFile)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				outFile));
		
		if( ! rOutput)
			writer.write("sample\t");
		
		writer.write("numSequencesPerSample\tunrarifiedRichness\tshannonDiversity\tshannonEveness\t" + 
				"run\tstoolOrSwab\t" + reader.readLine());
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String sampleKey = splits[0].replaceAll("\"", "");
			writer.write( wrapper.getCountsForSample(sampleKey) + "\t");
			writer.write(wrapper.getRichness(sampleKey) + "\t");
			writer.write(wrapper.getShannonEntropy(sampleKey) + "\t" );
			writer.write(wrapper.getEvenness(sampleKey) + "\t" );
			
			writer.write(sampleKey.split("_")[1] + "\t");
			
			if( sampleKey.startsWith("ST"))
				writer.write("stool\t");
			else if ( sampleKey.startsWith("SW"))
				writer.write("swab\t");
			else throw new Exception(" NO " );
			
			writer.write(s + "\n");
		}
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
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
		}
	}
}
