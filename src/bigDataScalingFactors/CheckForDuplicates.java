package bigDataScalingFactors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import utils.ConfigReader;

public class CheckForDuplicates
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"ttuLyte_70_mergedReads_PL_raw_counts.txt")));
		
		reader.readLine();
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( set.contains(splits[0]))
				throw new Exception("Duplicate " + splits[0]);
			else
				System.out.println(splits[0]);
			
			set.add(splits[0]);
		}
		
		System.out.println("FINISHED");
	}
}
