package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{	
	public static final String getTopLine() throws Exception
	{
		File metadataFile = new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
					"CompiledMetadata.txt");
		
		BufferedReader reader =new BufferedReader(new FileReader(metadataFile));
		
		String returnVal = reader.readLine();
		
		reader.close();
		return returnVal;
				
	}
	
	public static HashMap<String, String> getLinesAsMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		File metadataFile = new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
					"CompiledMetadata.txt");
		
		BufferedReader reader =new BufferedReader(new FileReader(metadataFile));
		
		reader.readLine();  
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key");
			
			map.put(key,s);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getLinesAsMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
	}
}
