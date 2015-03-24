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
		HashMap<String, String> divMap = getDiversities();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String expected = divMap.get(splits[0]);
			
			if( expected == null)
				throw new Exception("No " + splits[0]);
			
			if( expected.equals("nan"))
			{
				if( !expected.equals(splits[10]))
					throw new Exception("No");
			}
			else
			{
				double diff = Math.abs( Double.parseDouble(expected) - Double.parseDouble(splits[10]));
				if( diff > 0.00000000000000001)
						throw new Exception("No");
			}
			

		}
		
		System.out.println("Passed");
	}
	
	private static HashMap<String, String> getDiversities() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefied.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			map.put(splits[0], splits[1]);
		}
		
		return map;
	}
}
