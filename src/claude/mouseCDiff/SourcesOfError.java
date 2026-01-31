package claude.mouseCDiff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import claude.mouseCDiff.GenomeCountsByTaxon.TaxonGenomeCounts;

public class SourcesOfError
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\relative_abundance_1879x_predicted.tsv.txt");
		HashMap<String, Double> lookupMap = BackgroundSubtract.getLookupMap(inFile);
		
		GenomeCountsByTaxon gct = 
				GenomeCountsByTaxon.load(
					Path.of("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\errorModeling\\genome_counts_by_taxon.tsv"));
		
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
		
		writer.write("queryID\tgenus\toverallQueryError\toverallQueryErrorOutsdieOfGenus\t" + 
		"targetError\ttargetErrorOutsideOfGenus\t" + 
		"numberOfGenomes\t"
				+ "numberOfPlasmidsInGenomeFiles\tMBGenomes\tMBPlasmid\ttotalMB\tpctCompleteGenome\n");
		
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
			
			double targetError =0;
			double targetErrorOutsideOfGenus =0;
			
			for(String s2 : querySet)
			{
				if( ! s1.equals(s2))
				{
					String genus2 = TaxonCleaners.cleanWgsNameOrGenus(s2);
					String key = s2 + "@" + s1;
					
					Double val = lookupMap.get(key);
					
					if( val != null)
					{
						targetError += val;
						
						if( ! genus1.equals(genus2))
						{
							targetErrorOutsideOfGenus += val;
						}
					}
				}
			}
			
			writer.write(s1 + "\t" + genus1 + "\t" + queryError + "\t" + queryErrorOutsideOfGenus + "\t");
			writer.write(targetError + "\t" + targetErrorOutsideOfGenus + "\t");
			TaxonGenomeCounts b = gct.get(s1);
			
			if( b != null)
			{
				writer.write(b.getNumberOfGenomes() + "\t" + b.getNumberOfPlasmidsInGenomeFiles() + "\t" + 
						b.getMbGenomes() + "\t" + b.getMbPlasmid() + "\t" + b.getTotalMB() + "\t"+ 
								b.getPctCompleteGenome() + "\n");
			}
			else
			{

				writer.write("NA\tNA\tNA\tNA\tNA\tNA\n");	
			}
		}
		
		
		writer.flush(); writer.close();
		System.out.println("Finished");
	}
}
