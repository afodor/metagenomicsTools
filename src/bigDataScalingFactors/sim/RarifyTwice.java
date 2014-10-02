package bigDataScalingFactors.sim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class RarifyTwice
{
	private static Random RANDOM = new Random(2213);
	
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
		
		System.out.println(sampleID + " " + numCounts);
		List<Integer> list = wrapper.getSamplingList(sampleID);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + 
				File.separator + "dirk" + File.separator + "resample" 
				+ File.separator + "rarifiedTwice.txt"
				)));
		
		writer.write("numSeqsPerSample\trichnessOnce\trichnessTwice\n");
		
		for( int x=0;x < wrapper.getSampleNames().size(); x++)
		{
			int initialDepth = wrapper.getCountsForSample(x);
			writer.write(initialDepth + "\t");
			int[] sampledOnce =  WriteResampled.resample(wrapper, list, initialDepth);
			writer.write(getRichness(sampledOnce) + "\t");
			int[] sampledTwice = resample(sampledOnce, minNumCounts);
			writer.write(getRichness(sampledTwice) + "\n");
			writer.flush();
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static int[] resample(int[] in, int depth) throws Exception
	{
		int[] newArray = new int[in.length];
		
		List<Integer> aList = new ArrayList<Integer>();
		for( int x=0; x < in.length; x++)
			for( int y=0; y < in[x]; y++)
					aList.add(x);
		
		Collections.shuffle(aList, RANDOM);
		
		for( int x=0; x < depth; x++)
			newArray[aList.get(x)]++;
		
		return newArray;
	}
	
	private static int getRichness(int[] i)
	{
		int x=0;
		
		for( Integer anInt : i )
			if( anInt > 0 )
				x++;
		
		return x;
	}
}
