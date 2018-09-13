package scripts.PeterAntibody;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ConfigReader;

public class WriteFrequenciesToGraph
{
	public static void main(String[] args) throws Exception
	{
		Map< String, Map<String,Map<String,Character>>> map = CountFeatures.getMap();
		List<String> positions = WritePivotedFeatureTable.getAllPositions(map);
		List<String> classifications = new ArrayList<>( map.keySet());
		Collections.sort(classifications);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"frequenciesForGraphing.txt")));
		
		writer.write("classification\tposition\taa\tfrequency\tabsoluteNumber\tclassificationIndex\n");
		
		for(String position : positions)
		{
			Map<String, Map<Character,Integer>> classToPositioMap = getMapsForPosition(map, position);
			
			HashMap<String,Integer> countsForClass = new HashMap<>();
			
			for( String s : classToPositioMap.keySet() )
			{
				int totalCount=0;
				
				 Map<Character,Integer> map1 = classToPositioMap.get(s);
				for(Character c : map1.keySet())
				{
					totalCount += map1.get(c);
				}
				
				countsForClass.put(s,  totalCount);
			}
			
			for(int x=0; x < classifications.size(); x++)
			{
				String classification = classifications.get(x);
				
				if( countsForClass.get(classification) > 0 )
				{

					Map<Character,Integer> countMap = classToPositioMap.get(classification);
					
					for( Character c : CountFeatures.getAASet())
						if( Character.isUpperCase(c))
						{	
							writer.write(classification + "\t");
							writer.write(position + "\t");
							writer.write(c + "\t");
							writer.write( (countMap.get(c) / ((double)countsForClass.get(classification)) ) + "\t");
							writer.write( countMap.get(c) + "\t");
							writer.write((x+1) + "\n");
						}
				}
				
			}
		}
		
		writer.flush();  writer.close();
	}
	
	// outer key is classification
	// inner key is one of 20 amino acids
	private static Map<String, Map<Character,Integer>> getMapsForPosition(Map< String, Map<String,Map<String,Character>>> map,
										String position) throws Exception
	{
		Map<String, Map<Character,Integer>> returnMap = new HashMap<>();
		
		for(String s : map.keySet())
		{
			Map<Character,Integer> innerMap = new HashMap<>();
			
			for( Character c : CountFeatures.getAASet())
				innerMap.put(c, 0);
			
			Map<String,Map<String,Character>> map1 = map.get(s);
			
			for(String protID: map1.keySet())
			{
				Map<String,Character> map2 = map1.get(protID);
				
				Character c= map2.get(position);
				
				if( c != null)
				{
					Integer aVal = innerMap.get(c);
					aVal++;
					innerMap.put(c, aVal);
				}
			}
			
			returnMap.put(s, innerMap);
		}
			
		return returnMap;
	}
	
}
