package scripts.evanFeb2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class Demultiplex
{

	public static void main(String[] args) throws Exception
	{
		File mappingFile1 = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
				"Batch_1_2015" + File.separator + "batch_1_2015_mapping.txt");
		
		HashMap<String, String> barcode1 = getBarcodeToSampleMap(mappingFile1);

		for(String s : barcode1.keySet())
			System.out.println(s +  " "+ barcode1.get(s));
	}
	
	private static HashMap<String, String> getBarcodeToSampleMap(File inFile) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[1]))
				throw new Exception("No");
			
			map.put(splits[1], splits[0]);
		}
		
		return map;
	}
	
}

