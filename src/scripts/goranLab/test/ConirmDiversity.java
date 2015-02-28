package scripts.goranLab.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class ConirmDiversity
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getOTUDiversity();
		
		for(String s  : map.keySet() )
		{
			System.out.println( s + " " + map.get(s));
		}
	}
	
	private static HashMap<String, Double> getOTUDiversity() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranTrialDir() + File.separator + 
				"DATA Serum Solar Sano 02-20-2015.txt"
				)));
		
		List<List<Integer>> counts = new ArrayList<List<Integer>>();
		List<String> names = new ArrayList<String>();
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=3; x < firstSplits.length; x++)
		{
			if( firstSplits[x].trim().length() == 0)
				throw new Exception("No");
			
			names.add(firstSplits[x].trim());
			counts.add(new ArrayList<Integer>());
		}
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			for( int x=3; x < splits.length; x++)
				counts.get(x-3).add(Integer.parseInt(splits[x]));
		}
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		for( int x=0; x < names.size(); x++)
		{
			List<Integer> list = counts.get(x);
			
			double sum = 0;
			
			for( Integer i : list)
				sum +=i;
			
			double pLogP = 0;
			
			for( Integer i : list)
				if( i > 0 )
					pLogP += (i/sum) * Math.log(i/sum);
			
			if(map.containsKey(names.get(x)))
				throw new Exception("No");
			
			map.put(names.get(x), -pLogP);
		}
		
		return map;
	}
}
