package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class RemoveRareTaxa
{
	public static void main(String[] args) throws Exception
	{
		File inCounts = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
				"qiimeSummary" + File.separator +  
				"diverticulosis_closed_otu_AsColumns.txt");
		
		File outCounts = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
				"qiimeSummary" + File.separator +  
				"diverticulosis_closed_otu_AsColumnsRareTaxaRemoved.txt");
		
		
		List<Boolean> inList = getIncludeList(inCounts);
		
		for(Boolean b : inList)
			System.out.println(b);
	
	}
	
	private static List<Boolean> getIncludeList(File inFile) throws Exception
	{
		List<Double> fractionList= new ArrayList<Double>();
		
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			fractionList.add(0.0);
		
		int sampleSize =0;
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			for( int x=1; x < splits.length; x++)
				if( Integer.parseInt(splits[x]) > 0 )
					fractionList.set(x-1, fractionList.get(x-1) + 1);
			
			sampleSize++;
		}
		
		reader.close();
		
		List<Boolean> boolList = new ArrayList<Boolean>();
		
		for( int x=1; x < topSplits.length; x++)
		{
			boolList.add(  fractionList.get(x-1) / sampleSize >= 0.25  );
		}
		
		return boolList;
	}
}
