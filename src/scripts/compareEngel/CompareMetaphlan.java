package scripts.compareEngel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class CompareMetaphlan
{
	public static void main(String[] args) throws Exception
	{
		
		
		HashMap<String, HashMap<String,Float>>  map = parseMetaphlanSummaryAtLevel(new File("C:\\EngelCheck\\allSamples.metaphlan2.bve.profile.txt"));
	}
	
	// outer key is sample id;
	// inner key is taxa name
	private static HashMap<String, HashMap<String,Float>> parseMetaphlanSummaryAtLevel(File f) throws Exception
	{
		HashMap<String, HashMap<String,Float>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			System.out.println(splits[1] + " " + Float.parseFloat(splits[2]));
		}
		
		return map;
	}
}
