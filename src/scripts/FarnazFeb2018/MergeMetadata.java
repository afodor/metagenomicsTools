package scripts.FarnazFeb2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeMetadata
{
	public static void main(String[] args) throws Exception
	{
		File metaFile = new File(ConfigReader.getFarnazFeb2018Directory() + File.separator + 
					"Farnaz_MDMF_MBP_var.txt");
		
		String firstLine = readFirstLine(metaFile);
		HashMap<Integer, String> metaMap = getMetaMap(metaFile);
		System.out.println(metaMap.size());
	}
	
	private static HashMap<Integer, String> getMetaMap(File file) throws Exception
	{
		HashMap<Integer, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			Integer id = Integer.parseInt(splits[0]);
			
			if( map.containsKey(id) )
				throw new Exception("Duplicate id " + id);
			
			map.put(id, s.replaceAll("\"", ""));
		}
		
		reader.close();
		
		return map;
	}
	
	private static String readFirstLine(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String returnVal = reader.readLine();
		
		reader.close();
		
		return returnVal;
	}
}
