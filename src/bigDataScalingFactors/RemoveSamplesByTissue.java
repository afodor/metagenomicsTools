package bigDataScalingFactors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class RemoveSamplesByTissue
{
	private static HashSet<String> getIncluded() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + "June24_risk" 
						+ File.separator + "study_1939_mapping_file.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			if(splits[23].equals("stool"))
				set.add(splits[0].trim());
		}
		
		reader.close();
		
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + 
				File.separator + "July_StoolRemoved" + File.separator +"risk_PL_rawCountsTaxaAsColumnns.txt");
		
		HashSet<String> set = new HashSet<String>();
		HashSet<String> duplicates = new HashSet<String>();
		
		for(String s: wrapper.getSampleNames())
		{
			if( set.contains(s.trim()))
				duplicates.add(s.trim());
			
			set.add(s.trim());
		}
		
		HashSet<String> included = getIncluded();
		
		for(String s : included)
			if( ! set.contains(s))
				System.out.println("Could not find " + s);
		
		
	}
}	
