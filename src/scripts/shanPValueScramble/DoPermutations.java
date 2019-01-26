package scripts.shanPValueScramble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.processing.Filer;

import utils.ConfigReader;

public class DoPermutations
{
	public static final int NUM_PROVINCES =15;
	public static final double P_VALUE_THRESHOLD =0.05;
	
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
		
		permute(map);
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
