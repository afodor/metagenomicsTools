package test.KeTessaGVHD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;

public class AddMeta
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		File inFile = new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_Transposed.txt");
		
		OtuWrapper wrapper = new OtuWrapper(inFile);
		
		System.out.println(wrapper.getTotalCounts());
		System.out.println(wrapper.getSampleNames().size());
		
		File logNorm = new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_TransposedLogNorm.txt");
		
		File metaFile = new File("C:\\\\ke_tessa_test\\\\GVHDProject-main\\\\CountsTables\\\\bracken_species_TransposedLogNormPlusMeta.txt");
		wrapper.writeNormalizedLoggedDataToFile(logNorm);
		
		HashMap<String, String> gvnMap = getAMap(new File("C:\\ke_tessa_test\\GVHDProject-main\\metaGvN.txt"));
		HashMap<String, String> srMap = getAMap(new File("C:\\ke_tessa_test\\GVHDProject-main\\metaSvR.txt"));
		HashMap<String, Double> calProtectinMap= getCalprotectinMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(logNorm));
		BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
		
		String[] topSplits = reader.readLine().split("\t");
		writer.write(topSplits[0] + "\t" + "gvn" + "\t" + "svr" + "\tcalprotectin");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s !=null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			String id = splits[0];
			
			writer.write(id.replaceAll("\\.", "_").replaceAll(" ", "_") + "\t" + gvnMap.get(id) + "\t" + srMap.get(id) + "\t" + calProtectinMap.get(id));

			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
			
			
		}
	
		writer.flush(); writer.close();
		reader.close();
	}
	
	@SuppressWarnings("resource")
	private static HashMap<String, Double> getCalprotectinMap() throws Exception
	{
		BufferedReader reader = new BufferedReader( new FileReader(new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\gvhd_calprotectin_results.tsv")));
		
		reader.readLine();
		
		HashMap<String, Double> map = new HashMap<>();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length == 3)
			{
				String key = splits[0];
				
				if( map.containsKey(key))
					throw new Exception("Duplicate key");
				
				map.put(key, Double.parseDouble(splits[2]));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	@SuppressWarnings("resource")
	private static HashMap<String, String> getAMap(File f) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
			
		}
		
		return map;
	}
}
