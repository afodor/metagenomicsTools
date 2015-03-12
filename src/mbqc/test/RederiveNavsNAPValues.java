package mbqc.test;

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
import java.util.StringTokenizer;

import utils.ConfigReader;

public class RederiveNavsNAPValues
{
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, HashMap<String, List<List<Double>>>> map = getMap();
		 //for(String s : map.keySet())
		//	 System.out.println(s +  " "+ map.get(s).size());
		 
		 writeTestCompareFile(map);
	}
	
	private static void writeTestCompareFile(HashMap<String, HashMap<String, List<List<Double>>>> map) throws Exception
	{
		List<String> taxaNames = getTaxaNames();
		List<String> mbqcNames = getMBQCIDs(map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
			ConfigReader.getMbqcDir() + File.separator + "af_out" + File.separator + 
							"testValsForNAvsNonNA.txt"));
		
		writer.write("bioinformaticsID\tsequencingLab\textractionLab\ttaxa\tnaVals\textractionVals\tfoldChange\n");
		
		List<String> toMatch = new ArrayList<String>();
		List<String> naMatched = new ArrayList<String>();
		
		for(String s : map.keySet())
			if( ! s.endsWith("NA"))
			{
				String matched = getMatched(s, map);
				
				if( matched != null)
				{
					toMatch.add(s);
					naMatched.add(matched);
				}
			}
			
		for(int x=0; x < toMatch.size(); x++)
		{
			//System.out.println(toMatch);
			String[] splits = toMatch.get(x).split("_");
			
			for( int y=0; y < taxaNames.size(); y++)
			{
				writer.write(splits[0] + "\t");
				writer.write(splits[1] + "\t");
				writer.write(splits[2] + "\t");
				writer.write(taxaNames.get(y) + "\t");
				
				HashMap<String, List<List<Double>>> extractionMap = map.get(toMatch.get(x));
				HashMap<String, List<List<Double>>> na_Map = map.get(naMatched.get(x));
				
				List<Double> exVals = new ArrayList<Double>();
				List<Double> naVals = new ArrayList<Double>();
				
				for( int z=0; z < mbqcNames.size();z++)
				{
					String mbqcID = mbqcNames.get(z);
					List<List<Double>> exList = extractionMap.get(mbqcID);
					List<List<Double>> naList = na_Map.get(mbqcID);
					
					if( exList != null && naList != null)
					{
						List<Double> exAvg = average(exList);
						List<Double> naAvg = average(naList);
						exVals.add(exAvg.get(y));
						naVals.add(naAvg.get(y));
					}
				}
				
				writer.write(exVals + "\t");
				writer.write(naVals + "\t");
				writer.write( foldChange(exVals, naVals) +  "\n");	
			}
		}
		
		writer.flush();  writer.close();
	}
	
	private static double foldChange(List<Double> list1, List<Double> list2)
		throws Exception
	{
		double sum1 = 0;
		double sum2 = 0;
		
		if( list1.size() != list2.size() )
			throw new Exception("No");
		
		for( int x=0; x < list1.size(); x++)
		{
			sum1 += list1.get(x);
			sum2 += list2.get(x);
		}
		
		return sum1 / sum2;
	}
	
	private static List<Double> average(List<List<Double>> inList) throws Exception
	{
		List<Double> returnList = new ArrayList<Double>();
		
		int firstListSize = inList.get(0).size();
		
		for( int x=0; x < firstListSize; x++ )
		{
			double sum =0;
			int n=0;
			for( int y=0; y < inList.size(); y++)
			{
				List<Double> innerList = inList.get(y);
				
				if( innerList.size() != firstListSize)
					throw new Exception("No");
				
				sum += innerList.get(x);
				n++;
				
			}
			
			returnList.add(sum /n);
		}
		
		return returnList;
	}
	
	private static String getMatched(String toMatch, HashMap<String, HashMap<String, List<List<Double>>>> map)
		throws Exception
	{
		String target = toMatch.split("_")[0] + "_" +  toMatch.split("_")[1] + "_NA";
		
		for(String s : map.keySet())
		{
			if( s.equals(target))
				return target;
		}
		
		return null;
	}
	
	private static List<String> getTaxaNames() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + "dropbox" + File.separator + 
					"raw_design_matrix.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		List<String> list = new ArrayList<String>();
		
		for( int x=0; x < 42; x++)
			list.add(  splits[x+4]);
			
		return list;
	}
	
	private static List<String> getMBQCIDs(HashMap<String, HashMap<String, List<List<Double>>>> map) 
		throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for(String s : map.keySet())
		{
			HashMap<String, List<List<Double>>> iMap = map.get(s);
			
			for(String s1 : iMap.keySet())
				set.add(s1);
		}
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		return list;
	}
	
	// key is bioinformaticsID_sequencinglab_extractionlab
	// value is map with mbqcID key and list of values
	// the outer list is one replicate for each taxa
	// the inner list is the taxa
	private static HashMap<String, HashMap<String, List<List<Double>>>> getMap() throws Exception
	{
		HashMap<String, HashMap<String, List<List<Double>>>> map = 
				new HashMap<String, HashMap<String, List<List<Double>>>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + "dropbox" + File.separator + 
					"raw_design_matrix.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s =reader.readLine())
		{
			//System.out.println(s);
			String[] splits = s.split("\t");
			
			if( ! splits[4].equals("NA"))
			{
				String bioinformaticsID = new StringTokenizer( splits[0], ".").nextToken();
				String extractionLab = splits[1];
				String sequencingLab = splits[2];
				
				String key = bioinformaticsID  + "_" + sequencingLab + "_" + extractionLab;
				
				HashMap<String, List<List<Double>>> innerMap = map.get(key);
				
				if(innerMap == null)
				{
					innerMap = new HashMap<String, List<List<Double>>>();
					map.put(key, innerMap);
				}
				
				String mbqcID = splits[3];
				
				List<List<Double>> outerList = innerMap.get(mbqcID);
				
				if( outerList== null)
				{
					outerList = new ArrayList<List<Double>>();
					innerMap.put(mbqcID, outerList);
				}
				
				List<Double> innerList = new ArrayList<Double>();
				outerList.add(innerList);
				
				for( int x=0; x < 42; x++)
					innerList.add(Double.parseDouble(splits[x + 4]));
			}
		}
		
		
		reader.close();
		
		return map;
	}
}
