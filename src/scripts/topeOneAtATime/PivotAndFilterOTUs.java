package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


import utils.ConfigReader;

public class PivotAndFilterOTUs
{
	public static void main(String[] args) throws Exception
	{
		File originalFile = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
				"diverticulosisOTUs" + File.separator +  "otus" + File.separator + 
					"tope_otu_2_filteredLowSequenceSamples.txt");
		
		List<String> sampleList = getSampleList(originalFile);
		
		for(String s : sampleList)
			System.out.println(s);
	}
	
	private static List<String> getSampleList(File inFile) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length; x++)
			list.add(splits[x]);
		
		reader.close();
		
		return list;
	}
}
