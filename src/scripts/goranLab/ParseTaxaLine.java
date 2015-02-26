package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class ParseTaxaLine
{
	public static HashMap<String, String> getTaxaMap(int level) throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranTrialDir() + File.separator 
				+ "OTU_Front_DATA Serum Solar Sano 02-20-2015.txt")));
		
		reader.readLine();  reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
		
			//System.out.println(splits[2]);
			
			if( splits[2].split(";").length -1 >= level)
			{
				String familyString =  splits[2].split(";")[level];
				familyString = new StringTokenizer(familyString, "(").nextToken().replaceAll("\"", "").trim();
				
				if( map.containsKey(splits[0]))
					throw new Exception("No");
				
				map.put(splits[0], familyString);
			}
			
		}
		
		return map;
	}
}
