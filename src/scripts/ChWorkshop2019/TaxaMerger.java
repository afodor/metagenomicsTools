package scripts.ChWorkshop2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.processing.Filer;

public class TaxaMerger
{
	private final static String[] TAXA_LEVELS = {"p","c","o","f","g","s"};
	
	
	public static void main(String[] args) throws Exception
	{
		for(String t : TAXA_LEVELS)
		{
			HashMap<String, HashMap<String,Integer> > map  = parseAtLevel(t);
			
			/*
			for(String s : map.keySet())
			{
				System.out.println(s);
				
				for( String s2 : map.get(s).keySet())
					System.out.println("\t" + s2 + " " + map.get(s).get(s2));
				
			}
			*/
			writeFile(map, t);
			System.out.println(t);
		}
		
	}
	
	private static void writeFile(HashMap<String, HashMap<String,Integer> > map, String taxonomicLevel) throws Exception
	{
		File outFile = 
				new File( "C:\\chWorkshop\\190709-DORSEY\\03_countsTables_txtFormat\\190709-DORSEY-txt\\" + 
		  taxonomicLevel + "_merged.txt");

		BufferedWriter writer =new BufferedWriter(new FileWriter(outFile));
		
		List<String> samples = new ArrayList<>(map.keySet());
		
		writer.write("sample");
		
		HashSet<String> otus = new HashSet();
		
		for(String s : map.keySet())
			for(String s2 : map.get(s).keySet())
				otus.add(s2);
		
		List<String> otuList = new ArrayList<>(otus);
		Collections.sort(otuList);
		
		for(String s : otuList)
			writer.write("\t" + s);
		
		writer.write("\n");

		for(String s : map.keySet())
		{
			writer.write(s);
			
			HashMap<String, Integer> innerMap =map.get(s);
			
			for(String otu : otuList)
			{
				Integer val = innerMap.get(otu);
				
				if( val == null)
					val = 0;
				
				writer.write("\t" + val);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	/**
	 * the outer key is sampleID; the inner key is taxaId
	 * 
	 * */
	private static HashMap<String, HashMap<String,Integer> > parseAtLevel( String taxaLeve) throws Exception
	{
		HashMap<String, HashMap<String,Integer> > map = new LinkedHashMap();
		
		File inFile = new File( "C:\\chWorkshop\\190709-DORSEY\\03_countsTables_txtFormat\\190709-DORSEY-txt\\taxa-table.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(int x=1; x < topSplits.length-2; x++)
			map.put(topSplits[x], new HashMap<>());
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception();
			
			String taxaString = splits[splits.length-2];
			String taxaKey = getTaxaIDOrNull(taxaString, taxaLeve);
			if( taxaKey != null)
			{

				for(int x=1; x < topSplits.length-2; x++)
				{
					String sampleID = topSplits[x];
					
					HashMap<String, Integer> innerMap = map.get(sampleID);
					
					Integer oldVal = innerMap.get(taxaKey);
					
					if( oldVal == null)
						oldVal = 0;
					
					oldVal =oldVal + Integer.parseInt(splits[x].replace(".0", ""));
					
					innerMap.put(taxaKey, oldVal);
				}
				
			}
		}
		
		return map;
	}
	
	private static String getTaxaIDOrNull(String s, String taxa )
	{
		//System.out.println("Search " + s + " " +taxa);
		String[] splits = s.split(";");
		
		for( int x=0; x < splits.length; x++)
			if( splits[x].trim().startsWith(taxa+ "__" ))
			{
				String returnVal = 	splits[x].replace(taxa + "__", "").trim();
				
				if(returnVal.length() ==0 )
					return null;
				
				return returnVal;

			}
		
		return null;
	}
}
