package bigDataScalingFactors.sim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteResampled
{
	private static final Random RANDOM = new Random(342321);
	
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
		List<Integer> list = wrapper.getSamplingList(sampleID);
		writeResampledFile(wrapper, list);
	}
	
	private static void writeResampledFile(OtuWrapper wrapper, 
				List<Integer> resampledList) throws Exception
	{

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + 
				File.separator + "dirk" 
				+ File.separator + "resample" + File.separator + 
				"resampled.txt"
				)));
		
		writer.write( "sample" );
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			writer.write("\t" + wrapper.getOtuNames().get(x));

		writer.write("\n");
		
		for( int x=0;x < wrapper.getSampleNames().size(); x++)
		{
			int depth = wrapper.getCountsForSample(x);
			System.out.println( x + " depth= " + depth);
			writer.write("sample_" + depth);
			int[] counts = resample(wrapper, resampledList, depth);
			
			for( int y=0; y < counts.length; y++)
				writer.write("\t" + counts[y]);
			
			writer.write("\n");
			writer.flush();
		}
		
		writer.close();
		
	}
	
	public static int[] resample(OtuWrapper wrapper, List<Integer> resampleList, int depth) throws Exception
	{
		int[] a = new int[wrapper.getOtuNames().size()];
		Collections.shuffle(resampleList, RANDOM);
		
		for( int x=0; x < depth; x++)
			a[resampleList.get(x)]++;
		
		return a;
	}
	
		
	
}
