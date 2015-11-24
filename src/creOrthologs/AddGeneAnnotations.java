package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TabReader;

public class AddGeneAnnotations
{
	
	public static HashMap<Integer, String> getLineDescriptions() throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader( 
				new File(ConfigReader.getCREOrthologsDir() + 
						File.separator + "mbgd_2015-01_extended.tab")));
		
		for( int x=0; x< 9; x++)
			reader.readLine();
		
		int lineNumber = 9;
		for(String s= reader.readLine();  s != null; s = reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			for( int x=0; x < 7; x++)
				tReader.nextToken();
			
			map.put(lineNumber, tReader.nextToken());
			lineNumber++;
			
			if(lineNumber % 1000 == 0)
				System.out.println("Reading annotations " + lineNumber);
		}
		
		reader.close();
		return map;
	}
	
	public static HashMap<String, HashSet<Integer>>  getFileLineMap( ) throws Exception
	{
		System.out.println("Reading annotations...");
		HashMap<String, HashSet<Integer>> map = new HashMap<String, HashSet<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader( 
				new File(ConfigReader.getCREOrthologsDir() + 
						File.separator + "mmbgd_2015-01_extended.tab")));
		
		for( int x=0; x< 9; x++)
			reader.readLine();

		int lineNumber = 9;
		for(String s= reader.readLine();  s != null; s = reader.readLine())
		{
			
			TabReader tReader =new TabReader(s);
			
			for( int x=0; x < 8; x++)
				tReader.nextToken();
			
			while(tReader.hasMore())
			{
				String next = tReader.nextToken().trim();
				
				if( next.length() >0)
				{
					StringTokenizer innerTokenizer = new StringTokenizer(next);
					
					while( innerTokenizer.hasMoreTokens())
					{
						String key = new StringTokenizer(innerTokenizer.nextToken(), "(").nextToken();
						
						HashSet<Integer> set = map.get(key);
						
						if( set == null)
						{
							set = new HashSet<Integer>();
							map.put(key, set);
						}
						
						set.add(lineNumber);
					}
				}
			}
			
			lineNumber++;
			
			if( lineNumber % 1000 == 0 )
				System.out.println(lineNumber);
				
		}
		
		reader.close();
		return map;
	}
	
	public static void addGeneAnnotation(String inFile, String outFile, HashMap<Integer, String> lineDescriptions) 
				throws Exception
	{	
		BufferedReader reader = new BufferedReader(new FileReader( inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write(reader.readLine() + "\tgeneDescription\n");

		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			writer.write(s + "\t");
			
			Integer key = Integer.parseInt(s.split("\t")[0].replaceAll("\"", "").replace("Line_", ""));
			writer.write(lineDescriptions.get(key) + "\n");
		}
		
		writer.flush(); writer.close();
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, String> map = getLineDescriptions();
		
		addGeneAnnotation(ConfigReader.getCREOrthologsDir() + File.separator + 
						"pValuesCarVsRes.txt", 
						ConfigReader.getCREOrthologsDir() + File.separator + 
						"pValuesCarVsResPlusMetadata.txt"
						, map);
		
		addGeneAnnotation(ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesCarVsSuc.txt", 
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesCarVsSucPlusMetadata.txt"
				, map);
		

		addGeneAnnotation(ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesResVsSuc.txt", 
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesResVsSucPlusMetadata.txt"
				, map);
		

		addGeneAnnotation(ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesMerged.txt", 
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pValuesMergedPlusAnnotations.txt"
				, map);
	}
}
