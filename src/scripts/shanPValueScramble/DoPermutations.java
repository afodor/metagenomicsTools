package scripts.shanPValueScramble;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import utils.ConfigReader;

public class DoPermutations
{
	public static final int NUM_PROVINCES =15;
	public static final double P_VALUE_THRESHOLD =0.05;
	public static final int NUM_PERMUTATIONS = 1000;
	
	// just a constant, arbitrary seed
	private static final Random RANDOM = new Random(3241121);
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getChinaMay2017Dir() + File.separator + 
					"shanPValues" + File.separator + "China_anova_t1.csv");
		
		HashMap<String, List<Double>> map =getInitialParse(inFile);
		
		for(String s : map.keySet())
		{
			System.out.println(s);
		}
		
		List<List<Integer>> resultsList = new ArrayList<>();
		resultsList.add(getNumAtThreshold(map));
		
		for( int x=0; x < NUM_PERMUTATIONS; x++)
		{

			permute(map);
			
			if( x==0 )
				writeAPermutation(map);
			
			resultsList.add(getNumAtThreshold(map));
			System.out.println("Permuation " + x);
		}
		
		writeResults(map, resultsList);
	}
	
	private static void writeAPermutation( HashMap<String, List<Double>> map) throws Exception
	{
		File outFile = new File(ConfigReader.getChinaMay2017Dir() + File.separator + 
				"shanPValues" + File.separator + "anAnovaPermutation.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("key");
		
		for( int x=1; x <= NUM_PROVINCES; x++)
			writer.write("\t" + x);
		
		writer.write("\n");
		
		for(String s : map.keySet())
		{
			writer.write(s);
			
			List<Double> list =map.get(s);
			
			if( list.size() != NUM_PROVINCES)
				throw new Exception("No");
			
			for( Double d : list)
			writer.write("\t" + d);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static void writeResults(HashMap<String, List<Double>> map,List<List<Integer>> resultsList ) throws Exception
	{
		File outFile = new File(ConfigReader.getChinaMay2017Dir() + File.separator + 
				"shanPValues" + File.separator + "permutedBelowThreshold_China_anova_t1.txt");
		

		if( resultsList.size() != NUM_PERMUTATIONS + 1)
			throw new Exception("No " + resultsList.size() + " " +  (NUM_PERMUTATIONS + 1));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("test\tunpermuted");
		
		for( int x=0;x < NUM_PERMUTATIONS; x++)
			writer.write("\tperm" + x );
		
		writer.write("\n");
		
		int index =0;
		
		for(String s : map.keySet())
		{
			writer.write(s);
			

			for( int x=0; x < resultsList.size(); x++)
			{
				List<Integer> innerList = resultsList.get(x);
				writer.write("\t" + innerList.get(index));				
			}
			
			writer.write("\n");
			
			
			index++;
		}
		
		if( index != map.size())
			throw new Exception("No");
		
		writer.flush();  writer.close();
	
	}
	
	private static void permute(HashMap<String, List<Double>> map ) throws Exception
	{
		
		for( int x=0; x <NUM_PROVINCES; x++)
		{
			List<Double> oldList = new ArrayList<>();
			
			for( String s : map.keySet())
				oldList.add(map.get(s).get(x));
			
			Collections.shuffle(oldList, RANDOM);
			
			int index =0;
			
			for( String s : map.keySet())
			{
				map.get(s).set(x, oldList.get(index));
				index++;
			}
		
			if( index != oldList.size())
				throw new Exception("No " + index + " " + oldList.size());
			
		}
	}
	
	private static List<Integer> getNumAtThreshold(HashMap<String, List<Double>> map ) 
		throws Exception
	{
		List<Integer> list = new ArrayList<>();
	
		for(String s : map.keySet())
		{
			List<Double> innerList =map.get(s);
			
			if( innerList.size() != NUM_PROVINCES)
				throw new Exception("NO");
			
			int sum=0;
			
			for(Double d : innerList)
				if ( d <= P_VALUE_THRESHOLD)
					sum++;
			
			list.add(sum);
		}
		
		return list;
	}
	
	private static HashMap<String, List<Double>> getInitialParse(File inFile) throws Exception
	{
		HashMap<String, List<Double>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s !=null; s= reader.readLine())
		{
			String[] splits = s.split(",");
			
			if( splits.length != NUM_PROVINCES + 1)
				throw new Exception("NO " + splits.length);
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("NO");
			
			List<Double> list = new ArrayList<>();
			
			for( int x=1; x < NUM_PROVINCES + 1; x++)
				list.add(Double.parseDouble(splits[x]));
			
			map.put(key, list);
		}
		
		reader.close();
		
		return map;
	}
}

