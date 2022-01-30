package scripts.JamesCorCheck_Jan2022;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CheckCorrs
{
	private static class Holder
	{
		String taxaName;
		List<Double> taxaValues = new ArrayList<>();
		double maxCorrelation = 1;
		
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
	
	public static void writeCorrFile(File inputFile, File outputFile) throws Exception
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
		
		for(Holder h : taxaList)
			System.out.println(h.taxaName + " " + h.taxaValues);
		
		reader.close();
	}
}
