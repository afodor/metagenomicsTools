package scripts.IanNovember2015;

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

	private static HashMap<Integer, String> getMetadataLines() throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"HC Psych Data for Anthony_10.20.15_BlankColsDeleted.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			Integer val = Integer.parseInt(s.split("\t")[0].replace("HC", ""));
			
			if( map.containsKey(val))
				throw new Exception("No");
			
			map.put(val, s);
		}
		
		reader.close();
		return map;
	}
	
	private static String getMetaLine() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"HC Psych Data for Anthony_10.20.15_BlankColsDeleted.txt")));
		
		String s= reader.readLine();
		
		reader.close();
		
		return s;
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( 	ConfigReader.getIanNov2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getIanNov2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File outFile = new File(	ConfigReader.getIanNov2015Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile);
		}
		
	}
	
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile) throws Exception
	{
		HashMap<Integer, String> metaMap = getMetadataLines();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tread\tshannonDiversity\t" + getMetaLine());
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replace(".fastatoRDP.txt", "");
			StringTokenizer sToken = new StringTokenizer(key, "_");
			String read = sToken.nextToken();
			
			int id = Integer.parseInt(sToken.nextToken());
			
			writer.write(key+ "\t" + read + "\t" + wrapper.getShannonEntropy(splits[0]) + "\t");
			
			String metaLine = metaMap.get(id);
			
			if( metaLine == null)
			{
				for( int x=0; x < firstSplits.length; x++)
					writer.write("NA\t");
			}
			else
			{
				writer.write(metaLine);
				
			}
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
