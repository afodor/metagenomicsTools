package scripts.ChinaMerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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
		
		HashMap<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Integer key = Integer.parseInt(splits[0]);
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			List<Double> list = new ArrayList<Double>();
			
			map.put(key,list);
			
			for(int x=2; x < splits.length; x++)
				list.add(Double.parseDouble(splits[x]));
			
			if( list.size() != metaboliteNames.size())
				throw new Exception("No");
		}
		
		reader.close();
	}
}
