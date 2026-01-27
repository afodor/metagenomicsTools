package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;
import utils.Avevar;
import utils.Spearman;

public class CheckCorrs
{
	private static class Holder implements Comparable<Holder>
	{
		String taxaName;
		List<Double> taxaValues = new ArrayList<>();
		double maxCorrelation = -1;
		Holder parentTaxa = null;
		
		Holder(String s )
		{
			this.taxaName =s ;
		}
		
		double averageVal;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(o.averageVal, this.averageVal);
		}
	}
	
	public static void main(String[] args) throws Exception
	{

		/*
		File inFile = new File("C:\\claudeSummary\\simonCrossRho\\1879\\kraken_1879_counts_table_transposed.tsv");
		File outFile = new File("C:\\claudeSummary\\simonCrossRho\\1879\\kraken_1879_corVals.txt");
		*/

		/*
		File inFile = new File("C:\\claudeSummary\\simonCrossRho\\1879\\kraken_standard_counts_table_min100.tsv");
		File outFile = new File("C:\\claudeSummary\\simonCrossRho\\1879\\kraken_standard_corrs_min100.txt");
		*/
		
		/*
		File inFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_transposed.tsv");
		File outFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\krakenMax1Corrs.txt");
		*/
		
	//	File inFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_backSubtracted.tsv");
		//File outFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\krakenMax1Corrs_backSubtracted.txt");
		

		File inFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\kraken_max1_counts_table_backSubtracted_hardRemove.tsv");
		File outFile = new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\krakenMax1Corrs_hardRemove.txt");
			
		
		writeCorrFile(inFile, outFile,1, "\t");
	
	}
	
	private static List<Holder> parseFile( File inputFile, int startNum, String delimiter ) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		
		List<Holder> taxaList = new ArrayList<>();
		
		String topLine = reader.readLine();
		String [] topSplits= topLine.split(delimiter);
		
		for( int x=startNum; x < topSplits.length; x++)
			taxaList.add(new Holder(topSplits[x]));
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			String[] splits = s.split(delimiter);
			
			if( splits.length != topSplits.length)
				throw new Exception("NO " + topSplits.length + " " + splits.length + " " + s);
			
			for( int x=startNum; x < splits.length; x++)
			{
				Holder h = taxaList.get(x-startNum);
				
				String aVal = splits[x];
				
				if( aVal.equals(""))
					aVal = "0.0";
				
				h.taxaValues.add(Double.parseDouble(aVal));
			}
		}
		
		//for(Holder h : taxaList)
		//	System.out.println(h.taxaName + " " + h.taxaValues);
		
		reader.close();
		
		for( Holder h : taxaList )
		{
			h.averageVal = new Avevar(h.taxaValues).getAve();
		}
		
		Collections.sort(taxaList);
		
		return taxaList;
	}
	
	
	public static void writeCorrFile(File inputFile, File outputFile, int startNum, String delimiter) throws Exception
	{
		System.out.println(inputFile.getAbsolutePath() + " " + outputFile.getAbsolutePath());
		List<Holder> taxaList = parseFile(inputFile, startNum,delimiter);
		
		if( taxaList.size() == 0)
			throw new Exception("Failed to parse!");
		
		for(Holder h : taxaList)
		{
			System.out.println(h.taxaName + " " + h.averageVal);
		}
		
		for( int x=1; x < taxaList.size() ; x++)
		{
			System.out.println(x + " of " + taxaList.size());
			Holder xHolder = taxaList.get(x);
			
			for( int y = 0; y < x; y++)
			{
				Holder yHolder = taxaList.get(y);
				
				double corValue = Spearman.getSpearFromDouble(xHolder.taxaValues, yHolder.taxaValues).getRs();
				
				if( corValue > xHolder.maxCorrelation )
				{
					xHolder.maxCorrelation = corValue;
					xHolder.parentTaxa = yHolder;
				}
					
			}
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		writer.write("taxaName\taverageVal\tmaxCor\tparentTaxa\tparentAverageVal\n");
		
		for( Holder h : taxaList)
		{
			writer.write(h.taxaName + "\t" + 
						new Avevar(h.taxaValues).getAve() + "\t" +  h.maxCorrelation +  "\t"); 
			
			if( h.parentTaxa != null)
				writer.write(h.parentTaxa.taxaName + "\t" + h.parentTaxa.averageVal + "\n");
			else
				writer.write("NA\tNA\n");
		}
		
		writer.flush();  writer.close();
			
	}
}
