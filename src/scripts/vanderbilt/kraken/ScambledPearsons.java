package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import parsers.NewRDPParserFileLine;
import utils.Avevar;
import utils.ConfigReader;
import utils.Pearson;

public class ScambledPearsons
{
	private static final Random RANDOM = new Random(3423423);
	
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
	
	private static double getAPearson(int sample1ID, int sample2ID, List<String> samples, 
							 HashMap<String, HashMap<String, Holder>>  dataMap)
	{
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();
		
		HashMap<String, Holder> firstMap= dataMap.get(samples.get(sample1ID));
		HashMap<String, Holder> secondMap= dataMap.get(samples.get(sample2ID));
		
		HashSet<String> taxaSet = new HashSet<String>();
		taxaSet.addAll(firstMap.keySet());
		taxaSet.addAll(secondMap.keySet());
		
		for(String s : taxaSet)
		{
			Holder h1 = firstMap.get(s);
			Holder h2 = secondMap.get(s);
			
			if( h1 != null && h2 != null && h1.krakenLevel> 0 && h2.rdpLevel >0 )
			{
				list1.add( Math.log10( h1.krakenLevel));
				list2.add( Math.log10( h2.rdpLevel));
			}
		}
		
		if( list1.size() <= 2 )
			return 0;
		
		return Pearson.getPearsonR(list1, list2);
		
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			writePivotsForALevel(NewRDPParserFileLine.TAXA_ARRAY[x]);
	}
	
	private static void writePivotsForALevel(String level) throws Exception
	{
		System.out.println(level);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 
						File.separator + "mergedKrakenRDP_" + level + "_pearsons.txt")));
		
		writer.write("sample\tindex\trealR\tscambledRAvg\tscrambedRSD\n");
		
		HashMap<String, HashMap<String, Holder>>  map = getMap(level);
		List<String> samples = new ArrayList<String>(map.keySet());
		Collections.sort(samples);
		
		for( int x=0; x < samples.size(); x++)
		{
			HashSet<Integer> usedSamples = new HashSet<Integer>();
			usedSamples.add(x);
			
			writer.write(samples.get(x) + "\t");
			writer.write( (x+1) +"\t");
			writer.write( getAPearson(x, x, samples,map) + "\t" );
			List<Double> permutations =new ArrayList<Double>();
			
			for( int y=0; y < 20; y++)
			{
				int randomVal = RANDOM.nextInt(samples.size());
				
				while( usedSamples.contains(randomVal) )
					randomVal= RANDOM.nextInt(samples.size());
				
				usedSamples.add(randomVal);
				permutations.add(getAPearson(x, randomVal, samples,map));
			}
			
			Avevar aVar = new Avevar(permutations);
			writer.write(aVar.getAve() + "\t");
			writer.write(aVar.getSD() + "\n");
		}
		
		
		writer.flush();  writer.close();
	}
}
