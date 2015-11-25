package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pca.CovarianceMatrixDistanceMeasure;
import pca.PCA;

import utils.ConfigReader;



public class PCAPivot
{
	@SuppressWarnings("unused")
	private static HashMap<Integer, Integer> getNumDays() 
	{
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		map.put(1, 2);
		map.put(2, 2);
		map.put(3, 2);
		map.put(4, 12);
		map.put(5, 12);
		map.put(6,12);
		map.put(7, 18);
		map.put(8, 18);
		map.put(9, 18);
		map.put(10, 18);
		map.put(11,18);
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
						"pivotedSamplesAsColumns.txt")));
		
		List<String> samples = new ArrayList<String>();
		
		String firstList = reader.readLine();
		
		String[] splits = firstList.split("\t");
		
		for( int x=1; x < splits.length; x++)
			samples.add(splits[x]);
		
		int numGenes =0;
		
		for( String s = reader.readLine();
					s != null;
						s = reader.readLine());
			numGenes++;
			
		reader.close();
		
		reader = new BufferedReader(new FileReader(
				new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
						"pivotedSamplesAsColumns.txt")));
		
		reader.readLine();
		
		double[][] d= new double[numGenes][samples.size()];
		
		for( int x=0;x < numGenes; x++)
		{
			splits = reader.readLine().split("\t");
			
			for( int y=0; y<samples.size(); y++)
				d[x][y] = Math.log(Double.parseDouble(splits[y+1]+1));
		}
		
		List<String> catHeaders = new ArrayList<String>();
		//catHeader.add("sampleId");
		List<List<String>> categories = new ArrayList<List<String>>();
		//categories.add(samples);
		
		File outFile = 
			new File(ConfigReader.getJobinLabRNASeqDir() + File.separator
				+"pcaOut.txt");
		System.out.println("Writing " + outFile.getAbsolutePath());
		PCA.writePCAFile(samples, catHeaders, categories,
				d, outFile, new CovarianceMatrixDistanceMeasure()
				);
			
	}
}
