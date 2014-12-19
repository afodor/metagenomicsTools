package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class PivotRawDesignMatrix
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, HashMap<String, Double>>> map = getMap();
		System.out.println("Got map " + map.size());
	}
	
	private static void writeResults( HashMap<String, HashMap<String, HashMap<String, Double>>> map )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getMbqcDir() + 
				File.separator + "af_out" + File.separator + "rawDesignPivoted.txt"));
		
		//writer.write("ID\textraction_wetlab\tsequencing_wetlab\tMBQC.ID\ttaxaName\ttaxaLevel\tspecimenType\n");
		

		writer.flush();  writer.close();
	}
	
	/*
	 * The outer key is MBQCID
	 * the middle key is taxa
	 * the inner key is bioinformatics tagged sampleID
	 */
	private static HashMap<String, HashMap<String, HashMap<String, Double>>> getMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		HashMap<String, HashMap<String, HashMap<String, Double>>>  map =
				new HashMap<String, HashMap<String,HashMap<String,Double>>>();
		
		List<String> taxa = new ArrayList<String>();
		String[] headerSplits = reader.readLine().split("\t");
		
		int x=4;
		
		while(headerSplits[x].startsWith("k__"))
		{
			taxa.add(getPhyla(headerSplits[x]));
			x++;
		}
		
		x+=2;
		
		for(String s = reader.readLine(); s!= null; s= reader.readLine())
		{
			//System.out.println(s);
			String[] splits = s.split("\t");
			String mbqcID = splits[3];
			
			HashMap<String, HashMap<String, Double>> middleMap = map.get(mbqcID);
			
			if( middleMap == null)
			{
				middleMap = new HashMap<String, HashMap<String,Double>>();
				map.put(mbqcID, middleMap);
			}
			
			HashMap<String, Double> innerMap = middleMap.get(splits[0]);
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Double>();
				middleMap.put(splits[0], innerMap);
			}

			for(int y=0; y < taxa.size(); y++)
			{
				String thisTaxa = taxa.get(y);
				if( innerMap.containsKey(thisTaxa) )
					throw new Exception("Unexpected duplicate " + s);
				
				if( ! splits[y+4].equals("NA"))
					innerMap.put(thisTaxa, Double.parseDouble(splits[y+4]));
			}
		} 
		
		reader.close();
		return map;
	}
	
	private static String getPhyla(String s )
	{
		int index = s.indexOf("p__");
		return s.substring(index + 2).replaceAll("_","");
	}
}
