package scripts.luthurJan2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeAtLevel
{
	public static void main(String[] args) throws Exception
	{
		for(String level : SequenceToTaxaParser.TAXA_LEVELS )
		{
			System.out.println(level);
			HashMap<String, HashMap<String,Integer>> map = getMap(level);
			File unloggedFile = writeResults(map, level);
			
			OtuWrapper wrapper = new OtuWrapper(unloggedFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getLuthurJan2019Dir() + File.separator + 
					"data" + File.separator + level + "_logNorm.txt");
		}
	}
	
	private static File writeResults( HashMap<String, HashMap<String,Integer>> map , String level ) throws Exception
	{
		File outFile = new File(ConfigReader.getLuthurJan2019Dir() + File.separator + 
				"data" + File.separator + level + "asColumns.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		HashSet<String> taxaSet = new HashSet<>();
		
		for(String s : map.keySet())
			taxaSet.addAll(map.get(s).keySet());
		
		List<String> taxaList = new ArrayList<>(taxaSet);
		Collections.sort(taxaList);
		
		writer.write("sample");
		
		for(String s : taxaList)
			writer.write("\t" + s);
		writer.write("\n");
		
		for(String s : map.keySet())
		{
			writer.write(s);
			
			for(String s2 : taxaList)
			{
				Integer aVal = map.get(s).get(s2);
				if( aVal == null)
					aVal =0;
				
				writer.write("\t" + aVal);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		return outFile;
	}
	
	private static HashMap<String, HashMap<String,Integer>> getMap(String level) throws Exception
	{
		HashMap<String, SequenceToTaxaParser> taxaMap = SequenceToTaxaParser.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir() + File.separator + 
				"data" + File.separator + "luther.sequence.table.nochim.SILVA.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
		{
			SequenceToTaxaParser stp= taxaMap.get(topSplits[x]);
			
			if( stp == null)
				throw new Exception("Could not find " + topSplits[x]);
			
			topSplits[x] = stp.getForALevel(level).replaceAll("\"", "");
		}
		
		// otuer key is sample; inner key is taxa
		HashMap<String, HashMap<String,Integer>> map = new LinkedHashMap<>();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("Parsing error");
			
			String key = splits[0].replaceAll("\"", "");
			if( map.containsKey(key))
				throw new Exception("Duplicate");
			
			HashMap<String, Integer> innerMap = new HashMap<>();
			map.put(key, innerMap);
			
			for( int x=1; x < splits.length; x++)
			{
				Integer sVal = Integer.parseInt(splits[x]);
				
				if( sVal > 0)
				{
					Integer oldVal = innerMap.get(topSplits[x]);
					
					if(oldVal == null)
						oldVal =0;
					
					innerMap.put(topSplits[x], sVal + oldVal);
				}
			}
			
		}
		
		reader.close();
		return map;
	}
}
