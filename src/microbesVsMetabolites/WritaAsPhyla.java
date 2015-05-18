package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class WritaAsPhyla
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"toFodor05182015_PL_shucha_bothLanes_wTaxa.txt")));
		
		List<String> sampleNames = new ArrayList<String>();
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length - 1; x++)
			sampleNames.add(splits[x]);
		
		HashMap<String, List<Double>> map = new HashMap<String, List<Double>>();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			splits = s.split("\t");
			List<Double> thisList = new ArrayList<Double>();
			
			for( int x=1; x < splits.length -1; x++)
				thisList.add(Double.parseDouble(splits[x]));
			
			String taxaString = splits[splits.length-1];
			System.out.println(taxaString);
			
			if( ! taxaString.equals("Unassigned") && ! taxaString.equals("k__Bacteria") )
				taxaString = taxaString.split(";")[1].replace("p__", "");
			
			List<Double> oldList = map.get(taxaString);
			
			if( oldList != null)
			{
				if( oldList.size() != thisList.size() ) 
					throw new Exception("Logic error");
				
				for( int x=0; x < oldList.size(); x++)
				{
					double newSum = oldList.get(x) + thisList.get(x);
					thisList.set(x, newSum);
				}
			}
			
			map.put(taxaString, thisList);
 		}
		
		writeResults(sampleNames, map);
	}
	
	private static void writeResults(List<String> headers, HashMap<String, List<Double>> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"phylaAsRows.txt")));
		
		writer.write("phyla");
		
		for(String s : headers )
		{
			writer.write("\t" + s);
		}
		
		writer.write("\n");
		
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		
		for(String s : list)
		{
			writer.write(s);
			
			List<Double> counts = map.get(s);
			
			for( Double i : counts)
				writer.write("\t" + i);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
