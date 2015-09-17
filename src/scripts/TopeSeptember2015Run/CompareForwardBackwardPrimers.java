package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import utils.ConfigReader;

public class CompareForwardBackwardPrimers
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + 
				"Copy of DHSV Illumina Sets 1 and 2 Metadata.txt")));
		
		reader.readLine();
		
		for( String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			set1.add( splits[4] );
			set2.add( splits[9]);
		}
		
		System.out.println(set1);
		System.out.println(set2);
		
		set1.retainAll(set2);
		System.out.println(set1.size());
	}
}
