package mbqc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class CheckDiversities
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> divMap = getDiversities();
		
	}
	
	private static HashMap<String, Double> getDiversities() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefied.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			if( ! splits[1].equals("nan"))
				map.put(splits[0], Double.parseDouble(splits[1]));
		}
		
		return map;
	}
}
