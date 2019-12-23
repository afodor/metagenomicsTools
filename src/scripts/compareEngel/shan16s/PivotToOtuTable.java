package scripts.compareEngel.shan16s;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import scripts.pancreatitis.PivotOTUs;
import utils.ConfigReader;

public class PivotToOtuTable
{
	public static void main(String[] args) throws Exception
	{
		for( int x=2; x <=6; x++)
			writeALevel(x);
	}
	
	private static void writeALevel(int level) throws Exception
	{
		System.out.println("Level " + level);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getEngelCheckDir() + File.separator + 
				"shan16S" + File.separator + "otu_table_nc_wtax_L" + level  + ".txt")));
		
		HashMap<String, HashMap<String,Long>> map = new HashMap<>();
		
		reader.readLine();
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
		{
			String sampleID = topSplits[x];
			
			if( map.containsKey(sampleID))
				throw new Exception("duplicate");
			
			HashMap<String, Long> innerMap =new HashMap<>();
			
			map.put(sampleID, innerMap);
		}
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			String taxa = splits[0];
			
			for( int x=1; x < topSplits.length; x++)
			{
				HashMap<String, Long> innerMap = map.get(topSplits[x]);
				
				if(innerMap.containsKey(taxa))
					throw new Exception("No");
				
				String aVal = splits[x];
				
				if( aVal.endsWith(".0"))
					aVal = aVal.replace(".0", "");
				
				innerMap.put(taxa, Long.parseLong(aVal) );
			}
			
		}
		
		File outFile =new File(ConfigReader.getEngelCheckDir() + File.separator + 
				"shan16S" + File.separator + "pivoted_L" + level  + ".txt");
		
		PivotOTUs.writeResultsFromLong(map, outFile.getAbsolutePath());
	}
}
