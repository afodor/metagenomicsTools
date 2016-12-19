package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class SameSamples
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeSep2015Dir() + File.separator + 
				"tengteng_December_2019" + File.separator + "Genus level data.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			if( set.contains(splits[0]))
				throw new Exception("No");
			
			set.add(splits[0].trim().replaceAll("\"", ""));
			System.out.println("Set1 " + splits[0]);
		}
		
		reader.close();
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + "spreadsheets" + File.separator + 
		"genus" + "asColumnsLogNormalPlusMetadata.txt")));
		
		HashSet<String> set2 = new HashSet<String>();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String key = s.split("_")[0];
			
			if( key.startsWith("D5"))
			{
				System.out.println("Set2 " + key);
				set2.add(key.trim().replaceAll("\"", ""));
			}
				
		}
		
		int numInCommon =0;
		
		for( String s : set2)
			if( set.contains(s))
				numInCommon++;
		
		System.out.println(numInCommon + " " + set2.size() + " " + set.size());
	}
}
