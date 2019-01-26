package scripts.shanPValueScramble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.Filer;

import utils.ConfigReader;

public class DoPermutations
{
	public static final int NUM_PROVINCES =15;
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getChinaMay2017Dir() + File.separator + 
					"shanPValues" + File.separator + "China_anova_t1.csv");
		
		HashMap<String, List<Double>> map =getInitialParse(inFile);
		
		for(String s : map.keySet())
		{
			System.out.println(s);
		}
	}
	
	private static HashMap<String, List<Double>> getInitialParse(File inFile) throws Exception
	{
		HashMap<String, List<Double>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s !=null; s= reader.readLine())
		{
			String[] splits = s.split(",");
			
			if( splits.length != NUM_PROVINCES + 1)
				throw new Exception("NO " + splits.length);
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("NO");
			
			List<Double> list = new ArrayList<>();
			map.put(key, list);
		}
		
		reader.close();
		
		return map;
	}
}
