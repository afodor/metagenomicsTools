package test.JackCorrelation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class PivotToCountsTable
{
	public static void main(String[] args) throws Exception
	{
		File directoryToParse = new File("C:\\Jack_correlation\\standard_kraken2_db_with_fungi_reports");
		
		String[] files = directoryToParse.list();
		
		for(String s : files)
		{
			File f= new File( directoryToParse.getAbsolutePath() + File.separator + s );
			HashMap<String, Integer> innerMap = parseAFile(f);
		}
	}
	
	// outer map is sampleID
	private static HashMap<String, HashMap<String, Integer>> getMapFromKraken(File directoryToParse) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
		
		return map;
	}
	
	private static HashMap<String, Integer> parseAFile(File file) throws Exception
	{
		System.out.println(file.getAbsolutePath());
		HashMap<String, Integer> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 2)
				throw new Exception("parsing error");
		}
		
		reader.close();
		
		return map;
	}
}
