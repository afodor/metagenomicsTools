package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	public static HashMap<String, String> getBroadCategoryMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "broadCategories.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length !=2 )
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
		}
		
		reader.close();
							
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> catMap = getBroadCategoryMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getCREOrthologsDir() + File.separator + "bitScoreOrthologsAsColumns.txt" )));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() 
				+ File.separator + "bitScoreOrthologsAsColumnsPlustMedata.txt" )));
		
		writer.write("genome\tbroadCategory");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1;x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0] + "\t");
			
			String cat = catMap.get(splits[0]);
			
			if( cat == null)
				throw new Exception("No");
			
			writer.write(cat);
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
