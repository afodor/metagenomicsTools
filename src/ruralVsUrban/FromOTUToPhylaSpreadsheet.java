package ruralVsUrban;

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

import jdk.jfr.events.FileWriteEvent;
import parsers.OtuWrapper;
import sun.util.locale.StringTokenIterator;
import utils.ConfigReader;

public class FromOTUToPhylaSpreadsheet
{
	public static void main(String[] args) throws Exception
	{	
		BufferedReader reader = new BufferedReader( new FileReader( new File(
				ConfigReader.getChinaDir() + File.separator + 
				"UNC_Penny_data10292014_onlyTaxa.txt")));
		
		String[] samples = reader.readLine().split("\t");
		
		HashMap<String, List<Integer>> phylaCounts = getPhlyaCounts(reader);
		
		reader.close();
		
		BufferedWriter writer = new BufferedWriter( new FileWriter(new File(ConfigReader.getChinaDir() +
				File.separator + "phylaVsSamples.txt")));
		
		List<String> phyla = new ArrayList<>(phylaCounts.keySet());
		Collections.sort(phyla);
		
		writer.write("taxa");
		
		for(String s : phyla)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int y=1 ; y < samples.length; y++)
		{
			writer.write(samples[y]);
			for( int x=0; x < phyla.size(); x++)
			{
				List<Integer> innerList = phylaCounts.get(phyla.get(x));
				if( innerList.size() != samples.length-1)
					throw new Exception("No");
				writer.write("\t" + innerList.get(y-1));
			}
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		
	}
	
	private static String getPhylaOrNull(String s ) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, ";");
		
		if( sToken.countTokens() < 2)
			return null;
		
		sToken.nextToken();
		
		String phylaToken = sToken.nextToken().trim();
		System.out.println(s);
		
		if( ! phylaToken.startsWith("p__"))
			throw new Exception("No");
		
		return phylaToken.replace("p__", "");
	}
	
	private static HashMap<String, List<Integer>> getPhlyaCounts(BufferedReader reader) throws Exception
	{
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		
		
		for(String s= reader.readLine(); s != null;  s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String key = getPhylaOrNull(sToken.nextToken());
			
			if( key != null)
			{
				List<Integer> newList = new ArrayList<Integer>();
				
				while( sToken.hasMoreTokens())
					newList.add(new Integer(sToken.nextToken()));
				
				List<Integer> oldList = map.get(key);
				
				if( oldList != null)
				{
					if( oldList.size() != newList.size())
						throw new Exception("No");
					
					for( int x=0; x < oldList.size(); x++)
						newList.set(x, newList.get(x) + oldList.get(x));
				}
				
				map.put(key, newList);
			}
		}
		
		return map;
	}
}
