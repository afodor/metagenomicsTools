package scripts.compareEngel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class CompareMetaphlan
{
	public static void main(String[] args) throws Exception
	{
		String[] levels = {"phylum", "class", "order", "family", "genus", "species"};
		
		for(String s : levels)
			writeForALevel(s);
	}
	
	private static class Holder
	{
		Float virginiaParse;
	}
	
	public static void writeForALevel(String level) throws Exception
	{
		System.out.println(level);
		HashMap<String, HashMap<String,Holder>>  map = parseMetaphlanSummaryAtLevel(
				new File( ConfigReader.getEngelCheckDir() + File.separator +  "allSamples.metaphlan2.bve.profile.txt"), "" + level.charAt(0));
	
		OtuWrapper wrapper = new OtuWrapper(
				ConfigReader.getEngelCheckDir() + File.separator +  "racialDisparityMetaphlan2_2019Jun03_taxaCount_"+ level + ".tsv");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getEngelCheckDir() + File.separator + 
											"comparison_" + level + ".txt")));
		
		writer.write("sample\ttaxa\tvirginiaCount\tbiolockJCount\n");
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = new StringTokenizer( wrapper.getSampleNames().get(x), "_").nextToken();
			
			HashMap<String, Holder> virginiaMap = map.get(sampleName);
			
			if( virginiaMap == null)
				throw new Exception("Could not find " + sampleName);
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				Holder h = virginiaMap.get(wrapper.getOtuNames().get(y));
				
				writer.write(sampleName + "\t" + wrapper.getOtuNames().get(y) + "\t");
				
				if( h!= null)
					writer.write(h.virginiaParse + "\t");
				else
					writer.write(0 + "\t");
				
				virginiaMap.remove(wrapper.getOtuNames().get(y));
				
				writer.write(wrapper.getDataPointsNormalized().get(x).get(y) + "\n");
			}
			
		}
		
		for(String s : map.keySet())
		{
			HashMap<String, Holder> innerMap = map.get(s);
			
			for(String s2 : innerMap.keySet())
			{
				writer.write(s + "\t" + s2 + "\t" + innerMap.get(s2).virginiaParse + "\t0\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	
	
	
	
	// outer key is sample id;
	// inner key is taxa name
	private static HashMap<String, HashMap<String,Holder>> parseMetaphlanSummaryAtLevel(File f, String level) throws Exception
	{
		HashMap<String, HashMap<String,Holder>> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			//System.out.println(splits[0] + "@" + splits[1] + " " + Float.parseFloat(splits[2]));
			
			String lastMatch = getLastMatchOrNull(splits[1], level);
			
			if( lastMatch != null)
			{
				HashMap<String, Holder> innerMap = map.get(splits[0]);
				
				if( innerMap==null)
				{	
					innerMap = new HashMap<>();
					map.put(splits[0], innerMap);
				}
					
					
				if( innerMap.containsKey(lastMatch))
					throw new Exception("Duplicate " + lastMatch);
					
				Holder h= new Holder();
				h.virginiaParse = Float.parseFloat(splits[2]);
				innerMap.put(lastMatch, h);
			}
		}
		
		return map;
	}
	
	private static String getLastMatchOrNull(String s, String level)
	{
		s = s.substring(s.lastIndexOf("|") +1, s.length());
		
		String startString = level + "__";
		
		if( ! s.startsWith(startString))
			return null;
		
		return s.replace(startString, "");
	}
}
