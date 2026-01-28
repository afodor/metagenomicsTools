package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class ComparisonWith16S
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\allGenusFlat.txt"	)));
		
		add16SMap(writer);
		
		writer.flush();  writer.close();
	}
	
	private static void add16SMap( BufferedWriter flatFile) throws Exception
	{
		// outer key is genus@
		HashMap<String, Long> map = new HashMap<String, Long>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\genus.txt")));
		
		reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine());
		List<String> sampleNames = new ArrayList<String>();
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
				sampleNames.add(sToken.nextToken().replaceAll("\"", "").trim());
		
		System.out.println(sampleNames);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			sToken = new StringTokenizer(s, "\t");
		}
				
		reader.close();
	}
}
