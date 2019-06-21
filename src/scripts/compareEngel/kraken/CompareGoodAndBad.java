package scripts.compareEngel.kraken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class CompareGoodAndBad
{
	public static void main(String[] args) throws Exception
	{
		for( String level :  CompareKraken.LEVELS)
		{
			System.out.println(level);
			
			OtuWrapper good =new OtuWrapper(ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
						"mikeRelease" + File.separator + "kraken_" + level + ".txt");
			
			OtuWrapper bad = new OtuWrapper(ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
						"badCounts" + File.separator + "racialDisparityKraken2_2019Jun10_taxaCount_" + level +  ".tsv");
			
			BufferedWriter writer =new BufferedWriter(new FileWriter(new File( 
					ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
					"badCounts" + File.separator + "compareNewOld_" + level +  ".txt"
					)));
			
			writer.write("sample\ttaxa\toldCounts\tnewCounts\n");
			
			for( String sample : good.getSampleNames())
			{
				System.out.println(sample);
				for(String taxa : good.getOtuNames())
				{
					if(bad.getIndexForOtuName(taxa) != -1 )
					{

						writer.write( sample + "\t" + taxa  );
						double oldCount =bad.getDataPointsUnnormalized().get(bad.getIndexForSampleName(sample)).get(bad.getIndexForOtuName(taxa));
						writer.write("\t" + oldCount );
						double newCount =good.getDataPointsUnnormalized().get(good.getIndexForSampleName(sample)).get(good.getIndexForOtuName(taxa));
						writer.write("\t" + newCount + "\n");
						
					}
					else
					{
						System.out.println("Could not find " + taxa);
					}
					
				}
			}
			
			writer.flush(); writer.close();
		}
	}
}
