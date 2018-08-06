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
	
	// run CountFeatures first
	public static void main(String[] args) throws Exception
	{
		Map< String, Map<String,Map<String,Character>>> map = CountFeatures.getMap();
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"pivotedPositions.txt")));
		
		List<String> positionlist = getAllPositions(map);
		
		for(String s : positionlist)
		{
			System.out.println(s);
		}
		
		writer.write("id\tclassificaiton");
		
		for( String s : positionlist)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String classificaiton : map.keySet())
		{
			Map<String,Map<String,Character>> map1=  map.get(classificaiton);
			
			for(String proteinID : map1.keySet())
			{
				writer.write(proteinID + "\t" + classificaiton);
				
				Map<String,Character> map2 =map1.get(proteinID);
				
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
		}
		
		writer.flush();  writer.close();
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
