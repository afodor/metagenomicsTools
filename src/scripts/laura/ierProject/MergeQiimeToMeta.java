package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class MergeQiimeToMeta
{
	
	public static void main(String[] args) throws Exception
	{
		File relativeAbundnaceFile = new File(ConfigReader.getLauraDir() + File.separator + 
				"IER_Project" + File.separator + "taxa_summary" + File.separator + 
					"otu_table_no_singletons_no_chimeras_L6.txt");
		
	
		List<String> sampleId = getSampleIDs(relativeAbundnaceFile);
	}
	
	private static List<String> getSampleIDs(File inFile) throws Exception
	{
		List<String> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] firstLine = reader.readLine().split("\t");
		
		for( int x=1; x < firstLine.length; x++)
			list.add(firstLine[x]);
		
		reader.close();
		
		return list;
	}
}
