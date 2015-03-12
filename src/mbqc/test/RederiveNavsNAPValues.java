package mbqc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class RederiveNavsNAPValues
{
	public static void main(String[] args) throws Exception
	{
		
	}
	
	
	
	// key is bioinformaticsID_sequencinglab_extractionlab
	// value is map with mbqcID key and list of values
	private static HashMap<String, HashMap<String, List<Double>>> getMap() throws Exception
	{
		HashMap<String, HashMap<String, List<Double>>> map = new HashMap<String, HashMap<String, List<Double>>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + "raw_design_matrix.txt")));
		
		for(String s = reader.readLine() ; s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String bioinformaticsID = new StringTokenizer( splits[0], ".").nextToken();
			String extractionLab = splits[1];
			String sequencingLab = splits[2];
			
			String key = bioinformaticsID  + "_" + sequencingLab + "_" + extractionLab;
			
			HashMap<String, List<Double>> innerMap = map.get(key);
			
			if(innerMap == null)
			{
				innerMap = new HashMap<String, List<Double>>();
				map.put(key, innerMap);
			}
			
			String mbqcID = splits[3];
			
			List<Double> innerList = innerMap.get(mbqcID);
			
			if( innerList == null)
			{
			}
		}
		
		
		reader.close();
		
		return map;
	}
}
