package scripts.PeterAntibody.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import scripts.PeterAntibody.CountFeatures;
import utils.ConfigReader;

public class CompareCounts
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> fileToCatMap = CountFeatures.getFileNameToCategoryMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
					"frequenciesForGraphing.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			String classificaiton = splits[0];
			String position = splits[1];
			char aa = splits[2].charAt(0);
			int expected = Integer.parseInt(splits[4]);
			int observed = 0;
			for(String fileName : fileToCatMap.keySet() )
			{
				if( fileToCatMap.get(fileName).equals(classificaiton))
				{
					File inFile =new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
							"oneAtATime" + File.separator +   "inputFiles" + File.separator + "Aug_1" + 
									File.separator + fileName);
					observed += getObserved(inFile, position, aa);
				}
			}
			
			System.out.println(classificaiton + " " + position + "  " + aa + " " + observed );
			
			if( observed != expected)
				throw new Exception("Fail " + observed + " " + expected);
			
		}
		
		System.out.println("Pass");
	}
	
	private static int getObserved(File inFile, String position, char aaChar)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		int observed =0;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			if( sToken.countTokens() >= 2)
			{
				if( sToken.nextToken().equals(position))
				{
					char aChar =sToken.nextToken().charAt(0);
					
					if( Character.toUpperCase(aChar)==aaChar)
						observed++;
				}
			}
		}
		
		return observed;
	}
}
