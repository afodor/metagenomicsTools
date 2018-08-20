package scripts.farnaz.dada2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;


import utils.ConfigReader;

public class MergeMetadata
{
	public static String getFirstLine(File metaFile ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		
		String val = reader.readLine().replace("#", "");
		
		reader.close();
		return val;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getFarnazDada2Directory() + File.separator + 
					"pcoa_dada2.txt");
		
		File outFile = new File(ConfigReader.getFarnazDada2Directory() + File.separator + 
					"pcoa_dada2PlusMeta.txt");
		
		File metaFile = new File(ConfigReader.getFarnazDada2Directory() + File.separator + "Farnaz_MDMF_MBP.txt");
		
		addMetadata(inFile, outFile, metaFile);
	}
	
	private static void addMetadata(File inFile, File outFile, File metaFile) throws Exception
	{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		String firstLine = reader.readLine();
		
		writer.write("id\t");
		
		writer.write(getFirstLine(metaFile));
		
		writer.write("\t"  + firstLine + "\n" );
		
		HashMap<String, String> metaMap = getFirstLineMap(metaFile);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			String key = splits[0];
			
			writer.write(key + "\t");
			
			String metaLine = metaMap.get(key);
			
			if( metaLine == null)
				throw new Exception("No " + key);
			
			writer.write(metaLine);
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
			
		}
		
		reader.close();
		writer.flush();  writer.close();
	}
	
	public static HashMap<String, String> getFirstLineMap(File metaFile) throws Exception
	{
		HashMap<String, String>  map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ;  s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, s);
		}
		
		reader.close();
		return map;
	}
}
