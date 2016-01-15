package creOrthologs.kmers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import creOrthologs.AddMetadata;
import utils.ConfigReader;

public class NameReplacement
{
	public static void main(String[] args) throws Exception
	{
		// brute force and highly inefficient
		HashMap<String, String> map = getNameMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "outtree")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "outreeDecoded.txt")));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			for(String s2 : map.keySet())
				s = s.replaceAll(s2, map.get(s2));
			
			writer.write(s + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	private static HashMap<String, String> getNameMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		HashMap<String, String> catMap = AddMetadata.getBroadCategoryMap();
		
		for(String s : catMap.keySet())
			System.out.println(s + " " + catMap.get(s));
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"gatheredKmerMatrices" + File.separator + 
				"subKey.txt")));
		
		reader.readLine();
		for(String s = reader.readLine(); s != null;s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String shortString = sToken.nextToken();
			String longString = sToken.nextToken();
			
			if( map.containsKey(shortString))
				throw new Exception("No");
			
			if( map.containsValue(longString))
					throw new Exception("No");
			
			map.put(shortString, longString+"_" + catMap.get(longString));
		}
		
		reader.close();
		
		return map;
	}
}
