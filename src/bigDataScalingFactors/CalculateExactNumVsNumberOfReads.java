package bigDataScalingFactors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.Spearman;

public class CalculateExactNumVsNumberOfReads
{
	public static void main(String[] args) throws Exception
	{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + File.separator +  "dirk" + File.separator + 
				"spearmansForExactCount.txt"
				));
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
				+ File.separator + "dirk" + File.separator + 
			"may2013_refOTU_Table-subsetTaxaAsColumns.filtered.txt");
				
		writer.write("numSequences\tSpearmanR\tspearmanRSquared\tspearmanP\tminusLog10PValue\n");
		
		List<Double> numSequences = new ArrayList<Double>();
		
		for( int z=0; z < wrapper.getSampleNames().size(); z++)
		{
			double numSeqs = wrapper.getNumberSequences(wrapper.getSampleNames().get(z));
			numSequences.add(Math.log10( numSeqs+1 ));	
		}
		
		for( int x=0; x < 1000; x++)
		{
			System.out.println(x);
		
			List<Double> numMatching = new ArrayList<Double>();
			for( int z=0; z < wrapper.getSampleNames().size(); z++)
			{
				numMatching.add( getNumMatching(wrapper,z,x));
			}
			
			Spearman mySpear = Spearman.getSpearFromDouble(numSequences, numMatching);
			writer.write(x + "\t" + mySpear.getRs() + "\t" + (mySpear.getRs() *mySpear.getRs() ) 
					+ "\t" +  mySpear.getProbrs() + "\t" +
						-Math.log10(mySpear.getProbrs()) + "\n"	);
			writer.flush();
		}
			
		writer.flush();  writer.close();
	}
	
	private static double getNumMatching(OtuWrapper wrapper, int sampleNum, int numToMatch) throws Exception
	{
		double num =0;
		
		for( int y=0; y < wrapper.getOtuNames().size(); y++)
		{
			double aVal = wrapper.getDataPointsUnnormalized().get(sampleNum).get(y);
			
			if( aVal > numToMatch -0.001 && aVal < numToMatch + 0.001)
				num++;
		}
		
		return num;
	}
}
