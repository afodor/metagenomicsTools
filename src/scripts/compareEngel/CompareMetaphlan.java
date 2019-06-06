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
		Double virginiaParse;
		Double biolockJMetaphlan;
		Double krakenCount;
	}
	
	private static void addForAFile( File inFile , boolean isMetaphlan,  HashMap<String, HashMap<String,Holder>>  bigMap) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(inFile);
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = new StringTokenizer( wrapper.getSampleNames().get(x), "_").nextToken();
			
			HashMap<String, Holder> innerMap = bigMap.get(sampleName);
			
			if( innerMap== null)
				throw new Exception("Could not find " + sampleName);
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				Holder h = innerMap.get(wrapper.getOtuNames().get(y));
				
				if( h== null)
				{
					h = new Holder();
					innerMap.put(wrapper.getOtuNames().get(y), h);
				}
				
				if( isMetaphlan )
					h.biolockJMetaphlan = wrapper.getDataPointsNormalized().get(x).get(y);
				else
					h.krakenCount= wrapper.getDataPointsNormalized().get(x).get(y);
			}
		}
	}
	
	public static void writeForALevel(String level) throws Exception
	{
		System.out.println(level);
		HashMap<String, HashMap<String,Holder>>  map = parseMetaphlanSummaryAtLevel(
				new File( ConfigReader.getEngelCheckDir() + File.separator +  "allSamples.metaphlan2.bve.profile.txt"), "" + level.charAt(0));
	
		File blj_metaphan = new File(ConfigReader.getEngelCheckDir() + File.separator +  "racialDisparityMetaphlan2_2019Jun03_taxaCount_"+ level + ".tsv");
	
		addForAFile(blj_metaphan, true, map);
		
		File blj_kraken = new File(ConfigReader.getEngelCheckDir() + File.separator +  
				"racialDisparityKraken2_2019Jun03_taxaCount_" + level + ".tsv");
		
		addForAFile(blj_kraken, false, map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getEngelCheckDir() + File.separator + 
											"comparison_" + level + ".txt")));
		
		writer.write("sample\ttaxa\tvirginiaCount\tbiolockJCount\tkrakenCount\n");
		
		
		for(String s : map.keySet())
		{
			HashMap<String, Holder> innerMap = map.get(s);
			
			for(String s2 : innerMap.keySet())
			{
				Holder h = innerMap.get(s2);
				writer.write(s + "\t" + s2 + "\t" + getValOrZero(h.virginiaParse) + "\t" + getValOrZero(h.biolockJMetaphlan) + "\t" + 
									getValOrZero(h.krakenCount) + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	
	
	private static double getValOrZero( Double d )
	{
		if( d == null)
			return 0;
		
		return d;
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
				h.virginiaParse = Double.parseDouble(splits[2]);
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
