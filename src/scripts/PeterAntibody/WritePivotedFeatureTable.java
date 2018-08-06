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
