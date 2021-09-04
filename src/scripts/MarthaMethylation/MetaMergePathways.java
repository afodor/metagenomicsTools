package scripts.MarthaMethylation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.OtuWrapper;

public class MetaMergePathways
{
	public final static String PATHWAY_FILE = "C:\\MarthaMethylation\\shanCountTables\\van_re_humann2_unstratified.tsv";
	
	public final static String PATHWAY_FILE_PIVOTED = 
				"C:\\MarthaMethylation\\shanCountTables\\humann2.txt";
	
	public final static String PATHWAY_FILE_PIVOTED_LOG_NORM = 
			"C:\\MarthaMethylation\\shanCountTables\\humann2LogNorm.txt";
	
	public static void main(String[] args) throws Exception
	{
		List<String> sampleNames = getSampleNames();
		
		HashMap<String, List<Double>> map = getPathwayData();
		writePivotedTable(sampleNames, map);
		
		OtuWrapper wrapper = new OtuWrapper(PATHWAY_FILE_PIVOTED);
		wrapper.writeNormalizedLoggedDataToFile(PATHWAY_FILE_PIVOTED_LOG_NORM);
	}
	
	private static void writePivotedTable(List<String> sampleNames , 
			HashMap<String, List<Double>> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter( new File(PATHWAY_FILE_PIVOTED)  ));
		
		writer.write("sample");
		
		List<String> pathways = new ArrayList<>();
		
		for(String s : map.keySet())
			pathways.add(s);
		
		for(String s : pathways)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=0; x < sampleNames.size(); x++)
		{
			writer.write(sampleNames.get(x));
			
			for( String s : pathways)
			{
				Double aVal = map.get(s).get(x);
				writer.write("\t" + aVal);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static List<String> getSampleNames() throws Exception
	{
		List<String> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATHWAY_FILE)));
		
		String firstLine = reader.readLine();
		
		String[] splits = firstLine.split("\t");
		
		for( int x=1; x < splits.length; x++)
		{
			StringTokenizer sToken = new StringTokenizer(splits[x], "_");
			
			String name = sToken.nextToken();
			
			if( list.contains(name))
				throw new Exception("Duplicate");
			
			list.add(name);
		}
		
		reader.close();
		return list;
	}
	
	// key is pathway
	private static HashMap<String, List<Double>> getPathwayData() throws Exception
	{
		HashMap<String, List<Double>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATHWAY_FILE)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s = reader.readLine() )
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("Duplicate");
			
			List<Double> list = new ArrayList<>();
			
			map.put(splits[0], list);
			
			for( int x=1; x < splits.length; x++)
				list.add(Double.parseDouble(splits[x]));
		}
		
		reader.close();
		return map;
	}
}
