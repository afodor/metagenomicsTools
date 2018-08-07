package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WritePivotedFeatureTable
{
	public static void main(String[] args) throws Exception
	{
		Map< String, Map<String,Map<String,Character>>> map = CountFeatures.getMap();
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"pivotedPositions.txt")));
		
		List<String> positionlist = getAllPositions(map);
		
		writer.write("id\tclassificaiton");
		
		for( String s : positionlist)
			writer.write("\t" + s.replaceAll("\"",""));
		
		writer.write("\n");
		
		List<Holder> protIDs = getAllProteins(map);
	
		for(Holder h : protIDs)
		{
			writer.write(h.protName.replaceAll("\"","") + "\t" + h.classificaiton.replaceAll("\"",""));
				
			Map<String,Map<String,Character>>  map1 =map.get(h.classificaiton);
			Map<String,Character> map2 = map1.get(h.protName);
			
			for(String position : positionlist)
			{
				Character c = map2.get(position);
					
				if( c== null)
				{
					writer.write("\tNA");
				}
				else
				{
						c =Character.toUpperCase(c);
						writer.write("\t" + c);
				}
			}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String protName;
		String classificaiton;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return this.classificaiton.compareTo(arg0.classificaiton);
		}
	}
	
	
	private static List<Holder> getAllProteins(Map< String, Map<String,Map<String,Character>>> map ) throws Exception
	{
		List<Holder> list =new ArrayList<>();
		
		for(String classificaiton : map.keySet())
		{
			 Map<String,Map<String,Character>> map1 = map.get(classificaiton);
			 
			 for(String id : map1.keySet())
			 {
				 Holder h = new Holder();
				 h.classificaiton = classificaiton;
				 h.protName = id;
				 list.add(h);
			 }
		}
		
		Collections.sort(list);
		return list;
	}
	
	public static List<String> getAllPositions( Map< String, Map<String,Map<String,Character>>> map )
		throws Exception
	{
		List<String> list = new ArrayList<>();
		HashSet<String> set = new LinkedHashSet();
		
		for( Map<String,Map<String,Character>>  map1 : map.values())
		{
			for(Map<String,Character> map2 : map1.values())
			{
				set.addAll(map2.keySet());
			}
		}
		
		list.addAll(set);
		return list;
	}
}
