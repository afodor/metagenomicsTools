package claude.kraken2871;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import claude.mouseCDiff.BackgroundSubtract;
import parsers.OtuWrapper;

public class PredictedVsActual
{
	
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper(
			"C:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\paired_counts_table.tsv");
		
		HashMap<String, Double> predictionSums = new HashMap<String, Double>();
		
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_2871\\genome2Cross\\counts_table_normalized.tsv");
		HashMap<String, Double> lookupMap = BackgroundSubtract.getLookupMap(inFile);
		
		for(int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			String parentTaxa = wrapper.getOtuNames().get(x);
			double depth = wrapper.getNumberOfSequencesForOTU(parentTaxa);
			System.out.println( x + " " + wrapper.getOtuNames().size() +" " + parentTaxa);
				
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				String childTaxa = wrapper.getOtuNames().get(y);
					
				if( ! parentTaxa.equals(childTaxa) )
				{
					Double val = lookupMap.get( parentTaxa + "@"+ childTaxa );
						
					if( val == null)
						val =0.0;
						
					double predicted = depth * val;
					Double sum = predictionSums.get(childTaxa);
						
					if( sum == null)
						sum = 0.0;
						
					sum = sum + predicted;
					predictionSums.put(childTaxa, sum);
						
				}
			}
				
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("c:\\claudeSummary\\simonCrossRho\\kraken_2871\\simonData\\predictedVsActual_2871_max1.txt")));
		
		writer.write("taxa\tpredictedCount\tactualCount\n");
		
		for(String s: predictionSums.keySet())
		{
			writer.write(s + "\t");
			writer.write(predictionSums.get(s) + "\t");
			writer.write(wrapper.getCountsForTaxa(s) + "\n");
		}
		
		writer.flush(); writer.close();
	}
}
