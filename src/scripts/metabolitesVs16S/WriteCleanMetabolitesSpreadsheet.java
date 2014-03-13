package scripts.metabolitesVs16S;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TabReader;

public class WriteCleanMetabolitesSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMetabolitesCaseControl() + 
				File.separator + "sampleList.txt")));
		
		List<String> names =getNamedMetabolites(reader.readLine());
		
		List<String> fileStrings = new ArrayList<String>();
		for( String s= reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
			fileStrings.add(s);
		
		HashSet<Integer> exclusionNums = new HashSet<Integer>();
		
		for(String s : fileStrings)
			addToExclusionsNums(exclusionNums, s);

		writeResults(names, exclusionNums, fileStrings);
		
		reader.close();
	}
	
	private static void writeResults( List<String> names,
					HashSet<Integer> exclusionNums, List<String> fileStrings ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMetabolitesCaseControl() + 
				File.separator + "cleanSampleListMetabolitesAsColumns.txt")));
		
		writer.write("sampleID");
		
		for( int x=0; x < names.size(); x++)
			if( ! exclusionNums.contains(new Integer(x)))
				writer.write("\t" + names.get(x));
		
		writer.write("\n");
		
		for(String s: fileStrings)
		{
			TabReader tReader = new TabReader(s);
			
			tReader.nextToken();tReader.nextToken();
			
			writer.write(tReader.nextToken());
			
			tReader.nextToken();
			
			int x=0;
			
			while( tReader.hasMore() )
			{
				String aToken = tReader.nextToken().trim().replaceAll("\"", "");
				
				
				if( ! exclusionNums.contains(new Integer(x)))
					writer.write("\t" + aToken);
				
				x++;
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addToExclusionsNums(HashSet<Integer> set, String s)
	{
		TabReader tReader = new TabReader(s);
		
		for( int x=0; x < 4; x++)
			tReader.nextToken();
		
		int x=0;
		
		while( tReader.hasMore())
		{
			String aToken = tReader.nextToken().trim().replaceAll("\"", "");
			
			if(aToken.trim().length() == 0)
			{
				set.add(x);
			}
			else
			{
				Double.parseDouble(aToken);
			}
			x++;
		}
	}
	
	
	private static List<String> getNamedMetabolites(String firstLine) 
	{
		StringTokenizer sToken = new StringTokenizer(firstLine,"\t");
		
		for(int x=0; x < 4; x++)
			sToken.nextToken();
		
		List<String> list = new ArrayList<String>();
		
		while( sToken.hasMoreTokens())
			list.add(sToken.nextToken().trim().replaceAll("\"", ""));
		
		return list;
	}
	
}
