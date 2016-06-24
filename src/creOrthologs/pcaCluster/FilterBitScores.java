package creOrthologs.pcaCluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import utils.ConfigReader;

public class FilterBitScores
{
	public static void main(String[] args) throws Exception
	{
		int numColumns = 3000;
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + "bitScoreOrthologsAsColumns.txt"	)));
		
		BufferedWriter writer=  new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + "pcaCluster" + 
						File.separator + "sampledBitScore_" + numColumns + "_bitScoreOrthologsKPneuOnly.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		List<Boolean> includeList = new ArrayList<Boolean>();
		
		for( int x=0; x < splits.length; x++)
		{
			if ( x < numColumns)
				includeList.add(true);
			else
				includeList.add(false);
		}
		
		Collections.shuffle(includeList, new Random(32341));
		
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
