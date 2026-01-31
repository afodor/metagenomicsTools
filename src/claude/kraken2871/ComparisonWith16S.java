package claude.kraken2871;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import claude.mouseCDiff.SylphGenusCleaner;
import claude.mouseCDiff.TaxonCleaners;

public class ComparisonWith16S
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			"C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\allGenusFlat.txt"	)));
		writer.write("SampleType\tsampleID\tgenus\tgenusSum\n");
		
		add16SMap(writer);
		
		addKrakenMax1(writer, null, "kraken1_unfiltered");
		
		HashSet<String> set= getIncludedSet();
		System.out.println(set );
		System.out.println(set.size() );
		addKrakenMax1(writer, set, "kraken1_filtered");
		addSylph(writer);
		
		writer.flush();  writer.close();
	}
	
	private static HashSet<String> getIncludedSet() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader =
			    Files.newBufferedReader(new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\predictedVsActual_with_pvalues.txt").toPath(), 
			    			StandardCharsets.UTF_8);
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( Double.parseDouble(splits[3]) < 0.05)
				set.add(splits[0]);
		}
		
		reader.close();
		return set;
		
	}
	
	private static void addSylph(BufferedWriter flatFile) throws Exception
	{
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader =
			    Files.newBufferedReader(new 
			    		File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\sylph_merged.txt").toPath(), 
			    		StandardCharsets.UTF_8);
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine());
		List<String> sampleNames = new ArrayList<String>();
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
		{
			String sampleName = sToken.nextToken().replaceAll("\"", "").trim();
			
			sampleName = sampleName.substring(0, sampleName.indexOf("_"));
			
			sampleNames.add(sampleName);
		}
				
		
		System.out.println(sampleNames);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			sToken = new StringTokenizer(s, "\t");
			
			String tax = sToken.nextToken();
			
				
			String genus = SylphGenusCleaner.genusFromSylphLine(tax);
			System.out.println(tax + " "+ genus);
				
			if(genus.length() > 0 )
			{
				int x=0;
				while(sToken.hasMoreTokens())
				{
					String key = genus + "@" + sampleNames.get(x);
					Double oldVal = map.get(key);
					x++;
					
					if(oldVal == null)
						oldVal = 0.0;
					
					Double newVal = Double.parseDouble(sToken.nextToken()) * 11069562;
					
					newVal = newVal + oldVal;
					
					map.put(key, newVal);
					
				}
			}
		}

		for(String s : map.keySet())
		{
			flatFile.write("Sylph");
			
			sToken = new StringTokenizer(s, "@");
			String genus = sToken.nextToken();
			String sample = sToken.nextToken();
			
			flatFile.write("\t" + sample + "\t" + genus + "\t" + map.get(s) + "\n");
		}
		
		flatFile.flush();
		reader.close();
		System.out.println("Wrote 16s");
		
	}
	
	private static void addKrakenMax1(BufferedWriter flatFile, HashSet<String> filters, String outID) throws Exception
	{
		// outer key is genus@
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader =
			    Files.newBufferedReader(new File("C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\paired_counts_table.tsv").toPath(), 
			    		StandardCharsets.UTF_8);
		
		StringTokenizer topToken = new StringTokenizer(reader.readLine(), "\t");

		topToken.nextToken();
		List<String> genusNames = new ArrayList<String>();
		List<String> fullNames = new ArrayList<String>();

		while(topToken.hasMoreTokens())
		{
			String fullName =topToken.nextToken();
			fullNames.add(fullName);
			String genus = Cleaner.toGenus(fullName); ;
			
			genusNames.add(genus);
			
			
		}
		
		if(genusNames.size() != fullNames.size())
			throw new Exception("logic error");
		
		for( int x=0; x < genusNames.size() ; x++)
			System.out.println(genusNames.get(x) + " " + fullNames.get(x));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String id = sToken.nextToken();
			
			int x=0;
			
			while(sToken.hasMoreTokens())
			{
				if( filters == null || filters.contains(fullNames.get(x)))
				{	
					//if( filters == null &&  genusNames.get(x).equals("Eggerthella"))
						//System.out.println("FOUND Eggerthella in unfiltered");
					
					//if( filters != null && genusNames.get(x).equals("Eggerthella") )
						//System.out.println(fullNames.get(x) + " " + genusNames.get(x));
					
					String key = genusNames.get(x) + "@" + id;
					Double oldVal = map.get(key);
					x++;
					
					if(oldVal == null)
						oldVal = 0.0;
					
					Double newVal = Double.parseDouble(sToken.nextToken());
					
					newVal = newVal + oldVal;
					
					map.put(key, newVal);
				}
				else
				{
					sToken.nextToken();
					x++;
				}
			}
		
		}
		
		reader.close();
		for(String s : map.keySet())
		{
			flatFile.write(outID);
			
			StringTokenizer sToken = new StringTokenizer(s, "@");
			String genus = sToken.nextToken();
			String sample = sToken.nextToken();
			
			flatFile.write("\t" + sample + "\t" + genus + "\t" + map.get(s) + "\n");
		}
		
		flatFile.flush();
		System.out.println("Kraken Max 1");
	}
	
	private static void add16SMap( BufferedWriter flatFile) throws Exception
	{
		// outer key is genus@
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader =
			    Files.newBufferedReader(new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\genus.txt").toPath(), 
			    		StandardCharsets.UTF_8);
		
		
		reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine());
		List<String> sampleNames = new ArrayList<String>();
		
		sToken.nextToken();
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
				sampleNames.add(sToken.nextToken().replaceAll("\"", "").trim());
		
		System.out.println(sampleNames);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			sToken = new StringTokenizer(s, "\t");
			
			String tax = sToken.nextToken();
			
			if( tax.indexOf(";g__") != -1)
			{
				String genus = null;
				
				//from chatGPT
				for (String part : tax.split(";")) 
				{
				    if (part.startsWith("g__")) 
				    {
				        genus = part.substring(3);
				        break;
				    }
				}
				
				genus = genus.trim();
				
				genus = TaxonCleaners.clean16SLabel(genus);
				
				genus = genus.replaceAll("[\\r\\n]", "");
				
				if(genus.length() > 0 )
				{
					int x=0;
					while(sToken.hasMoreTokens())
					{
						String key = genus + "@" + sampleNames.get(x);
						Double oldVal = map.get(key);
						x++;
						
						if(oldVal == null)
							oldVal = 0.0;
						
						Double newVal = Double.parseDouble(sToken.nextToken());
						
						newVal = newVal + oldVal;
						
						map.put(key, newVal);
						
					}
				}
			}
		}

		for(String s : map.keySet())
		{
			flatFile.write("16S_Dada2");
			
			sToken = new StringTokenizer(s, "@");
			String genus = sToken.nextToken();
			String sample = sToken.nextToken();
			
			flatFile.write("\t" + sample + "\t" + genus + "\t" + map.get(s) + "\n");
		}
		
		flatFile.flush();
		reader.close();
		System.out.println("Wrote 16s");
	}
}
