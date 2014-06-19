package bigDataScalingFactors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parsers.OtuWrapper;
import pca.CorrelationMatrixDistanceMeasure;
import pca.PCA;
import utils.ConfigReader;

public class PCA_Pivot
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
				+ File.separator + 
			"riskRawTaxaAsColumn.txt");
		
		System.out.println(wrapper.getSampleNames().size() + " " + wrapper.getOtuNames().size());
			
		double[][] d=  wrapper.getAsArray();
			
		List<String> numSequences = new ArrayList<String>();
		List<String> sampleIDs = new ArrayList<String>();
		for(String s : wrapper.getSampleNames())
		{
			System.out.println(s);
			sampleIDs.add(s);
			numSequences.add("" + wrapper.getCountsForSample(s));
		}
			
		List<String> catHeaders = new ArrayList<String>();
		catHeaders.add("NumSequences"); 
			
		List<List<String>> categories = new ArrayList<List<String>>();
			
		categories.add(numSequences); 
		
		File outFile = new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
				+ File.separator + 
			"PCA_NotNormalizedPearson.txt");
			
		PCA.writePCAFile(sampleIDs, catHeaders, categories,d, outFile, new CorrelationMatrixDistanceMeasure());
	}
}
