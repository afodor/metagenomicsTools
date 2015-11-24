package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import utils.ConfigReader;

public class RemoveSparse
{
	public static void main(String[] args) throws Exception
	{
		getIncludeColumns();
	}
	
	private static HashSet<String> getIncludeColumns() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "bitScoreOrthologsAsColumns.txt"
				)));
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int[] numOverZero = new int[firstSplits.length];
		
		int numSamples = 0;
		for( String s = reader.readLine(); s != null ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != firstSplits.length)
				throw new Exception("No");
			
			for( int x=1; x < splits.length; x++)
				if( Float.parseFloat(splits[x]) > 0 )
					numOverZero[x] = numOverZero[x] + 1;
			
			numSamples++;
		}
		
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\blah.txt")));
		
		for( int x=1; x < numOverZero.length; x++)
			writer.write( (((float) numOverZero[x]) / numSamples) + "\n" );
		
		writer.flush(); writer.close();
		
		return null;
	}
}
