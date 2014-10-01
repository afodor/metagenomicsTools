package bigDataScalingFactors.sim;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteResampled
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + 
				File.separator + "dirk" 
				+ File.separator +
				"may2013_refOTU_Table-subsetTaxaAsColumnsstoolOnly.filtered.txt");
		
		int sampleID = wrapper.getSampleIdWithMostCounts();
		int numCounts = wrapper.getCountsForSample(sampleID);
		
		int minSampleID = wrapper.getSampleIdWithMinCounts();
		int minNumCounts = wrapper.getCountsForSample(minSampleID);
		System.out.println(sampleID + " " + numCounts);
		System.out.println(minSampleID + " " + minNumCounts);
	}
}
