package scripts.AliBariatricSurgery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class AddOneMonthDates
{
	public static final File ALI_TOP_DIR = new File("C:\\bariatricSurgery_Daisy\\fromAli_Dec15_2021\\countTables");
	public static final File FILE_OUTPUT_DIR = new File("C:\\bariatricSurgery_Daisy\\fromAli_Dec15_2021\\AF_Out");
	
	public static final int[] TIMEPOINTS = { 0,1,6,12,18,24 };
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, HashMap<Integer,Double>> map = getPatientWeightMap();
		 System.out.println(map);
		 

		String[] filesToParse = ALI_TOP_DIR.list();
			
		for(String s : filesToParse)
			if( s.endsWith(".tsv"))
			{
				File inFile = new File( ALI_TOP_DIR + File.separator + s );
				File outFile = new File( FILE_OUTPUT_DIR + File.separator + s.replace(".tsv", "") +"withPercentChange.txt" );
				addMetadata(map, inFile, outFile);
			}
	}
	
	private static String getPercentChange( HashMap<Integer,Double> map , Integer timepoint2, Integer timepoint1 ) throws Exception
	{
		if( map == null)
			return "NA";
		
		Double val1 = map.get(timepoint1);
		Double val2 = map.get(timepoint2);
		
		if( val1 == null || val2 == null)
			return "NA";
		
		double percentChange = 100.0 * (val2 - val1 ) / val1;
		
		return "" + percentChange;
	}
	
	private static void addMetadata( HashMap<String, HashMap<Integer,Double>> map , File inFile, File outFile) throws Exception
	{
		System.out.println(inFile.getAbsolutePath());
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(int x=0; x < 6; x++)
			writer.write(topSplits[x] + "\t");
		
		writer.write("timepointInt");
		
		for( int x=1; x < TIMEPOINTS.length; x++)
			writer.write("\tpercentChange_" + TIMEPOINTS[x] + "_0");
		
		for( int x=2; x < TIMEPOINTS.length; x++)
			writer.write("\tpercentChange_" + TIMEPOINTS[x] + "_1");

		for( int x=7; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for( String s= reader.readLine(); s != null; s = reader.readLine() )
		{
			String[] splits = s.split("\t");
			
			for( int x=0; x <6; x++)
				writer.write("\t" + splits[x]);
			
			HashMap<Integer, Double> innerMap = map.get(splits[1]);
			
			for( int x=1; x < TIMEPOINTS.length; x++)
				writer.write("\t" + getPercentChange(innerMap, TIMEPOINTS[x] , 0));
			
			for( int x=2; x < TIMEPOINTS.length; x++)
				writer.write("\t" + getPercentChange(innerMap, TIMEPOINTS[x] , 1));
			
			for( int x=7; x < topSplits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");

		}
		
		
		writer.flush();  writer.close();
		reader.close();
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
