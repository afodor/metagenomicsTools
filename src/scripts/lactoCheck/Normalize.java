package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;

public class Normalize
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getTotalCounts();
		
		for(String s : map.keySet())
			System.out.println( s + " " + map.get(s));
		
		double average = 0;
		
		for(Double d : map.values())
			average += d.doubleValue();
		
		average = average / map.size();
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(new File(
			ConfigReader.getLactoCheckDir() + File.separator + 
				"Lacto_InersLogNormPlusRunID.tsv")));
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
				"Lacto_InersPlusRunID.tsv")));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits=  s.split("\t");
			
			writer.write(splits[0]);
			
			for( int x=1;x <= 7; x++)
				writer.write("\t" + splits[x]);
			
			double sequenceCount = map.get(splits[3].replaceAll("\"", ""));
			
			for( int x=8; x < splits.length - 4; x++)
			{
				Double val = Double.parseDouble(splits[x]);
				
				double newVal = Math.log10( val / sequenceCount * average + 1 );
				writer.write("\t" + newVal);
			}
			
			for(int x = splits.length -4; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
				
		reader.close();
		writer.flush();  writer.close();
		
	}
	
	static HashMap<String, Double> getTotalCounts() throws Exception
	{
		List<String> names = new ArrayList<String>();
		List<Double> counts = new ArrayList<Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"gaQiimeClosedRef.txt")));
		
		reader.readLine();
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length; x++)
		{
			names.add(splits[x]);
			counts.add(0.0);
		}
		
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			splits =s.split("\t");
			
			for( int x=1; x < splits.length-1; x++)
				counts.set(x-1, counts.get(x-1) + Double.parseDouble(splits[x]));
		}
		
		reader.close();

		HashMap<String, Double> map = new LinkedHashMap<String,Double>();
		
		for(int x=0;x  < names.size();x++)
		{
			if( map.containsKey(names.get(x)))
				throw new Exception("No");
			
			map.put(names.get(x), counts.get(x));
		}
		
		return map;
	}
	
}
