package scripts.goranLab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import parsers.OtuWrapper;
import utils.ConfigReader;

public class CollapseToDifferentLevels
{
	public static final String[] TAXA = { "phyla", "class", "order", "family", "genus" };
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x <=5 ; x++)
		{
			System.out.println(TAXA[x-1]);
			HashMap<String, HashMap<String, Double>> map = getCountsMap(x);
			File aFile = writeCountsFile(map, x);
			
			OtuWrapper wrapper = new OtuWrapper(aFile);
			
			wrapper.writeLoggedDataWithTaxaAsColumns(new File(ConfigReader.getGoranTrialDir() + 
				File.separator + TAXA[x-1] + "fromOTUsAsColumnLogNorm.txt"));
		}
	}
	
	private static File writeCountsFile(HashMap<String, HashMap<String, Double>> map, int level)
		throws Exception
	{
		
		File outFile = new File( ConfigReader.getGoranTrialDir() + 
				File.separator + TAXA[level-1] + "fromOTUsAsColumn.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		HashSet<String> samples = new HashSet<String>();
		
		for( String s : map.keySet())
			samples.addAll(map.get(s).keySet());
		
		List<String> sampleList = new ArrayList<String>(samples);
		Collections.sort(sampleList);
		
		List<String> otuList = new ArrayList<String>(map.keySet());
		Collections.sort(otuList);
		
		writer.write("sample");
		
		for( String s : otuList)
			writer.write("\t" +s);
		
		writer.write("\n");
		
		for( String s : sampleList)
		{
			writer.write(s);
			
			for( String o : otuList)
			{
				HashMap<String, Double> innerMap = map.get(o);
				
				Double val = innerMap.get(s);
				
				if( val == null)
					writer.write("\t0");
				else
					writer.write("\t" + val);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		return outFile;
	}
	
	// outer key is taxa; inner key is sample
	private static HashMap<String, HashMap<String, Double>> getCountsMap(int level) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getGoranTrialDir() + File.separator + 
				"otuCountsAsColumns.txt");
		
		HashMap<String,String> otuToTaxaMap = ParseTaxaLine.getTaxaMap(level);
		HashMap<String, HashMap<String, Double>> map = new HashMap<String, HashMap<String,Double>>();
			
		for( int y=0; y < wrapper.getOtuNames().size(); y++)
		{
			String otuName = wrapper.getOtuNames().get(y);
			String taxaName = otuToTaxaMap.get(otuName);
			
			if( taxaName != null)
			{
				HashMap<String, Double> innerMap = map.get(taxaName);
				
				if(innerMap == null)
				{
					innerMap = new HashMap<String, Double>();
					map.put(taxaName, innerMap);
				}
				
				for( int z=0; z < wrapper.getSampleNames().size(); z++)
				{
					double newCount = wrapper.getDataPointsUnnormalized().get(z).get(y);
					String sampleName = wrapper.getSampleNames().get(z);
					
					Double oldCount = innerMap.get( sampleName );
					
					if(oldCount == null)
						oldCount = 0.0;
					
					newCount = newCount + oldCount;
					
					innerMap.put(sampleName, newCount);
				}
			}
		}
		
		return map;
	}
}
