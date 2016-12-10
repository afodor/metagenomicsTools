package scripts.LyteNov2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class PivotRawPValues
{
	private static HashMap<String, HashMap<String,Double>> getPivotedMap(
		File inFile	) 
		throws Exception
	{
		// outer key is the taxa; inner key is the tissue source
		 HashMap<String, HashMap<String,Double>> map = 
				 new HashMap<String, HashMap<String,Double>>();
		 
		 BufferedReader reader = new BufferedReader(new FileReader(
				 inFile));
		 
		 reader.readLine();
		 
		 for(String s= reader.readLine(); s != null; s= reader.readLine())
		 {
			 s = s.replaceAll("\"", "");
			 String[] splits =s.split("\t");
			 HashMap<String, Double> innerMap = map.get(splits[0]);
			 
			 if( innerMap == null)
			 {
				 innerMap = new HashMap<String,Double>();
				 map.put(splits[0], innerMap);
			 }
			 
			 if(innerMap.containsKey(splits[1]))
				 throw new Exception("Duplicate");
			 
			 innerMap.put(splits[1],Double.parseDouble(splits[2]));
		 }
		 
		 reader.close();
		 
		 return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			File inFile = new File(
					 ConfigReader.getLyteNov2016Dir() + File.separator + 
					 "spreadsheets" + File.separator + 
					 "pValuesFor" + taxa + ".txt");
			
			HashMap<String, HashMap<String,Double>> map = 
					getPivotedMap(inFile);
			
			HashSet<String> tissues = new HashSet<String>();
			
			for( HashMap<String, Double> innerMap : map.values() )
				tissues.addAll(innerMap.keySet());
			
			List<String> tissueList = new ArrayList<String>(tissues);
			Collections.sort(tissueList);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					new File( ConfigReader.getLyteNov2016Dir() + File.separator + 
					 "spreadsheets" + File.separator + 
					 "pValuesPivotedFor" + taxa + ".txt")));
			
			writer.write(taxa);
			
			for(String s : tissueList)
				writer.write("\t" + s);
			
			writer.write("\n");
			
			for(String s : map.keySet())
			{
				writer.write(s);
				
				for(String s2: tissueList)
				{
					Double val = map.get(s).get(s2);
					
					writer.write("\t" + (val != null ? val : "") );
				}
				
				writer.write("\n");
			}
			
			writer.flush(); writer.close();
			
		}
	}
}
