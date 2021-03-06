package scripts.Pierce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotOTUTable 
{
	private static int controlCount =0;
	
	public static void main(String[] args) throws Exception
	{
		File otuFile = new File(ConfigReader.getPierce2019Dir() + File.separator + 
					"OTUTable.txt");
		
		File pivotFile = new File(ConfigReader.getPierce2019Dir() + File.separator + 
					"taxaAsColumns_OTU.txt");
		
		HashMap<String, List<Integer>> map = getOTUMap(otuFile);
		writePivotedFile(pivotFile, otuFile, map);
		
		File pivotFileLogNorm = new File(ConfigReader.getPierce2019Dir() + File.separator + 
				"taxaAsColumns_OTULogNorm.txt");
		
		OtuWrapper wrapper = new OtuWrapper(pivotFile);
		wrapper.writeNormalizedLoggedDataToFile(pivotFileLogNorm);
		
		String[] taxa = { "phylum", "class" , "order", "family", "genus" };
		
		for( int x=0; x <=4; x++)
		{
			System.out.println(taxa[x]);
			File taxaFile = new File(ConfigReader.getPierce2019Dir() + File.separator + 
					"taxaAsColumns_" + taxa[x] + ".txt");
			
			ParseTaxaAssignment.writePivotedTaxaCounts(wrapper, x, taxaFile);
			
			OtuWrapper taxaWrapper =new OtuWrapper(taxaFile);

			File taxaFileLogNorm = new File(ConfigReader.getPierce2019Dir() + File.separator + 
					"taxaAsColumns_" + taxa[x] + "LogNorm.txt");
			
			taxaWrapper.writeNormalizedLoggedDataToFile(taxaFileLogNorm);
		}
	
	}
	
	private static void writePivotedFile(File pivotFileName, File otuFile, HashMap<String, List<Integer>> map )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(pivotFileName));
		
		List<String> sampleNames = getHeader(otuFile);
		
		List<String> otuNames = new ArrayList<String>(map.keySet());
		
		writer.write("sample");
		
		for(String s : otuNames)
		{
			writer.write("\t" + s);	
		}
			
		writer.write("\n");
		
		for(int x=1; x < sampleNames.size(); x++)
		{
			String sampleName = sampleNames.get(x);
			
			if(sampleName.indexOf("Ctrl") != -1 ) 
			{
				sampleName = sampleName + "_" + controlCount;
				controlCount++;
			}
			
			writer.write( sampleName );
			
			for(String otu : otuNames)
			{
				List<Integer> innerList = map.get(otu);
				
				writer.write("\t" + innerList.get(x));
				
			}
		
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static List<String> getHeader(File otuFile) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(otuFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			list.add(s);
		
		reader.close();
		
		return list;
	}
	
	/*
	 * key is otu id
	 */
	private static HashMap<String, List<Integer>> getOTUMap(File otuFile) throws Exception
	{
		HashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(otuFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			s= s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			if(splits.length != topSplits.length)
				throw new Exception();
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception();
			
			List<Integer> list = new ArrayList<Integer>();
			
			list.add(null);
			
			for( int x=1; x < splits.length; x++)
				list.add(Integer.parseInt(splits[x]));
			
			map.put(key, list);
			
		}
		
		reader.close();
		return map;
	}
	
	
	
}
