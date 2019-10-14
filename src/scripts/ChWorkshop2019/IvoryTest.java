package scripts.ChWorkshop2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.processing.Filer;

import utils.Avevar;

public class IvoryTest
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File( "C:\\chWorkshop\\190709-DORSEY\\03_countsTables_txtFormat\\190709-DORSEY-txt\\taxa-table.txt");
		File ivoryFile =new File("C:\\chWorkshop\\190709-DORSEY\\04_statistics\\190709-DORSEY_normalizedCounts.txt");
		
		HashMap<String, HashMap<String,Long>> map = getCounts(inFile);
		HashMap<String, HashMap<String,Double>> ivoryMap = parseIvory(ivoryFile);
	
		double totalMean = getTotalMean(map);
		
		System.out.println(totalMean);
		
		checkNormalization(map, ivoryMap, totalMean);
	}
	
	private static void checkNormalization(HashMap<String, HashMap<String,Long>> map ,
			HashMap<String, HashMap<String,Double>> ivoryMap, double totalMean ) throws Exception
	{
		
		List<String> aList = new ArrayList<>(map.keySet());
		List<String> anotherList = new ArrayList<>(ivoryMap.keySet());
		
		System.out.println(aList.size());
		System.out.println(anotherList.size());

		Collections.sort(aList);
		Collections.sort(anotherList);
		
		if( ! aList.equals(anotherList))
			throw new Exception("No");
		
		for(String sample: aList)
		{
			HashMap<String, Double> ivoryInner = ivoryMap.get(sample);
			HashMap<String, Long> innerMap = map.get(sample);
			
			List<String> bList = new ArrayList<>(ivoryInner.keySet());
			List<String> cList = new ArrayList<>(innerMap.keySet());
			
			Collections.sort(bList);
			Collections.sort(cList);
			
			if( ! bList.equals(cList))
				throw new Exception("No");
			
			Long countSum =0l;
			
			for( Long l : innerMap.values() )
				countSum+=l;
			
			double total = (double) countSum;
			
			
			for( String s : ivoryInner.keySet() )
			{
				Double ivoryVal = ivoryInner.get(s);
				
				Double expectedVal = ((double)innerMap.get(s)) / total * totalMean + 1;
				
				if( Math.abs(ivoryVal- expectedVal) > 0.0001)
					throw new Exception("No " + ivoryVal + " " + expectedVal + " " + sample + " "+ s);
				
				//System.out.println(ivoryVal +  " " + expectedVal);
			}
			
		}

	}
	
	private static double getTotalMean(HashMap<String, HashMap<String,Long>> map) throws Exception
	{
		List<Double> list = new ArrayList<>();
		
		for( HashMap<String,Long> innerMap : map.values() )
		{
			long count =0;
			
			for( Long l : innerMap.values())
				count += l;
			
			list.add( (double)count );
		}
		
		return new Avevar(list ).getAve();
	}
	
	private static HashMap<String, HashMap<String,Double>> parseIvory(File inFile ) throws Exception
	{
		HashMap<String, HashMap<String,Double>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String topLine = reader.readLine();
		
		List<String> taxa  = new ArrayList<>();
		
		String[] topSplits = topLine.split("\t");
		
		for(int x=1; x < topSplits.length;x++)
		{
			taxa.add(topSplits[x]);
		}
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String sample= splits[0];
			
			HashMap<String,Double> innerMap = new LinkedHashMap<>();
			map.put(sample, innerMap);
			
			int index=1;
			for(String taxon : taxa)
			{
				if(innerMap.containsKey(taxon))
					throw new Exception("No");
				
				innerMap.put(taxon,  Double.parseDouble(splits[index]));
				
				index++;
			}
		}
		
		return map;
	}
	
	
	// outer key is sample;  inner key is taxa
	
	private static HashMap<String, HashMap<String,Long>> getCounts(File inFile) throws Exception
	{
		HashMap<String, HashMap<String,Long>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String topLine = reader.readLine();
		
		List<String> samples = new ArrayList<>();
		
		String[] topSplits = topLine.split("\t");
		
		for(int x=1; x < topSplits.length-2;x++)
		{
			samples.add(topSplits[x]);
			map.put(topSplits[x], new LinkedHashMap<String,Long>());
		}
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String taxa= splits[0];
			
			int index=1;
			for(String sample : samples)
			{
				HashMap<String,Long> innerMap= map.get(sample);
				
				if(innerMap.containsKey(taxa))
					throw new Exception("No");
				
				innerMap.put(taxa, (long) Double.parseDouble(splits[index]));
				
				index++;
			}
		}
		
		return map;
	}
}

