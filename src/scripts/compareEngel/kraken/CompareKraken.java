package scripts.compareEngel.kraken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class CompareKraken
{
	public static final String[] LEVELS = { "phylum", "class", "order", "family", "genus", "species" };
	
	public static void main(String[] args) throws Exception
	{
		for( String level : LEVELS)
		{
			System.out.println(level);
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getEngelCheckDir() + File.separator + "krakenCheck" + File.separator + 
					"racialDisparityKraken2_2019Jun10_taxaCount_" + level + ".tsv");
			
			for(int x=0; x < wrapper.getSampleNames().size(); x++)
			{
				String sample = wrapper.getSampleNames().get(x);
				System.out.println(sample);
				
				String sampleName = new StringTokenizer(sample, "-").nextToken();
				
				File fileCheck = new File(ConfigReader.getEngelCheckDir() + File.separator + "krakenCheck" + 
										File.separator + sampleName + "_reported.tsv");
				
				if( fileCheck.exists())
				{
					HashMap<String, Integer> map = getExpectedAtLevel(fileCheck, "" + level.charAt(0));
				
					System.out.println(map);
					
					for( int y=0; y< wrapper.getOtuNames().size(); y++)
					{
						String taxaName = wrapper.getOtuNames().get(y);
						
						if( taxaName.toLowerCase().indexOf("unclassified") == - 1)
						{
							double aVal = wrapper.getDataPointsUnnormalized().get(x).get(y);
							
							Integer anotherVal = map.get(taxaName);
							
							if(anotherVal == null)
								anotherVal = 0;
							
							if( Math.abs(aVal-anotherVal) > 0.001) 
								throw new Exception("Mismatch " + taxaName + " "+  aVal + " " + anotherVal);
							else
								System.out.println("Pass " + taxaName+ " " + aVal + " " + anotherVal);
						}
					}
				}
				else
				{
					// some control samples that should not have been included in original sequences
					System.out.println("Could not find " + sampleName);
				}			
			}
		}
		
		System.out.println("Pass");
	}
	
	public static HashMap<String, Integer> getExpectedAtLevel(File file, String level) throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<>();
		
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
				
				map.put(taxa, Integer.parseInt(splits[1]));
			}
		}
		
		return map;
	}
}
