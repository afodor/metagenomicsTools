package scripts.MarthaMethylation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class MetaMergePathways
{
	public final static String PATHWAY_FILE = "C:\\MarthaMethylation\\shanCountTables\\van_re_humann2_unstratified.tsv";
	
	public static void main(String[] args) throws Exception
	{
		List<String> sampleNames = getSampleNames();
		
		HashMap<String, List<Double>> map = getPathwayData();
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
		HashMap<String, List<Double>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATHWAY_FILE)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s = reader.readLine() )
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("Duplicate");
			
			
			List<Double> list = new ArrayList<>();
			
			map.put(splits[0], list);
			
			for( int x=1; x < list.size(); x++)
				list.add(Double.parseDouble(splits[x]));
		}
		
		reader.close();
		return map;
	}
}
