package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import utils.ConfigReader;
import utils.TabReader;

public class WriteUrineForSVMLight
{
	static HashMap<Integer,Double> getPCOA() throws Exception
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
		
		List<Integer> keys = new ArrayList<Integer>(pcoaMap.keySet());
		int halfPoint = keys.size() / 2;
		Random random = new Random(324234);
		Collections.shuffle(keys, random);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
						"trainingSet.txt"
				)));
		
		for( int x=0; x < halfPoint; x++)
		{
			writer.write( pcoaMap.get(keys.get(x)) + " "  );
			
			List<Double> list = metaboliteMap.get(keys.get(x));
			
			for(  int y=0; y < list.size(); y++ )
				writer.write( (y+1) + ":" + list.get(y) + " " );
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		// train with svm_learn.exe -z r trainingSet.txt regressModel
		
		writer = new BufferedWriter(new FileWriter(new File( 
			ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					"setToClassify.txt"	)));
		
		for( int x=halfPoint; x < keys.size(); x++ ) 
		{
			writer.write("sample" + keys.get(x) + " " );
			
			List<Double> list = metaboliteMap.get(keys.get(x));
			
			for(  int y=0; y < list.size(); y++ )
				writer.write( (y+1) + ":" + list.get(y) + " " );
			
			writer.write("\n");
		}
		// classify with svm_classify setToClassify.txt regressModelsvmOut.txt
		
		writer.flush(); writer.close();
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
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			tr =new TabReader(s);
			tr.nextToken(); tr.nextToken(); tr.nextToken();
			
			for( Integer key : map.keySet())
			{
				List<Double> list =map.get(key);
				list.add(Double.parseDouble(tr.nextToken()));
			}
			
			if( tr.hasMore())
				throw new Exception("No");
				
		}
		
		reader.close();
		
		return map;
	}
}
