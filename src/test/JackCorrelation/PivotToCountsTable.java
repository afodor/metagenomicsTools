package test.JackCorrelation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class PivotToCountsTable
{
	public static void main(String[] args) throws Exception
	{
		File directoryToParse = new File("C:\\Jack_correlation\\standard_kraken2_db_with_fungi_reports");
		getMapFromKraken(directoryToParse);
	}
	
	// outer map is sampleID
	private static HashMap<String, HashMap<String, Integer>> getMapFromKraken(File directoryToParse) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
		
		String[] files = directoryToParse.list();
		
		for(String s : files)
		{
			File f= new File( directoryToParse.getAbsolutePath() + File.separator + s );
			HashMap<String, Integer> innerMap = parseAFile(f);
			String key = new StringTokenizer(s, "_").nextToken();
			System.out.println(key);
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			map.put(key, innerMap);
			
		}
		
		return map;
	}
	
	private static HashMap<String, Integer> parseAFile(File file) throws Exception
	{
		//System.out.println(file.getAbsolutePath());
		HashMap<String, Integer> map = new HashMap<>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 2)
				throw new Exception("parsing error");
			
			String genus = getAGenusOrNull(splits[0]);
			
			if( genus != null)
			{
				if( map.containsKey(genus))
					throw new Exception("Parsing error " + genus);
				
				map.put(genus, Integer.parseInt(splits[1]));
				
			}
		}
		
		reader.close();
		
		return map;
	}
	
	private static String getAGenusOrNull( String firstToken ) 
	{
		String[] splits = firstToken.split("\\|");
		
		if( splits[splits.length-1].startsWith("g__"))
		{
			String genus = splits[splits.length-1].substring(3);
			return genus;
		}
		
		return null;
	}
}
