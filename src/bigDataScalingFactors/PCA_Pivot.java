package bigDataScalingFactors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import parsers.OtuWrapper;
import pca.PCA;
import utils.ConfigReader;
import utils.Pearson;
import utils.Regression;

public class PCA_Pivot
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBigDataScalingFactorsDir() + 
				File.separator + "risk" + File.separator + "pcoaLog.txt")));
		
		writer.write("sparsityNum\ttaxaRemoved\ttaxaLeft\tsamplesRemoved\tsamplesLeft\tpValue\trSquared\n");
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
				+ File.separator + 
			"riskRawTaxaAsColumn.txt");
		
		for( int x=50; x>=0; x = x -5)
			writePCOA(writer, x, wrapper);
		
		writer.flush();  writer.close();
	
	}
	
	public static void writePCOA( BufferedWriter writer, int removeSparse, OtuWrapper wrapper) throws Exception
	{	
		System.out.println("removeSparse " + removeSparse);
		System.out.println(wrapper.getSampleNames().size() + " " + wrapper.getOtuNames().size());
		
		HashSet<String> excludedTaxa = new HashSet<String>();
		
		for( int x=0; x< wrapper.getOtuNames().size(); x++)
		{
			if( wrapper.getCountsForTaxa(x) <= removeSparse + 0.001 )
				excludedTaxa.add(wrapper.getOtuNames().get(x));
		}
		
		HashSet<String> excludedSamples = new HashSet<String>();
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			if( wrapper.getCountsForSampleExcludingTheseTaxa(x, excludedTaxa) <= 0.001 )
				excludedSamples.add(wrapper.getSampleNames().get(x));
		}
		
		 wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
					+ File.separator + 
				"riskRawTaxaAsColumn.txt", excludedSamples, excludedTaxa);
		
		
		System.out.println("EXCLUDING " + excludedTaxa);
		System.out.println("EXCLUDING " + excludedSamples);
			
		double[][] d=  wrapper.getAsGeoNormalizedLoggedArray();
			
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
			"PCA_LognormalizedGeoMean_" + removeSparse +  ".txt");
			
		PCA.writePCAFile(sampleIDs, catHeaders, categories,d, outFile);
		
		writer.write(removeSparse + "\t");
		writer.write(excludedTaxa.size() + "\t");
		writer.write(wrapper.getOtuNames().size() + "\t");
		writer.write(excludedSamples.size() + "\t");
		writer.write(wrapper.getSampleNames().size() + "\t");
		
		BufferedReader reader = new BufferedReader(new FileReader(outFile));
		
		reader.readLine();
		
		List<Number> numSequencesParsed = new ArrayList<Number>();
		List<Number> pcoa1 = new ArrayList<Number>();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			numSequencesParsed.add( Math.log10( Double.parseDouble(splits[1])));
			pcoa1.add(Double.parseDouble(splits[2]));
		}
		
		Regression regression = new Regression();
		regression.fitFromList(numSequencesParsed, pcoa1);
		writer.write(Pearson.getPearsonRFromNumber(numSequencesParsed, pcoa1) + "\t");
		writer.write(regression.getPValueForSlope() + "\n");
		writer.flush();
	}
}
