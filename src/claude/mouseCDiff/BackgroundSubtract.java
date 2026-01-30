package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import parsers.OtuWrapper;



public class BackgroundSubtract
{
	
	
	private static HashSet<String> getTaxaToRemove() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\misclass_vs_sartor_reads.tsv")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( Double.parseDouble(splits[1]) >= Double.parseDouble(splits[2]))
				set.add(splits[0]);
		}
		
		reader.close();
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\relative_abundance_1879x_predicted.tsv.txt");
		HashMap<String, Double> lookupMap = getLookupMap(inFile);
	
		OtuWrapper initialData = new OtuWrapper( new File( 
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv") );
		
		HashSet<String> taxaToRemove = getTaxaToRemove();
		System.out.println(taxaToRemove);
		
		System.out.println(taxaToRemove.size());
		
		for( int x=0; x < initialData.getOtuNames().size(); x++)
		{
			if( taxaToRemove.contains(initialData.getOtuNames().get(x) ))
			{
				System.out.println("Zeroing" + initialData.getOtuNames().get(x));
				for( int y=0; y < initialData.getSampleNames().size(); y++)
				{
					initialData.getDataPointsUnnormalized().get(y).set(x, 0.0);
				}
			}
		}
		
		//System.out.println(initialData.getOtuNames());
				
		for( int x=0; x < initialData.getSampleNames().size(); x++)
		{
			System.out.println("Starting " + x + " " +initialData.getSampleNames().size() );
			HashMap<String, Double> vals = new LinkedHashMap<String, Double>();
			
			for( int y=0; y < initialData.getOtuNames().size(); y++)
				vals.put(initialData.getOtuNames().get(y), 
						initialData.getDataPointsUnnormalized().get(x).get(y));
			
			//System.out.println(vals);
			
			// sort descending from ChatGPT
			vals =
				    vals.entrySet()
				        .stream()
				        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				        .collect(Collectors.toMap(
				            Map.Entry::getKey,
				            Map.Entry::getValue,
				            (a, b) -> a,
				            LinkedHashMap::new
				        ));
			
			List<String> sortedTaxaName = new ArrayList<String>();
			for(String s : vals.keySet())
				sortedTaxaName.add(s);
			
			for(int y=0; y < sortedTaxaName.size()-1; y++)
			{
				String sourceTaxa = sortedTaxaName.get(y).replaceAll("\"", "").trim();
				
				for(int z= y+1; z < sortedTaxaName.size(); z++)
					{
						String destTaxa = sortedTaxaName.get(z).replaceAll("\"", "").trim();
						
						String key = sourceTaxa + "@" + destTaxa;
						
						Double proportion = lookupMap.get(key);
						
						if( proportion != null)
						{
							double subtractVal =
									initialData.getDataPointsUnnormalized().get(x).get(y) * 
									proportion;
							
							//if( subtractVal >100)
								//System.out.println("Subtract " + sourceTaxa + " " + destTaxa + " " + subtractVal);
							
							double newVal = initialData.getDataPointsUnnormalized().get(x).get(z)
													- subtractVal;
							
							if( newVal <0.0 )
								newVal = 0.0;
							
							initialData.getDataPointsUnnormalized().get(x).set(z, newVal);
							
						}				
				}
		}
		
		initialData.writeUnnormalizedDataToFile(new File(
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_backSubtracted_hardRemove.tsv"));
		}
		
	}
	
	
	
	// key is sourceGenome@targetGenome
	public static HashMap<String, Double> getLookupMap(File inFile) throws Exception
	{
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(
			    new InputStreamReader(new FileInputStream(inFile), StandardCharsets.UTF_8));
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine(), "\t");
		List<String> topNames = new ArrayList<String>();
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
			topNames.add(sToken.nextToken().replaceAll("\"", "").trim());
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			sToken = new StringTokenizer(s, "\t");
			
			String firstToken = sToken.nextToken();
			
			//System.out.println(firstToken);
			
			int x=0;
			double sum =0;
			
			while( sToken.hasMoreTokens())
			{
				double val = Double.parseDouble(sToken.nextToken());
				
				sum += val;
				
				map.put(firstToken + "@" + topNames.get(x).replaceAll("\"", "").trim(), val);
				x++;
			}
			
			if (sum > 0 )
				if(Math.abs(1.0-sum) > 0.001)
					throw new Exception("Parsing error " + sum + " "+ firstToken ); ;
				
		}
		
		
		return map;
	}
	
}
