package scripts.ChinaMerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class MergeMetabolitesToTaxaPlusMetadata
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getChinaDir() + File.separator + "metabolites"	 +
		File.separator + "metabolitesAsColumns.txt")));
		
		List<String> metaboliteNames = new ArrayList<String>();
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(int x=2; x < topSplits.length; x++)
			metaboliteNames.add(topSplits[x]);
		
		reader.close();
	}
}
