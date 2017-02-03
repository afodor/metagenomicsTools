package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddTaxaToOTUs
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Long, String> map = getOtuToTaxaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"merged" + File.separator + 
				"metapValuesFor_otu_read1_.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"merged" + File.separator + 
				"metapValuesFor_otu_read1_WithTaxa.txt"
				)));
		
		writer.write(reader.readLine() + "\tclassification\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			Long key = Long.parseLong(splits[0].replaceAll("\"", "").replace("X", ""));
			
			String taxaString = map.get(key);
			
			if( taxaString == null)
				throw new Exception("No");
			
			writer.write(s + "\t" + taxaString + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static HashMap<Long, String> getOtuToTaxaMap() throws Exception
	{
		HashMap<Long, String> map = new HashMap<Long,String>();
		
		addToMap(map, new File(ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"qiimeSummary" + File.separator + "diverticulosissummaryTaxonomy_file3.txt"));
		
		addToMap(map, new File(ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"qiimeSummary" + File.separator + "diverticulosissummaryTaxonomy_file4.txt"));
		
		return map;
	}
	
	private static void addToMap(HashMap<Long, String> map, File inFile ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			Long key = Long.parseLong(splits[0]);
			
			if( map.containsKey(key) && ! map.get(key).equals(splits[1]))
				throw new Exception("No " + map.get(key) + " " + splits[1]);
			
			map.put(key, splits[1]);
		}
		
		
		reader.close();
	}
}
