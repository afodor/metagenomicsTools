package scripts.evanFeb2018;

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
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			File inFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +".txt");
			
			File outFile = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"spreadsheets" + File.separator + "pcoa_withTaxa" + 
					NewRDPParserFileLine.TAXA_ARRAY[x]  +"WithMetadata.txt");
			
			addMetadata(inFile, outFile);
			
		}
	}
	
	private static void addMetadata(File inFile, File outFile) throws Exception
	{
		HashMap<String, String[]> metaMap =getMetaLineMap();
		
		//System.out.println(metaMap.keySet());
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer=  new BufferedWriter(new FileWriter(outFile));
		
		writer.write("fileID\tsampleID\tbatch\t" + reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s !=null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			StringTokenizer sToken = new StringTokenizer(splits[0],"_");
			
			String key = sToken.nextToken();
			String[] metaSplits = metaMap.get(key);
			
			if( metaSplits == null)
				System.out.println("Could not find " + key);
			
			writer.write(splits[0] + "\t" + key + "\t" + sToken.nextToken().charAt(0) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static HashMap<String, String[]> getMetaLineMap() throws Exception
	{
		HashMap<String, String[]> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"meta-data for analysis - 2018.02.21.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			String key = splits[0].trim();
			
			if( map.containsKey(key) )
				throw new Exception("Dupliate" + key);
			
			map.put(key, splits);
		}
		
		reader.close();
		return map;
	}
}
