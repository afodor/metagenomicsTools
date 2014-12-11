package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class NumberSequencesHumanStandardDB
{
	private static class Holder
	{
		HashMap<String, Boolean> foundInHuman = new HashMap<String, Boolean>();
		HashMap<String, Boolean> foundInReference = new HashMap<String, Boolean>();
	}
	
	private static void addToMap(File file, HashMap<String, Holder> map) throws Exception
	{
		System.out.println(file.getAbsolutePath());
		boolean human = file.getName().indexOf("Human") != -1;
		
		String[] splits = file.getName().split("_");
		String fileName = splits[1] + "_" + splits[2];
		
		Holder h = map.get(fileName);
		
		if( h ==  null)
		{
			h = new Holder();
			map.put(fileName, h);
		}
		
		HashMap<String, Boolean> foundMap = human ? h.foundInHuman : h.foundInReference;
		
		if( foundMap.size() != 0)
			throw new Exception("Map not empty");
		
		BufferedReader reader =new BufferedReader(new InputStreamReader( new GZIPInputStream( new FileInputStream(  file))));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String firstToken = sToken.nextToken();
			
			if( firstToken.length() != 1)
				throw new Exception("Unexpected " + firstToken);
			
			String seqName = sToken.nextToken();
			
			if( foundMap.containsKey(seqName))
				throw new Exception("Duplicate sequence " + seqName);
			
			if( firstToken.equals("U"))
				foundMap.put(seqName, false);
			else if ( firstToken.equals("C"))
				foundMap.put(seqName, true);
			else throw new Exception("Unexpected first token " + s);
		}
		
		reader.close();
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String, Holder>();
		
		File topDir = new File(ConfigReader.getVanderbiltDir() + File.separator + "krakenOut");
		
		for(String s: topDir.list())
			if( s.startsWith("Sample"))
				addToMap(new File(topDir.getAbsoluteFile() + File.separator + s), map);
		
		
	}
}
