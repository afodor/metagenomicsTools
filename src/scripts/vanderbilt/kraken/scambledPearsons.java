package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import utils.ConfigReader;

public class scambledPearsons
{
	private static final Random RANDOM = new Random(3423423);
	
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
	
	private double getAPearson(int sampleID, List<String> samples, 
							boolean shuffle, HashMap<String, HashMap<String, Holder>>  dataMap)
	{
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();
		
		HashMap<String, Holder> realMap = dataMap.get(samples.get(sampleID));
		
		int randomIndex= RANDOM.nextInt( samples.size() );
		
		while(randomIndex== sampleID)
			randomIndex= RANDOM.nextInt( samples.size() );
		
		//HashMap<String, Holder> randomMap= dataMap.get(sampleID.get(randomIndex));
		
		return 0;
		
	}
	
	private static void writePivotsForALevel(String level) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 
						File.separator + "mergedKrakenRDP_" + level + "_pearsons.txt")));
		
		writer.write("sample\trealR\tscambledRAvg\tscrambedRSD\n");
		
		HashMap<String, HashMap<String, Holder>>  map = getMap(level);
		List<String> samples = new ArrayList<String>(map.keySet());
		Collections.sort(samples);
		
		
		writer.flush();  writer.close();
	}
}
