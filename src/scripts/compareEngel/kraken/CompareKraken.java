package scripts.compareEngel.kraken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

import parsers.OtuWrapper;
import utils.ConfigReader;

import javax.tools.JavaFileManager.Location;

public class CompareKraken
{
	private static final String[] LEVELS = { "phylum", "class", "order", "family", "genus", "species" };
	
	public static void main(String[] args) throws Exception
	{
		for( String level : LEVELS)
		{
			System.out.println(level);
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getEngelCheckDir() + File.separator + 
					"racialDisparityKraken2_2019Jun03_taxaCount_" + level + ".tsv");
			
			for(String sample : wrapper.getSampleNames())
			{
				
				String sampleName = new StringTokenizer(sample, "-").nextToken();
				
				File fileCheck = new File(ConfigReader.getEngelCheckDir() + File.separator + "krakenCheck" + 
										File.separator + sampleName + "_reported.tsv");
				
				if( fileCheck.exists())
				{
					HashMap<String, Long> map = getExpectedAtLevel(fileCheck, "" + level.charAt(0));
					//System.out.println("Found " + sampleName);
				}
				else
				{
					// some control samples that should not have been included in original sequences
					System.out.println("Could not find " + sampleName);
				}			
			}
		}
		
	}
	
	private static HashMap<String, Long> getExpectedAtLevel(File file, String level) throws Exception
	{
		HashMap<String, Long> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine();  s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != 2)
			{
				throw new Exception("Expecting 2");
			}
			
			String taxa = splits[0];
			taxa = taxa.substring(taxa.lastIndexOf("|") +1, taxa.length());
			
			if( taxa.startsWith(level + "__"))
			{
				taxa = taxa.replace(level + "__", "");
				
				if( map.containsKey(taxa))
					throw new Exception("Duplicate");
				
				map.put(taxa, Long.parseLong(splits[1]));
			}
		}
		
		return map;
	}
}
