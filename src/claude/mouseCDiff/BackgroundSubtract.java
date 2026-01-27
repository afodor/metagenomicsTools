package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import parsers.OtuWrapper;

public class BackgroundSubtract
{

	public static void main(String[] args) throws Exception
	{

		HashMap<String, Double> lookupMap = getLookupMap();
	
		OtuWrapper initialData = new OtuWrapper( new File( 
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv") );
		
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
						
						double newVal = initialData.getDataPointsUnnormalized().get(x).get(z)
												- subtractVal;
						
						if( newVal <0.0 )
							newVal = 0.0;
						
						initialData.getDataPointsUnnormalized().get(x).set(z, newVal);
						
					}				
			}
			
		}
		
		initialData.writeUnnormalizedDataToFile(new File(
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_backSubtracted.tsv"));
		}
		
	}
	
	// key is sourceGenome@targetGenome
	private static HashMap<String, Double> getLookupMap() throws Exception
	{
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\relative_abundance_1879x_predicted.tsv.txt");
		
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
			
			System.out.println(firstToken);
			
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
