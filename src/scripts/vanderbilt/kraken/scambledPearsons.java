package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class scambledPearsons
{
	public static void main(String[] args) 
	{
		
	}
	
	private static class Holder
	{
		double krakenLevel;
		double rdpLevel;
		double kraken16SLevel;
	}
	
	/*
	 * Outer key is sampleID; inner key is taxa
	 */
	private static HashMap<String, HashMap<String, Holder>> getMap(String level) throws Exception
	{
		HashMap<String, HashMap<String, Holder>> map = 
				new HashMap<String, HashMap<String,Holder>>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
				ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 
						File.separator + "mergedKrakenRDP_" + level + ".txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String sample = new String(splits[0]);
			
			HashMap<String, Holder> innerMap = map.get(sample);
			
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Holder>();
				map.put(sample, innerMap);
			}
			
			String taxa = new String(splits[2]);
			
			if( innerMap.containsKey(taxa))
				throw new Exception("Duplicate " + taxa);
			
			Holder h= new Holder();
			h.krakenLevel = Double.parseDouble(splits[3]);
			h.rdpLevel = Double.parseDouble(splits[4]);
			h.kraken16SLevel = Double.parseDouble(splits[5]);
			innerMap.put(taxa, h);
		}
		
		reader.close();
		
		return map;
	}
	
	private static void writePivotsForALevel(String level) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 
						File.separator + "mergedKrakenRDP_" + level + "_pearsons.txt")));
		
		writer.write("sample\trealR\tscambledRAvg\tscrambedRSD\n");
		
		HashMap<String, HashMap<String, Holder>>  map = getMap(level);
		
		writer.flush();  writer.close();
	}
}
