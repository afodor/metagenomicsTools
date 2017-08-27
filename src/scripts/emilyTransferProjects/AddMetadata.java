package scripts.emilyTransferProjects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	private static List<Boolean> getExcludeList(File inFile) throws Exception
	{
		List<Boolean> list = new ArrayList<Boolean>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] splits = reader.readLine().split("\t");
		
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		
		for( int x=1; x  <splits.length; x++)
			map.put(x,0);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			splits = s.split("\t");
			
			for( int x= 1; x < splits.length; x++)
			{
				if( Double.parseDouble(splits[x]) > 0 )
				{
					map.put(x, map.get(x) + 1);
				}
			}
				
		}
		
		list.add(true);
		
		for( int x=1; x < splits.length; x++)
		{
			if( map.get(x) > 11)
				list.add(true);
			else
				list.add(false);
		}
		
		
		return list;
	}
	
	private static void writeFilteredFile(File inFile, File filteredFile) throws Exception
	{
		List<Boolean> excludeList = getExcludeList(inFile);
		

		BufferedWriter writer = new BufferedWriter(new FileWriter(filteredFile));
		
		BufferedReader reader=  new BufferedReader(new FileReader(inFile));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0]);
			
			for( int x=1; x < splits.length; x++)
				if( excludeList.get(x))
					writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		reader.close();
		writer.flush(); writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getEmilyTransferProject() + File.separator + 
				"otu_table_mc2_w_taxAsColumns.txt");
		
		File filteredFile = new File(ConfigReader.getEmilyTransferProject() + File.separator + 
				"otu_table_mc2_w_taxAsColumnsNonRare.txt");
		
		writeFilteredFile(inFile, filteredFile);
		
		OtuWrapper wrapper = new OtuWrapper(filteredFile);
		
		File logFile = new File(ConfigReader.getEmilyTransferProject() + File.separator + 
				"otu_table_mc2_w_taxAsColumnsfilteredLogNormal.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logFile);
		
	}
	
	/*
	private static HashMap<String, String> getMetadataLine() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		
		//BufferedReader reader = new BufferedReader(new FileReader(			)));
		
		//String topSplits = reader.readLine();
		
		//return map;
	}*/
}
