package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import utils.ConfigReader;

public class AddMetadataToR
{
	private static HashMap<String, String> getIDToClassificationMap() throws Exception
	{
		HashMap<String,String> returnMap = new HashMap<>();
		
		Map< String, Map<String,Map<String,Character>>> map = CountFeatures.getMap();
		 
		for(String classification : map.keySet())
		{
			for(String id : map.get(classification).keySet())
			{
				if( returnMap.containsKey(id))
					throw new Exception("No");
				
				returnMap.put(id, classification);
			}
		}
		 
		return returnMap;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String,String> idToClassificaiton= getIDToClassificationMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"lc_pcoa.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"lc_pcoaWithMetadata.txt")));
		
		writer.write("id\tclassificaiton\t" + reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			s = s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			String classification = idToClassificaiton.get(splits[0]);
			
			if( classification == null)
				throw new Exception("No");
			
			writer.write(splits[0] + "\t" + classification );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		
		reader.close();
	}
}
