package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;
import utils.TabReader;

public class WriteUrineForSVMLight
{
	private static HashMap<Integer,Double> getPCOA() throws Exception
	{
		HashMap<Integer, Double> map = new HashMap<Integer,Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"pcoa_Microbiome_Metabolomics_taxaAsColumns.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int key = Integer.parseInt(splits[0].replaceAll("\"", ""));
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Double.parseDouble(splits[1]));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer,Double> pcoaMap = getPCOA();
		HashMap<Integer, List<Double>> metaboliteMap = getMetabolites();
		
	}
	
	private static HashMap<Integer, List<Double>> getMetabolites() throws Exception
	{
		HashMap<Integer, List<Double>> map = new LinkedHashMap<Integer, List<Double>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"urine_metabolites_data.txt")));
		
		String firstLine = reader.readLine();
		
		TabReader tr = new TabReader(firstLine);
		
		tr.nextToken(); tr.nextToken(); tr.nextToken();
		
		for(int x=1; x <= 124; x++)
		{
			int aVal = Integer.parseInt(tr.nextToken());
			
			map.put(aVal, new ArrayList<Double>());
		}
		
		if(tr.hasMore())
			throw new Exception("No");
		
		reader.close();
		
		return map;
	}
}
