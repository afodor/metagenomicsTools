package creOrthologs.pcaCluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class FilterBitScores
{
	public static final int MIN_NUM = 5;
	
	private static List<Boolean> getIncludeList( File inFile ) throws Exception
	{
		List<Float> list = new ArrayList<Float>();
		
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=0;x  < splits.length; x++) 
			list.add(0.0f);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			if( splits[0].indexOf("pneu") != -1 )
			{
				// first token is ignored
				for( int x=1; x < splits.length; x++)
					if( Float.parseFloat(splits[x].replace("\"","")) > 0)
						list.set(x, list.get(x)+ 1);
			}
		}
			
		List<Boolean> includeList = new ArrayList<Boolean>();
		
		int numIncluded =0;
		
		for( int x=0; x < list.size(); x++)
		{
			if( list.get(x) >= MIN_NUM )
			{
				includeList.add(true);
				numIncluded++;
			}
			else
			{
				includeList.add(false);
			}
				
		}
			
		reader.close();
		
		System.out.println("Returning with " + numIncluded);
		return includeList;
	}
	
	public static void main(String[] args) throws Exception
	{
		int numColumns = 3000;
		
		File inFile = new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "bitScoreOrthologsAsColumns.txt");
		
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer=  new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + "pcaCluster" + 
						File.separator + "sampledBitScore_" + numColumns + "_bitScoreOrthologsKPneuOnly.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		List<Boolean> includeList = getIncludeList(inFile);
		
		for( int x=0; x < splits.length; x++)
		{
			if ( x < numColumns)
				includeList.add(true);
			else
				includeList.add(false);
		}
		
		writer.write("genome");
		
		for( int x=0; x < splits.length; x++)
			if( includeList.get(x))
				writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			if( splits[0].indexOf("pneu") != -1 )
			{
				writer.write(splits[0]);
				

				for( int x=0; x < splits.length; x++)
					if( includeList.get(x))
						writer.write("\t" + splits[x]);
				
				writer.write("\n");
				
				
			}
		}
			
		writer.flush();  writer.close();
		reader.close();
	}
}
