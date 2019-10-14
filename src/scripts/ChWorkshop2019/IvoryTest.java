package scripts.ChWorkshop2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.Filer;

public class IvoryTest
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File( "C:\\chWorkshop\\190709-DORSEY\\03_countsTables_txtFormat\\190709-DORSEY-txt\\taxa-table.txt");
		File ivoryFile =new File("C:\\chWorkshop\\190709-DORSEY\\04_statistics\\190709-DORSEY_normalizedCounts.txt");
		
		HashMap<String, HashMap<String,Long>> map = getCounts(inFile);
		HashMap<String, HashMap<String,Double>> ivoryMap = parseIvory(ivoryFile);
		
		List<String> aList = new ArrayList<>(map.keySet());
		List<String> anotherList = new ArrayList<>(ivoryMap.keySet());
		
		System.out.println(aList.size());
		System.out.println(anotherList.size());
		
		if( ! aList.equals(anotherList))
			System.out.println("No");
	}
	
	private static HashMap<String, HashMap<String,Double>> parseIvory(File inFile ) throws Exception
	{
		HashMap<String, HashMap<String,Double>> map = new HashMap<>();
		
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
			
			HashMap<String,Double> innerMap = new HashMap<>();
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
		HashMap<String, HashMap<String,Long>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String topLine = reader.readLine();
		
		List<String> samples = new ArrayList<>();
		
		String[] topSplits = topLine.split("\t");
		
		for(int x=1; x < topSplits.length-2;x++)
		{
			samples.add(topSplits[x]);
			map.put(topSplits[x], new HashMap<String,Long>());
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
