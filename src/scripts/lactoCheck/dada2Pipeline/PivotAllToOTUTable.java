package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotAllToOTUTable
{
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getLactoCheckDir() + File.separator + 
				"fastqDemultiplexedRarified" + File.separator + 
				"filteredAllTogether" + File.separator + "allTogetherTable.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		List<String> header = getSequenceHeaders(reader.readLine());
		
		for(String s: header)
			System.out.println(s);
	}
	
	private static List<String> getSequenceHeaders(String s) throws Exception
	{
		s=s.replaceAll("\"", "");
		List<String> list = new ArrayList<>();
		
		StringTokenizer sToken = new StringTokenizer(s);
		
		while(sToken.hasMoreTokens())
			list.add(sToken.nextToken());
		
		return list;
	}
}
