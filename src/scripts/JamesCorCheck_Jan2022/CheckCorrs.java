package scripts.JamesCorCheck_Jan2022;

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
		File inFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\Normalized_Tables\\China\\Kraken2_China_genus_Normalized.csv");
		File outFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\AF_Out\\Kraken2_China_genus_Normalized_Corr.txt");
		
		//writeCorrFile(inFile, outFile,2);
		
		inFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\Normalized_Tables\\China\\Metaphlan2_China_genus_Normalized.csv");
		outFile = new File("C:\\Users\\afodor\\git\\Ranking_WGS_Classifier\\AF_Out\\Metaphlan2_China_genus_Normalized_CorrTopCorr.txt");
		
		//writeCorrFile(inFile, outFile,2);
		
		inFile = new File("C:\\JamesKraken\\chinaKraken.txt");
		
		OtuWrapper wrapper = new OtuWrapper(inFile);
		
		File logFile = new File("C:\\JamesKraken\\chinaKrakenLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logFile);
		
		outFile = new File("C:\\JamesKraken\\chinaKrakenLogNormCorrTopCorr.txt");
	
		writeCorrFile(logFile, outFile,1, "\t");
	
		//logFile = new File("C:\\JamesKraken\\china16S.txt");
		
		//outFile = new File("C:\\JamesKraken\\china16SLogNormCorrTopCorr.txt");
	
		//writeCorrFile(logFile, outFile,1, "\t");
	
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
					xHolder.maxCorrelation = corValue;
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
