package bigDataScalingFactors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class CheckForDuplicates
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
						+ File.separator + 
					"riskRawTaxaAsColumn.txt")));
		
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
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" 
						+ File.separator + 
					"riskRawTaxaAsColumn.txt");
		
		set = new HashSet<String>();
		
		for(String s : wrapper.getOtuNames())
		{
			if( set.contains(s))
				throw new Exception("NO");
			
			set.add(s);
		}
		
		
		System.out.println("FINISHED");
	}
}
