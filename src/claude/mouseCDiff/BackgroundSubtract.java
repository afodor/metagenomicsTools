package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
		OtuWrapper initialData = new OtuWrapper( new File( 
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv") );
		
		System.out.println(initialData.getOtuNames());
		
		for( int x=0; x < initialData.getSampleNames().size(); x++)
		{
			HashMap<String, Double> vals = new HashMap<String, Double>();
			
			for( int y=0; y < initialData.getOtuNames().size(); y++)
				vals.put(initialData.getOtuNames().get(y), 
						initialData.getDataPointsUnnormalized().get(x).get(y));
			
			System.out.println(vals);
			
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
			
			for(String s : vals.keySet())
				System.out.println( s + " " + vals.get(s));

			System.exit(1);
			
		}
		
	}
	
}
