package claude.mouseCDiff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

public class SourcesOfError
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\relative_abundance_1879x_predicted.tsv.txt");
		HashMap<String, Double> lookupMap = BackgroundSubtract.getLookupMap(inFile);
		
		HashSet<String> querySet = new LinkedHashSet<String>();
		HashSet<String> targetSet = new LinkedHashSet<String>();
		
		for(String s : lookupMap.keySet())
		{	
			StringTokenizer sToken = new StringTokenizer(s, "@");
			String id = sToken.nextToken() ;
			querySet.add(id);
			targetSet.add(id);
			
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\errorBreakdown.txt")));
		
		writer.write("queryID\tgenus\toverallQueryError\toverallQueryErrorOutsdieOfGenus\n");
		
		for(String s1 : querySet)
		{
			String genus1 = TaxonCleaners.cleanWgsNameOrGenus(s1);
			double queryError = 0;
			double queryErrorOutsideOfGenus =0;
			
			for(String s2 : targetSet)
			{
				if( ! s1.equals(s2))
				{
					String genus2 = TaxonCleaners.cleanWgsNameOrGenus(s2);
					String key = s1 + "@" + s2;
					
					Double val = lookupMap.get(key);
					
					if( val != null)
					{
						queryError += val;
						
						if( ! genus1.equals(genus2))
						{
							queryErrorOutsideOfGenus += val;
						}
					}
				}
			}
			
			writer.write(s1 + "\t" + genus1 + "\t" + queryError + "\t" + queryErrorOutsideOfGenus + "\n");
		}
		
		
		writer.flush(); writer.close();
	}
}
