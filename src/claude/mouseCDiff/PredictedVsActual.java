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

public class PredictedVsActual
{
	
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv");
		
		HashMap<String, Double> predictionSums = new HashMap<String, Double>();
		
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\relative_abundance_1879x_predicted.tsv.txt");
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
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\predictedVsActual.txt")));
		
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
