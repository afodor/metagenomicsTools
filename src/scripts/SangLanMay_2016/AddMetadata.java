package scripts.SangLanMay_2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getSangLabMay2016Dir()
					+ File.separator + "forwardSpreadsheets" + File.separator + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getSangLabMay2016Dir()
					+ File.separator + "forwardSpreadsheets" + File.separator + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNorm.txt");
			
			File outFile = new File( 
					ConfigReader.getSangLabMay2016Dir()
					+ File.separator + "forwardSpreadsheets" + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile,false);
			
			logNormalizedFile = new File(ConfigReader.getSangLabMay2016Dir()
					+ File.separator + "forwardSpreadsheets" +
					File.separator +  "mds_"+ NewRDPParserFileLine.TAXA_ARRAY[x] +  ".txt" );
			
			outFile = new File( ConfigReader.getSangLabMay2016Dir()
					+ File.separator + "forwardSpreadsheets" +
					File.separator +  "mds_"+ NewRDPParserFileLine.TAXA_ARRAY[x] +  "PlusMetadata.txt" );
			
			addMetadata(wrapper, logNormalizedFile, outFile, true );
		}
		
	}
	
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile, boolean fromR) throws Exception
	{
		HashMap<String, String> categoryMap = BarcodeFileLine.getCategoryMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tcategory\tnumberSequencesPerSample\tshannonEntropy");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			writer.write(key+ "\t" + categoryMap.get(key.replaceAll(".fastatoRDP.txt", "")) + "\t" + 
					wrapper.getNumberSequences(key) 
						+ "\t" + wrapper.getShannonEntropy(key) );
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
