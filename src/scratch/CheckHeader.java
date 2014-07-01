package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import utils.ConfigReader;

public class CheckHeader
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getBigDataScalingFactorsDir() + 
				File.separator + "July_StoolRemoved" + File.separator +"risk_PL_raw_countsTaxaAsColumns.txt")));
		
		HashSet<String> set = new HashSet<String>();
		
		String[] splits = reader.readLine().split("\t");
		
		for(String s : splits)
		{
			if( set.contains(s))
				System.out.println("Duplciate " + s);
			
			set.add(s);
		}
		
		System.out.println(set);
	}
}
