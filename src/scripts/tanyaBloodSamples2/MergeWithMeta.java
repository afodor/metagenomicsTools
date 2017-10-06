package scripts.tanyaBloodSamples2;

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

public class MergeWithMeta
{
	private final static String[] TAXA_LEVELS = 
		{"Phylum", "Class", "Order", "Family", "Genus", "Species", "OTU" };
	
	public static void main(String[] args) throws Exception
	{
		for(String taxa :TAXA_LEVELS)
		{
			HashMap<String, HashMap<String,Double>>  map = new HashMap<>();
			HashMap<String, HashMap<String,String>> metaMap = MetadataParser.getMetaMap();
			
			File metaFile = new File(ConfigReader.getTanyaBloodDir2() + File.separator + 
						"Blood " + taxa +" META 18July16.txt");
			File proFile = new File(ConfigReader.getTanyaBloodDir2() + File.separator + 
						"Blood " + taxa +" PRO 18July16.txt");
			
			parseAFile(metaFile, map);
			parseAFile(proFile , map);
			
			//for(String s : map.keySet())
				//System.out.println(s +  " " + map.get(s));
			
			File outFile = new File(
					ConfigReader.getTanyaBloodDir2() + File.separator + 
					"Blood_" + taxa +"18July16MergedWithMeta.txt");
			
			writeMetaFile(map, metaMap, outFile);
		}
	}
	
	private static void writeMetaFile(HashMap<String, HashMap<String,Double>> map,
			HashMap<String, HashMap<String,String>> metaMap, File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		List<String> metaList =new ArrayList<>(MetadataParser.getIncluded());
		
		writer.write("ID\tisWater");
		
		for(String s : metaList)
			writer.write("\t" + s);
		
		HashSet<String> taxa =new HashSet<>();
		
		for(HashMap<String,Double> m :map.values())
			taxa.addAll(m.keySet());
		
		List<String> taxaList = new ArrayList<String>(taxa);
		Collections.sort(taxaList);
		
		for(String s : taxaList)
			writer.write("\t" + s);
		
		writer.write("\n");

		for(String id : map.keySet())
		{
			writer.write(id);
		
			if( id.startsWith("Con"))
			{
				writer.write("\ttrue");
				
				for( int x=0; x < metaList.size(); x++)
					writer.write("\tNA");
			}
			else
			{
				writer.write("\tfalse");
				
				HashMap<String, String> innerMeta = metaMap.get(id);
				
				if( innerMeta == null)
					System.out.println("Could not find " + id);
				
				for(String s : metaList)
				{
					writer.write("\t" + innerMeta.get(s));
					
				}
			}
			
				
			HashMap<String, Double> innerTaxa = map.get(id);
			
			for(String s : taxaList)
			{
				Double val = innerTaxa.get(s);
				
				if( val == null)
					val = 0.0;
				
				writer.write("\t" + val);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static void parseAFile(File f,HashMap<String, HashMap<String,Double>>  map) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		String[] topSplits = reader.readLine().replaceAll("/", "_").replaceAll("-", "").split("\t");
		
		for(String s= reader.readLine(); s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != topSplits.length)
				throw new Exception("No");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			HashMap<String,Double> innerMap = new HashMap<>();
			
			map.put(key, innerMap);
			
			for( int x=1; x< topSplits.length; x++)
			{
				String taxa = topSplits[x];
				
				if(innerMap.containsKey(taxa))
					throw new Exception("No");
				
				innerMap.put(taxa, Double.parseDouble(splits[x]));
			}
		}
		
		reader.close();
	}
}
