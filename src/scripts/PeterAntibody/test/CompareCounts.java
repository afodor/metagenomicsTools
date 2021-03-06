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
	// outker key is file name
	// inner key is position@aaChar
	private static HashMap<String, HashMap<String,Integer>> getCountMap() throws Exception
	{
		HashMap<String, HashMap<String,Integer>>  map = new HashMap<>();
		
		HashMap<String, String> fileToCatMap = CountFeatures.getFileNameToCategoryMap();
		
		for(String fileName : fileToCatMap.keySet() )
		{
			System.out.println(fileName);
			File inFile =new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
					"oneAtATime" + File.separator +   "inputFiles" + File.separator + "Aug_1" + 
							File.separator + fileName);
			
			HashMap<String,Integer> innerMap = new HashMap<>();
			map.put(fileName, innerMap);
		
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				StringTokenizer sToken = new StringTokenizer(s);
				
				if( sToken.countTokens() == 2)
				{
					String key = sToken.nextToken() + "@" + Character.toUpperCase(sToken.nextToken().charAt(0));
					
					Integer count = innerMap.get(key);
					
					if(count == null)
						count =0;
					
					count++;
					
					innerMap.put(key, count);
				}
			}
		
		}
		
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,Integer>> countMap =getCountMap();
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
				HashMap<String,Integer> innerMap = countMap.get(fileName);
				
				if( fileToCatMap.get(fileName).equals(classificaiton))
				{
				//	File inFile =new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
					//		"oneAtATime" + File.separator +   "inputFiles" + File.separator + "Aug_1" + 
						//			File.separator + fileName);
					
					Integer aNum = innerMap.get(position + "@" + aa );
					if( aNum==null)
						aNum=0;
					observed += aNum;
				}
			}
			
			if( observed != expected)
				throw new Exception("Fail " + observed + " " + expected);
			

			System.out.println(classificaiton + " " + position + "  " + aa + " " + observed + " " + expected );
			
		}
		
		System.out.println("Pass");
	}
}
