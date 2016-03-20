package scripts.sonja2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class QuickAverages
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> annotationMap = getAnnotationMap();
		
		for(String s : annotationMap.keySet())
			System.out.println(s + " " + annotationMap.get(s));
	}
	
	private static HashMap<String, String> getAnnotationMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getSonja2016Dir() + File.separator + "AllArrayData.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s!= null; s=reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[splits.length-1].replaceAll("\"", ""));
		}
		
		return map;
	}
}
