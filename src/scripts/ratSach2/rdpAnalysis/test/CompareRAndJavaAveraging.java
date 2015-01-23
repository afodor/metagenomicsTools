package scripts.ratSach2.rdpAnalysis.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class CompareRAndJavaAveraging
{
	public static void main(String[] args) throws Exception
	{
		
		String[] levels = { "phylum","class","order","family","genus"};
		String[] tissues = { "Cecal Content", "Colon content" };
		
		for(String level : levels)
			for( String tissue : tissues )
		{
			System.out.println(level + " " + tissue);
			BufferedReader reader= new BufferedReader(new FileReader(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
							File.separator + "pValuesForTime_taxa_" +  tissue + "_" + level +".txt"
					)));
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s = reader.readLine())
			{
				String[] splits = s.split("\t");
				
				double lowAvg = getAverage(level, tissue, splits[0].replaceAll("\"", ""), "Low");
				
				if( Math.abs(lowAvg-Double.parseDouble(splits[3])) > 0.0001) 
					throw new Exception("No");
				
				double highAvg = getAverage(level, tissue, splits[0].replaceAll("\"", ""), "High");
				
				if( Math.abs(highAvg-Double.parseDouble(splits[2])) > 0.0001) 
					throw new Exception("No");
				
			}
		}
		
		System.out.println("Global pass");
	}
	
	private static double getAverage(String level, String tissue, String taxa, String highLo)
		throws Exception
	{
		taxa = taxa.replaceAll("\\.", " ");
		
		if( taxa.equals("Escherichia Shigella"))
			taxa = "Escherichia/Shigella";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" +
						File.separator + "sparseThreeColumn_" + level +
						 "_AsColumnsLogNormalizedPlusMetadata.txt")));
		
		String firstLine = reader.readLine();
		
		int taxaIndex = -1;
		
		String[] topSplits= firstLine.split("\t");
		
		for( int x=0; x < topSplits.length; x++)
			if( topSplits[x].equals(taxa))
				taxaIndex = x;
		
		if( taxaIndex == -1)
			throw new Exception("Could not find " + level + " " + tissue +  " " + taxa);
		
		double sum =0;
		double n=0;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			//System.out.println(splits[1] + " " + splits[2]);
			if( splits[1].equals(highLo) && splits[2].equals(tissue))
			{
				n++;
				sum += Double.parseDouble(splits[taxaIndex]);
			}
		}
		
		reader.close();
		
		if(n ==0 )
			throw new Exception("Could not find " + level + " " + tissue +  " " + taxa);
		
		return sum / n;
	}
}
