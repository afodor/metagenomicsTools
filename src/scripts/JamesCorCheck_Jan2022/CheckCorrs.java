package scripts.JamesCorCheck_Jan2022;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.Avevar;
import utils.Spearman;

public class CheckCorrs
{
	private static class Holder
	{
		String taxaName;
		List<Double> taxaValues = new ArrayList<>();
		double maxCorrelation = -1;
		
		Holder(String s )
		{
			this.taxaName =s ;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\Normalized_Tables\\China\\Kraken2_China_genus_Normalized.csv");
		File outFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\AF_Out\\Kraken2_China_genus_Normalized_Corr.txt");
		
		writeCorrFile(inFile, outFile);
	}
	
	private static List<Holder> parseFile( File inputFile ) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		
		List<Holder> taxaList = new ArrayList<>();
		
		String topLine = reader.readLine();
		String [] topSplits= topLine.split(",");
		
		for( int x=2; x < topSplits.length; x++)
			taxaList.add(new Holder(topSplits[x]));
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			String[] splits = s.split(",");
			
			if( splits.length != topSplits.length)
				throw new Exception("NO " + topSplits.length + " " + splits.length);
			
			for( int x=2; x < splits.length; x++)
			{
				Holder h = taxaList.get(x-2);
				
				h.taxaValues.add(Double.parseDouble(splits[x]));
			}
		}
		
		//for(Holder h : taxaList)
		//	System.out.println(h.taxaName + " " + h.taxaValues);
		
		reader.close();
		
		return taxaList;
	}
	
	
	public static void writeCorrFile(File inputFile, File outputFile) throws Exception
	{
		System.out.println(inputFile.getAbsolutePath() + " " + outputFile.getAbsolutePath());
		List<Holder> taxaList = parseFile(inputFile);
		
		for( int x=0; x < taxaList.size() -1 ; x++)
		{
			
			Holder xHolder = taxaList.get(x);
			
			
			for( int y = x+ 1; y < taxaList.size(); y++)
			{
				Holder yHolder = taxaList.get(y);
				
				double corValue = Spearman.getSpearFromDouble(xHolder.taxaValues, yHolder.taxaValues).getRs();
				
				if( corValue > xHolder.maxCorrelation )
					xHolder.maxCorrelation = corValue;
				
				if( corValue > yHolder.maxCorrelation)
					yHolder.maxCorrelation = corValue;
			}
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		writer.write("taxaName\taverageVal\tmaxCor\n");
		
		for( Holder h : taxaList)
		{
			writer.write(h.taxaName + "\t" + 
						new Avevar(h.taxaValues).getAve() + "\t" +  h.maxCorrelation + "\n");
		}
		
		writer.flush();  writer.close();
			
	}
}
