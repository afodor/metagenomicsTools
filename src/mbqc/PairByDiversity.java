package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;

public class PairByDiversity
{
	
	// key is informatics_extraction_sequencing_mbqcID
	private static HashMap<String, List<Double>> getDiversities() throws Exception
	{
		HashMap<String, List<Double>> map = new LinkedHashMap<String, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( ! splits[10].equals("nan"))
			{
				String key = splits[1]  + "_" + splits[4] + "_" + splits[5] + "_" + splits[6];
				
				List<Double> list = map.get(key);
				
				if(list == null)
				{
					list = new ArrayList<Double>();
					map.put(key, list);
				}
				
				list.add(Double.parseDouble(splits[10]));
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, List<Double>> map = getDiversities();
	}
}
