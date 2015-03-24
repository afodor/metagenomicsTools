package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.Avevar;
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
	
	private static void writePairs( HashMap<String, List<Double>> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(		
				ConfigReader.getMbqcDir() + File.separator + 
		 File.separator +  "fromGaleb" + File.separator + "simpsonsPaired.txt")));
		
		HashMap<String, String> kitMap = PValuesByExtraction.getManualKitManufacturer();
		
		writer.write("informaticsID\textractionID\tsequencingID\tmbqcID\tkit\textractionsimpson_reciprocal\tshippedsimpson_reciprocal\n");
		
		for(String s : map.keySet())
		{
			String[] splits = s.split("_");
			//informatics_extraction_sequencing_mbqcID
			
			if( !splits[1].equals("NA"))
			{
				// find the pair
				List<Double> match = map.get(splits[0] + "_NA_" + splits[2] +"_" + splits[3]);
				
				if( match != null)
				{
					writer.write(splits[0] + "\t" + splits[1] + "\t" + splits[2] + "\t" + splits[3] + "\t" + 
								kitMap.get(splits[1])  + "\t" + 
								new Avevar(map.get(s)).getAve() + "\t" + new Avevar(match).getAve() + "\n");
				}
			}
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, List<Double>> map = getDiversities();
		 writePairs(map);
	}
}
