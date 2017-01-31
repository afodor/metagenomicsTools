package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import utils.ConfigReader;

public class PivotAndFilterOTUs
{
	public static void main(String[] args) throws Exception
	{
		File originalFile = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
				"diverticulosisOTUs" + File.separator +  "otus" + File.separator + 
					"tope_otu_2_filteredLowSequenceSamples.txt");
		
		List<String> sampleList = getSampleList(originalFile);
		
		 HashMap<String, List<Integer>> counts = getFilteredMap(originalFile);
		 
		 File outFile = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
					"diverticulosisOTUs" + File.separator +  "otus" + File.separator + 
					"tope_otu_asColumns.txt");
		 
		 writePivot(counts, sampleList, outFile);
	}
	
	private static void writePivot( HashMap<String, List<Integer>> counts, 
			List<String> sampleList, File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		List<String> taxaList = new ArrayList<String>(counts.keySet());
		Collections.sort(taxaList);
		
		writer.write("sample");
		
		for(String s : taxaList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		int index =0;
		for(String sample : sampleList)
		{
			writer.write(sample);
			
			for(String taxa : taxaList)
				writer.write("\t" + counts.get(taxa).get(index));
			
			writer.write("\n");
			
			index++;
		}
		
		writer.flush(); writer.close();
	}
	
	private static HashMap<String, List<Integer>> getFilteredMap(File inFile) throws Exception
	{
		HashMap<String, List<Integer>> map = new HashMap<String,List<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s=reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			double fractionAboveZero =0;
			
			List<Integer> list = new ArrayList<Integer>();
			
			for( int x=1; x < splits.length; x++)
			{
				int val =Integer.parseInt(splits[x]); 
				list.add(val);
				if( val > 0 )
					fractionAboveZero++;
			}
			
			if( fractionAboveZero > list.size() / 4)
			{
				map.put(key, list);
			}
		}
		
		
		reader.close();
		return map;
		
		
	}
	
	private static List<String> getSampleList(File inFile) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length; x++)
			list.add(splits[x]);
		
		reader.close();
		
		return list;
	}
}
