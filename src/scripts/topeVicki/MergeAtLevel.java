package scripts.topeVicki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MergeAtLevel
{
	public static void main(String[] args)
	{
		
	}
	
	private static Map<String, Map<String,Integer>> mergeAtLevel(String level ) throws Exception
	{
		Map<String, Map<String,Integer>> map = new HashMap<>();
		
		return map;
	}
	
	private static void addToMap(Map<String, Map<String,Integer>>  map, File file, String level) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		String[] topSplits =reader.readLine().split("\t");
		
		for(String s= reader.readLine(); s  != null; s= reader.readLine())
		{
			String taxa = getTaxa(s, level);
			
			
		}
		
		reader.close();
	}
	
	private static String getTaxa(String s, String level) throws Exception
	{
		String[] splits = s.split("\t");
		String last = splits[splits.length -1];
		
		StringTokenizer sToken = new StringTokenizer(last,";");
		
		while(sToken.hasMoreTokens())
		{
			String taxa =sToken.nextToken();
			
			if( taxa.startsWith(level + "__"))
			{
				taxa = taxa.replace(level + "__", "" ).trim();
				
				if( taxa.length() == 0 )
					return null;
				
				return taxa;
			}
		}
		
		throw new Exception("Could not find " + level + " " +last);
	}
}
