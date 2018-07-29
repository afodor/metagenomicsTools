package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;

public class MergeQiimeToMeta
{
	
	public static void main(String[] args) throws Exception
	{
		File relativeAbundnaceFile = new File(ConfigReader.getLauraDir() + File.separator + 
				"IER_Project" + File.separator + "taxa_summary" + File.separator + 
					"otu_table_no_singletons_no_chimeras_L6.txt");
		
		List<String> sampleIds = getSampleIDs(relativeAbundnaceFile);
		HashMap<String, List<Double>> taxaMap = getAsMap(relativeAbundnaceFile);
		
		writeMergedMeta(sampleIds, taxaMap);
		
	}
	
	private static void writeMergedMeta(List<String> sampleIds , HashMap<String, List<Double>> taxaMap ) 
		throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator +
				"MergedMetaAF.txt")));
		
		List<String> taxa =new ArrayList<>();
		
		for(String s: taxaMap.keySet())
			taxa.add(s);
		
		writer.write("sampleID\ttreatmentGroup\ttimePoint\ttumorVolume\ttumorWeight\tcageNumber");

		for(String s : taxa)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int x=0; x < sampleIds.size(); x++)
		{
			String key = sampleIds.get(x);
			System.out.println(key);
			writer.write(key);
			MetadataParserFileLine mpfl = metaMap.get(key);
			
			writer.write("\t" + mpfl.getTreatmentGroup() + "\t" + mpfl.getTimePoint() + "\t" + 
						mpfl.getTumorVolume() + "\t" + mpfl.getTumorWeight() );
			
			if( mpfl.getCageNumber() == null)
				writer.write("\tNA");
			else 
				writer.write("\t" + mpfl.getCageNumber());
			
			writer.write("\t" + taxaMap.get(key).get(x));
			
			writer.write("\n");
		}
		
		writer.flush();
		writer.close();
	}
	
	
	// outer key is taxa
	private static HashMap<String, List<Double>> getAsMap(File file) throws Exception
	{
		HashMap<String, List<Double>> map =new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits= s.split("\t");
			
			String key = new String(splits[0]);
			
			if(map.containsKey(key))
				throw new Exception("No");
			
			List<Double> list= new ArrayList<>();
			
			map.put(key,list );
			
			for( int x=1; x < splits.length; x++)
			{
				list.add(Double.parseDouble(splits[x]));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	
	private static List<String> getSampleIDs(File inFile) throws Exception
	{
		List<String> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] firstLine = reader.readLine().split("\t");
		
		for( int x=1; x < firstLine.length; x++)
			list.add(firstLine[x]);
		
		reader.close();
		
		return list;
	}
}
