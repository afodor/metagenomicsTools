package scripts.TopeDiverticulosisJan2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static HashMap<String, Integer> getCaseControlMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeJan2016Dir() + File.separator + "tk_out_29Jan2016_corrected.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length > 2)
				throw new Exception("No");
			
			if( splits.length == 2)
			{
				if( map.containsKey(splits[0]))
					throw new Exception("No");
				
				map.put(splits[0], Integer.parseInt(splits[1]));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeJan2016Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getTopeJan2016Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File outFile = new File( ConfigReader.getTopeJan2016Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile);
		}
		
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile) throws Exception
	{
		HashMap<String, Integer> caseControlMap = getCaseControlMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tnumberSequencesPerSample\tshannonEntropy\tcaseContol");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			writer.write(key+ "\t" + wrapper.getNumberSequences(splits[0]) 
						+ "\t" + wrapper.getShannonEntropy(splits[0]) + "\t" );
			
			Integer val = caseControlMap.get(key);
			
			if( val == null)
				writer.write("NA");
			else
				writer.write("" + val);
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
