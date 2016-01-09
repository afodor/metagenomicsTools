package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class MergePcaToMappedOrthologs
{
	private static final int NUM_COMPONENTS = 10;
	
	private static HashMap<String, List<Double>> getFirstAxes() throws Exception
	{
		HashMap<String, List<Double>> map = new HashMap<String, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator +
			"notTransposedPCA.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].replaceAll("\"", "").replace("Line_", "");
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			List<Double> list = new ArrayList<Double>();
			
			map.put(key, list);
			
			for( int x=0; x < NUM_COMPONENTS; x++)
				list.add(Double.parseDouble(splits[x+1]));
		}
					
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<Double>> pcaMap = getFirstAxes();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
					"chs_11_plus_cards.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
						"chs_11_plus_cards_plusPCA.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0]);
		
		for( int x=0; x < NUM_COMPONENTS; x++)
			writer.write("\tPCA" + (x+1));
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for( String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			List<Double> vals = pcaMap.get(splits[0]);
			
			writer.write(splits[0]);
			
			if ( vals == null)
			{
				for( int x=0; x < NUM_COMPONENTS; x++)
					writer.write("\tNA");
			}
			else
			{
				for( int x=0; x < NUM_COMPONENTS; x++)
					writer.write("\t" + vals.get(x));
			}
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
			
		reader.close();
	}
}
