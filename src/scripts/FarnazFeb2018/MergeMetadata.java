package scripts.FarnazFeb2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeMetadata
{
	public static void main(String[] args) throws Exception
	{
		File metaFile = new File(ConfigReader.getFarnazFeb2018Directory() + File.separator + 
					"Farnaz_MDMF_MBP_Final.txt");
		
		String firstLine = readFirstLine(metaFile);
		HashMap<Integer, String> metaMap = getMetaMap(metaFile);
		System.out.println(metaMap.size());
		
		File inFile = new File(ConfigReader.getFarnazFeb2018Directory() + File.separator + 
					"otu_table_mc2_w_tax_L6.txt");
		
		File outFile = new File(ConfigReader.getFarnazFeb2018Directory() + File.separator + 
				"otu_table_mc2_w_tax_L6WithMeta.txt");
	
		addColumns(inFile, firstLine, metaMap, outFile);
	}
	
	private static void addColumns(File inFile, String firstLine,HashMap<Integer, String> metaMap, 
				File outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sampleID");
		
		String[] topSplits = firstLine.split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] dataSplits=  s.split("\t");
			
			int key = Integer.parseInt(dataSplits[0]);
			writer.write("" + key);
			
			String metaLine = metaMap.get(key);
			String[] metaSplits = metaLine.split("\t");
			
			for( int x=1; x < metaSplits.length; x++)
				writer.write("\t" + metaSplits[x]);
			
			for( int x=1; x < dataSplits.length; x++)
				writer.write("\t" + dataSplits[x]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
	
	private static HashMap<Integer, String> getMetaMap(File file) throws Exception
	{
		HashMap<Integer, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			Integer id = Integer.parseInt(splits[0]);
			
			if( map.containsKey(id) )
				throw new Exception("Duplicate id " + id);
			
			map.put(id, s.replaceAll("\"", ""));
		}
		
		reader.close();
		
		return map;
	}
	
	private static String readFirstLine(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String returnVal = reader.readLine();
		
		reader.close();
		
		return returnVal;
	}
}
