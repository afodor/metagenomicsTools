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
		
		for(String s : sampleNames)
			System.out.println(s);
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
		
		return list;
	}
	
	
	// key is sampleID
	private static HashMap<String, List<Double>> getPathwayData() throws Exception
	{
		HashMap<String, List<Double>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATHWAY_FILE)));
		
		reader.readLine();
		
		
		reader.close();
		return map;
	}
}
