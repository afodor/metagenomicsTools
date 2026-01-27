package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import parsers.OtuWrapper;

public class TaxaByTaxaPredictions
{
	private static HashSet<String> getReliableTaxa() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\krakenMax1Corrs.txt")));
		
		reader.readLine();
		
		boolean first = true;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			String parentTaxa = sToken.nextToken();
			double depth = Double.parseDouble(sToken.nextToken());
			double rho = Double.parseDouble(sToken.nextToken());
			
			if( first || (depth >= 1000 && rho < 0.9 ))
			{
				set.add(parentTaxa);
			}
			first = false;
		}
		
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> reliableMap = getReliableTaxa();
		System.out.println(reliableMap);
		
		HashMap<String, Double> lookupMap = BackgroundSubtract.getLookupMap();
		
		OtuWrapper wrapper = 
				new OtuWrapper("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\predictedVsActual.txt")));
		
		writer.write("parentTaxa\tchildTaxa\tpredictedCount\tactualCount\n");

		double readDepth = wrapper.getTotalCounts();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\krakenMax1Corrs.txt")));
		
		reader.readLine();
		
		boolean first = true;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			String parentTaxa = sToken.nextToken();
			double depth = Double.parseDouble(sToken.nextToken());
			double rho = Double.parseDouble(sToken.nextToken());
			
			if( first || (depth >= 1000 && rho < 0.9 ))
			{
				for( int x=0; x < wrapper.getOtuNames().size(); x++)
				{
					String childTaxa = wrapper.getOtuNames().get(x);
					
					if( ! parentTaxa.equals(childTaxa) && !reliableMap.contains(childTaxa))
					{
						writer.write(parentTaxa + "\t" + childTaxa);
						
						Double val = lookupMap.get( parentTaxa + "@"+ childTaxa );
						
						if( val == null)
							val =0.0;
						
						double predicted = depth * val;
						
						double actual = wrapper.getNumberOfSequencesForOTU(childTaxa);
						
						writer.write("\t" +  predicted + "\t" + (actual/wrapper.getSampleNames().size()) + "\n" );
					}
				}
				
				first = false;
			}
			
			System.out.println(parentTaxa);
		}
		
		writer.flush(); writer.close();
		
		
		reader.close();
	}
}
