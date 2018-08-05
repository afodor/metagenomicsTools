package scripts.laura.ierSleeveMerged;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utils.ConfigReader;

public class MergePaired
{
	public static void main(String[] args) throws Exception
	{
		// outer key is taxa
		// inner taxa is comparison
		HashMap<String, Map<String,Double>> map = new HashMap<>();
		
		addToMap(new File(ConfigReader.getLauraDir() + File.separator + "IER_Project" + 
						File.separator + 
						"pariedModelsBugs2ndTimePointOnly_IER_genus.txt"), map, "IER_");
		
		addToMap(new File(ConfigReader.getLauraDir() + 
				File.separator + "SleeveGastroProject" + File.separator + 
					"pariedModelsBugs2ndTimePointOnlygenus.txt"), map, "Sleeve_");
		
		writePivot(map);
		
	}
	
	private static void writePivot(HashMap<String, Map<String,Double>> map) throws Exception
	{
		List<String> compList = getComparisonList(map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				ConfigReader.getLauraDir() + File.separator + "ier_Sleeve_merged" + 
						File.separator + "pariedPValuesMerged.txt"));
		
		writer.write("id");
		
		for(String s : compList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String key : map.keySet())
		{
			writer.write(key);
			
			Map<String, Double> innerMap =map.get(key);
			
			for(String s : compList)
			{
				Double val = innerMap.get(s);
				
				if( val == null)
					writer.write("NA\t");
				else
					writer.write("\t" + val);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static List<String> getComparisonList(HashMap<String, Map<String,Double>> map)
	{
		HashSet<String> set = new HashSet<>();
		
		for(String s : map.keySet())
			set.addAll(map.get(s).keySet());
		
		List<String> list = new ArrayList<>(set);
		
		Collections.sort(list);
		
		return list;
	}
	
	private static void addToMap( File fileToParse,  HashMap<String, Map<String,Double>> map,
					String prefix) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			s =s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			String id = splits[0];
			Map<String,Double> innerMap = map.get(id);
			
			
			if( innerMap ==null)
			{
				innerMap = new HashMap<String,Double>();
				map.put(id, innerMap);
			}
			
			String innerKey = prefix + splits[3] + "_" + splits[4];
			
			if( innerMap.containsKey(innerKey))
				throw new Exception("No");
			
			innerMap.put(innerKey,Double.parseDouble(splits[2]));
		}
	}
}
