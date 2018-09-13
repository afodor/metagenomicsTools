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
import java.util.List;

import utils.ConfigReader;

public class WriteSymmetricalFileForR
{
	private static HashMap<String,Double> getDistanceMap(File inFile ) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		HashMap<String, Double> map = new HashMap<>();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key1 = splits[0] + "@" + splits[1];
			String key2 = splits[1] + "@" + splits[0];
			
			if( map.containsKey(key1) || map.containsKey(key2))
				throw new Exception("No");
			
			Double val = Double.parseDouble(splits[6]);
			
			map.put(new String(key1), val);
			map.put(new String(key2), val);
		}
		
		reader.close();
		
		return map;
	}
	
	// run CountFeatures first
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"pairwiseLC_" + CountFeatures.LC_THRESHOLD + ".txt");
		
		HashMap<String, Double> symmetricalMap =getDistanceMap(inFile);
		System.out.println("Got symmetrical map");
		
		HashSet<String> ids = new HashSet<>();
		
		for(String s : symmetricalMap.keySet())
		{
			String[] splits =s.split("@");
			
			if( splits[0] == null || splits[1] == null || splits.length !=2)
				throw new Exception("No");
			
			ids.add(splits[0]);
			ids.add(splits[1]);
		}
		
		List<String> list = new ArrayList<>(ids);
		Collections.sort(list);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"pairwiseLC_" + CountFeatures.LC_THRESHOLD + "ForR.txt")));
		
		for(int x=0; x < list.size(); x++)
		{
			writer.write(list.get(x));
			
			for( int y=0; y < list.size(); y++)
			{
				if(  y ==x )
					writer.write("\t1");
				else
					writer.write("\t" 
				+ symmetricalMap.get(list.get(x) +"@" + list.get(y)));
			}
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
	}
}
