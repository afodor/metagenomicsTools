package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddTaxaNames
{
	private static HashMap<String, String> getTaxaStrings() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
				+ File.separator + 
					"LyteSharon_r01_cr.txt")));
		
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key ="X" + splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			
			map.put(key, splits[splits.length-1]);
		}
		
		reader.close();
	
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getTaxaStrings();
		
		String[] sources = {"Cecal_Content", "feces", "jej", "ileum","duo" };
		
		for( String t : sources)
		{	
			
			File inFile = new File( ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
					+ File.separator + 
					 "mainEffects" + t+ ".txt");
			
			File outFile =new File( ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
					+ File.separator + 
					 "mainEffectsWithTaxaCalls_" + t+ ".txt");
			
			addTaxaNames(map, inFile, outFile);
			 
		}
		
	}
	
	private static void addTaxaNames( HashMap<String, String> map , File inFile ,File outFile  )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		writer.write(reader.readLine() + "\ttaxaAssignment\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String taxaAssignment = map.get(s.split("\t")[0].replaceAll("\"", ""));
			
			if( taxaAssignment == null)
			{
				System.out.println("No " + s.split("\t")[0]);
				taxaAssignment = s.split("\t")[0].replaceAll("\"", "");
			}
			
			writer.write(s + "\t" + taxaAssignment + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
