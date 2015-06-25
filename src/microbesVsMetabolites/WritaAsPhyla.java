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
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WritaAsPhyla
{
	public static final String[] LEVELS = { "p" , "c", "o", "f", "g", "otu" };
	
	public static void main(String[] args) throws Exception
	{
		for(String s : LEVELS)
			writeALevel(s);
	}
	
	public static void writeALevel(String level) throws Exception
	{
		System.out.println(level);
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
			
			if( level.equals("otu"))
				taxaString = splits[0];
			else
				taxaString = getAssignment(taxaString, level);
			
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
		
		writeResults(sampleNames, map, level);
	}
	
	private static void writeResults(List<String> headers, HashMap<String, List<Double>> map, String level ) 
				throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + level + "AsRows.txt")));
		
		writer.write(level);
		
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
	
	private static String getAssignment(String  s, String level) throws Exception
	{
		if( s.equals("Unassigned"))
			return s;
		
		StringTokenizer sToken = new StringTokenizer(s, ";");

		String start = level + "__";
		
		
		while(sToken.hasMoreTokens())
		{
			String nextToken = sToken.nextToken().trim();
			
			if( nextToken.startsWith(start))
			{
				String val = nextToken.replace(start, "").replace("[", "").replace("]", "");
				
				if( val.length() == 0 )
					val = "Unassigned";
				
				return val;
			}
				
		}
		
		return "Unassigned";
	}
}
