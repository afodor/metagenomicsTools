package ruralVsUrban.metabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		
		reader = new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir() + 
				File.separator + "phylum_taxaAsColumnsLogNorm_WithMetadata.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getChinaDir() + File.separator + "metabolites" + 
		File.separator + "mergedMetabolitesPhylumWithMedata.txt")));
		
		writer.write(reader.readLine());
		
		for(String s : metaboliteNames)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine() )
		{
			writer.write(s);
			
			Integer key = Integer.parseInt(s.split("\t")[2]);
			
			List<Double> list = map.get(key);
			
			if( list == null)
				throw new Exception("No");
			
			for( Double d : list)
				writer.write("\t" + d);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
