package scripts.AliBariatricSurgery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class AddOneMonthDates
{
	public static final File ALI_TOP_DIR = new File("C:\\bariatricSurgery_Daisy\\fromAli_Dec15_2021\\countTables");
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, HashMap<Integer,Double>> map = getPatientWeightMap();
		 System.out.println(map);
	}
	
	// outerkey is patient ID;  inner key is timepoint;  inner value is weight
	private static HashMap<String, HashMap<Integer,Double>> getPatientWeightMap() throws Exception
	{
		HashMap<String, HashMap<Integer,Double>> map = null;
		
		String[] filesToParse = ALI_TOP_DIR.list();
		
		for(String s : filesToParse)
		{
			File inFile = new File( ALI_TOP_DIR + File.separator + s );
			System.out.println(inFile.getAbsolutePath());
			
			if( ! inFile.isDirectory() && s.endsWith(".tsv"))
			{
				if( map == null)
				{
					map = getPatientWeightMap(inFile);
				}
				else
				{
					HashMap<String, HashMap<Integer,Double>> map2 = getPatientWeightMap(inFile);
					
					assertTwoMapsEqual(map, map2);
				}
					
			}
		}
		
		return map;
	}
	
	private static void assertTwoMapsEqual(   HashMap<String, HashMap<Integer,Double>> map1,  HashMap<String, HashMap<Integer,Double>>  map2) throws Exception
	{
		Set<String> set1 = map1.keySet();
		Set<String> set2 = map2.keySet();
		
		if( ! set1.equals(set2))
			throw new Exception("NO");
		
		for(String s : set1)
		{
			if( ! map1.get(s).equals(map2.get(s)))
				throw new Exception("NO");
		}
	}
	
	private static HashMap<String, HashMap<Integer,Double>> getPatientWeightMap(File inFile) throws Exception
	{
		HashMap<String, HashMap<Integer,Double>> map = new LinkedHashMap<String,HashMap<Integer,Double>>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String id = splits[0];
			
			if( ! splits[8].equals("NA"))
			{

				int index = id.lastIndexOf("-");
				
				String subjectId = id.substring(0,index);
				Integer timepoint = Integer.parseInt(id.substring(index+1, id.length()));
				Double weight = Double.parseDouble(splits[8]);
				
				HashMap<Integer,Double> innerMap = map.get(subjectId);
				 
				 if( innerMap == null)
				 {
					 innerMap = new HashMap<Integer,Double>();
					 map.put(subjectId, innerMap);
				 }
				 
				 if( innerMap.containsKey(timepoint) && ! weight.equals(innerMap.get(timepoint)))
					 throw new Exception(" No " + subjectId + " " + timepoint);
				 
				 innerMap.put(timepoint,weight );
			}
			
		}
		
		reader.close();
		return map;
	}
}
