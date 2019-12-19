package scripts.compareEngel.taxaReassign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class PivotToCountsTable
{
	public static void main(String[] args) throws Exception
	{
		File inFile =new File(ConfigReader.getEngelCheckDir() + File.separator + 
				"JenDatabase" + File.separator +  "Engel_PIN" + File.separator +  "16S" + 
				File.separator + "STIRRUPS" + File.separator + "Engel_16S_stirrups_summary_97_070819.txt");
		/*HashMap<String, HashMap<String,Long>> map = */ getMap(inFile);
		
		
	}

	private static HashMap<String, HashMap<String,Long>> getMap(File file) throws Exception
	{
		HashMap<String, HashMap<String,Long>>  map = new HashMap<>();
		
		BufferedReader reader =new BufferedReader(new FileReader(file));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\\|");
			
			System.out.println(splits[0] +  " " + splits[1]);
		}
		
		return map;
	}
}
