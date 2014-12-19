package mbqc;

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

import utils.ConfigReader;

public class PivotRawDesignMatrix
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, HashMap<String, Double>>> map = getMap();
		System.out.println("Got map " + map.size());
		
		/*
		for(String s : map.keySet())
		{
			HashMap<String, HashMap<String, Double>> middleMap = map.get(s);
			
			for(String s2 : middleMap.keySet())
			{
				System.out.println("\t\t" + s2);
				
				for(String s3 : middleMap.get(s2).keySet())
					System.out.println("\t\t\t\t" + s3 + " " + middleMap.get(s2).get(s3));
			}
				
		}
		*/
		
		writeResults(map, "jpetrosino.");
	}
	
	private static List<String> getSampleIDs(HashMap<String, HashMap<String, HashMap<String, Double>>> map, 
			String prefix ) throws Exception
	{
		List<String> mbqcCats = new ArrayList<String>(map.keySet());
		
		HashSet<String> sampleIds = new HashSet<String>();
		
		for(String s : mbqcCats)
		{
			HashMap<String, HashMap<String, Double>> middleMap = map.get(s);
			
			for(String s2 : middleMap.keySet())
				if( s2.startsWith(prefix))
					sampleIds.add(s2);
		}
		
		List<String> sampleIdList = new ArrayList<String>(sampleIds);
		Collections.sort(sampleIdList);
		return sampleIdList;
	}
	
	private static List<String> getTaxa(HashMap<String, HashMap<String, HashMap<String, Double>>> map, 
			String prefix, List<String> samples ) throws Exception
	{
		List<String> mbqcCats = new ArrayList<String>(map.keySet());
		
		HashSet<String> taxaIds = new HashSet<String>();
		
		for(String s: mbqcCats)
		{
			HashMap<String, HashMap<String, Double>> middleMap = map.get(s);
			
			for(String s2 : samples)
			{
				HashMap<String, Double> innerMap = middleMap.get(s2);
				
				if( innerMap != null)
				{
					for(String taxa : innerMap.keySet())
					{
						if( innerMap.get(taxa) > 0 )
							taxaIds.add(taxa);
					}
				}
			}
		}
		List<String> list = new ArrayList<String>(taxaIds);
		Collections.sort(list);
		return list;
	}
	
	
 	private static void writeResults( HashMap<String, HashMap<String, HashMap<String, Double>>> map, String prefix )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getMbqcDir() + 
				File.separator + "af_out" + File.separator + "rawDesignPivoted_" + prefix + ".txt"));
		
		List<String> sampleIds = getSampleIDs(map, prefix);
		
		writer.write("MBQC.ID\ttaxaName");
		
		for(String s : sampleIds)
			writer.write("\t" + s.replaceAll(prefix, ""));
		
		writer.write("\n");
		
		List<String> taxa =getTaxa(map, prefix, sampleIds);
		
		for(String mbqc : map.keySet())
		{
			for(String taxaId : taxa)
			{
				writer.write(mbqc + "\t" + taxaId );
				
				for(String s : sampleIds)
				{
					HashMap<String, HashMap<String, Double>> middleMap = map.get(mbqc);
					
					HashMap<String, Double> innerMap = middleMap.get(s);
					
					Double val = null;
					
					if (innerMap != null)
						val = innerMap.get(taxaId);
					
					if( val == null)
						writer.write("\t");
					else
						writer.write("\t" + val);
				}
				
				writer.write("\n");
			}
		}

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
				new LinkedHashMap<String, HashMap<String,HashMap<String,Double>>>();
		
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
