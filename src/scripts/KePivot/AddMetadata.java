package scripts.KePivot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\kePivot\\genusAsColumns.txt");
		
		File logNormFile = new File("C:\\kePivot\\genusAsColumnsLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
		
		HashMap<String, Double> map = getMeanMap();
		
		//for(String s : map.keySet())
		//	System.out.println(s + " " + map.get(s));
		
		BufferedReader reader = new BufferedReader(new FileReader(logNormFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\kePivot\\genusAsColumnsLogNormPlusMeta.txt")));
		
		writer.write("sample\tmeanCalProtectin");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1 ; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0]);
			
			Double aVal = map.get(splits[0]);
			
			if( aVal == null)
				writer.write("\tNA");
			else
				writer.write("\t" + aVal);
			
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
	}
	
	@SuppressWarnings({"resource" })
	private static HashMap<String, Double> getMeanMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\kePivot\\gvhd_calprotectin_results.tsv")));
		
		reader.readLine();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			//System.out.println("IN S " + s);
			String[] splits =s.split("\t");
			
			if( splits.length == 3)
			{
				String key = splits[0];
				
				if( map.containsKey(key))
					throw new Exception("Duplicate");
				
				map.put(key, Double.parseDouble(splits[2]));
			}
		}
		
		
		reader.close();
		
		return map;
	}
}
