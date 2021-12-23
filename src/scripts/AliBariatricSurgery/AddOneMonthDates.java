package scripts.AliBariatricSurgery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddOneMonthDates
{
	public static final File ALI_TOP_DIR = new File("C:\\bariatricSurgery_Daisy\\fromAli_Dec15_2021\\countTables");
	
	public static void main(String[] args) throws Exception
	{
		getPatientWeightMap();
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

				getPatientWeightMap(inFile);
			}
		}
		
		return map;
	}
	
	private static HashMap<String, HashMap<Integer,Double>> getPatientWeightMap(File inFile) throws Exception
	{
		HashMap<String, HashMap<Integer,Double>> map = new LinkedHashMap<String,HashMap<Integer,Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String id = splits[0];
			
			int index = id.lastIndexOf("-");
			
			String subjectId = id.substring(0,index);
			Integer timepoint = Integer.parseInt(id.substring(index+1, id.length()));
			
			System.out.println( id + " " +  subjectId + " " + timepoint );
		}
		
		reader.close();
		return map;
	}
}
