package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class SplitAndPivotQiimeResults
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + "rg_results" 
				+ File.separator + 
					"LyteSharon_r01_cr.txt")));
		
		File dataOnlyFile = 
				new File(
						ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
						"rg_results" + File.separator + 
						"LyteSharon_r01_crDataOnly.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataOnlyFile));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
			writeAllButOne(s, writer);
		
		writer.flush(); writer.close();
	}
	
	private static void writeAllButOne(String s, BufferedWriter writer) throws Exception
	{
		String[] splits = s.split("\t");
		
		writer.write(splits[0]);
		
		for( int x=1 ; x < splits.length - 1; x++)
		{
			writer.write("\t" + splits[x]);
		}
		
		writer.write("\n");
	}
}
