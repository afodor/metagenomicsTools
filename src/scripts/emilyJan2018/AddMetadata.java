package scripts.emilyJan2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		File meta1 =new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN703_16S metadata.txt");
		
		File meta2 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
				"2018-01-10_AN40_16S metadata.txt");
		
		File meta3 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN34_16S metadata.txt");
		
		String top1= getFirstLine(meta1);
		String top2 = getFirstLine(meta2);
		String top3 = getFirstLine(meta3);
		
		if( ! top1.equals(top2))
			throw new Exception("No");
		
		if( ! top1.equals(top3))
			throw new Exception("No");
		
		if( ! top2.equals(top3))
			throw new Exception("Logic error");
		
		HashMap<String, String> map = new HashMap<>();
		addToMap(meta1, map, "AN703");
		addToMap(meta2, map, "AN40");
		addToMap(meta3, map,"AN34");
	}
	
	private static void addToMap(File inFile, HashMap<String, String> map, String suffix) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			String key = splits[0] + "_" + suffix;
			
			if( map.containsKey(key))
				throw new Exception("Duplciate key " +  key);
			
			map.put(key, s);
		}
		
		reader.close();
	}
	
	private static void writeAMeta(File inFile, File outFile, String firstLine ) throws Exception
	{
		
	}
	
	private static String getFirstLine(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String firstLine = reader.readLine();
		
		reader.close();
		
		return firstLine;
	}
}
