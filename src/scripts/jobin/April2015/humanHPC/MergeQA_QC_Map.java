package scripts.jobin.April2015.humanHPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MergeQA_QC_Map
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, MetadataFileLine> map = MetadataFileLine.getMapBySampleName();
		HashMap<String, Double> quantMap = getQuantEstimates();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getJobinApril2015Dir() + File.separator + 
						"pancreasQuantsPlusAssignments.txt")));
		
		writer.write("id\tdiseaseCat\tquant\n");
		
		for(String s : map.keySet())
		{
			writer.write( s + "\t");
			writer.write( map.get(s).getDiagnostic() + "\t");
			
			Double d = quantMap.get(s);
			
			if ( d == null)
				throw new Exception("No " + s);
			
			writer.write(d + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	static HashMap<String, Double> getQuantEstimates() throws Exception
	{
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader= new BufferedReader(new FileReader(
				ConfigReader.getJobinApril2015Dir() + File.separator + 
					"pancreasQuants.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits=s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Double.parseDouble(splits[1]));
		}
		
		return map;
	}
}
