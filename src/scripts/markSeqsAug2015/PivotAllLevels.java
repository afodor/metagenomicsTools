package scripts.markSeqsAug2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotAllLevels
{
	private static final String[] LEVELS = { "otu", "k", "p", "c", "o", "f", "g" };
	public static final String UNCLASSIFIED = "UNCLASSIFIED";
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				ConfigReader.getMarkAug2015Batch1Dir() + File.separator + "qiime18_Freads_cr.txt");
		
		List<String> sampleNames = getSampleNames(inFile);
	}
	
	private static HashMap<String, Integer> getCounts(String level, File inFile) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		
		
		reader.close();
		
		return map;
	}
	
	private String getTaxonomy(String lastToken, String level) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(lastToken, ";");
		
		while( sToken.hasMoreTokens())
		{
			String nextToken = sToken.nextToken();
			
			if( nextToken.startsWith(level))
			{
				String taxa = nextToken.substring(3).trim();
				
				if( taxa.length() == 0 )
					taxa = UNCLASSIFIED;
				
				return taxa;
			}
		}
		
		throw new Exception("Could not find " + level + " in " + lastToken);
	}
	
	private static List<String> getSampleNames(File inFile) throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
		List<String> list = new ArrayList<String>();
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length - 1; x++)
			list.add(splits[x]);
		
		reader.close();
		return list;
	}
}
