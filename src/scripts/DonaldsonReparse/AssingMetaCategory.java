package scripts.DonaldsonReparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class AssingMetaCategory
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> barcodeMap= getBarcodeToSampleMap();
		
		for(String s : barcodeMap.keySet())
			System.out.println(s + " " + barcodeMap.get(s));
	}
	
	private static HashMap<String, String> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\2010_08_20_454_key.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null && s.trim().length() > 0; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			String key = splits[1].replaceAll(Quick_454_Parse.PRIMER_TO_TRIM, "");
			
			if( !map.containsKey(key))
				map.put(key,splits[2]);
			else
			{
				System.out.println("Warning ignoring  " + key + " for " + splits[2] + " for " + map.get(key));
			}
		}
		
		reader.close();
		return map;
	}
}	
