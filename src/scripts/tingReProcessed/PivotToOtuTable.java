package scripts.tingReProcessed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotToOtuTable
{
	private static void writePivotTable(  HashMap<String, List<Integer>> map, String[] sampleNames,
			int aLevel) throws Exception
	{
		List<String> taxaList = new ArrayList<String>(map.keySet());
		Collections.sort(taxaList);
		
		File outFile = new File(ConfigReader.getTingDir() + File.separator +  "may_2017_rerun" 
				+ File.separator + "otuAsColumns_rerun_" + aLevel + ".txt");
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sample");
		
		for(String s : taxaList)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=1; x < sampleNames.length; x++)
		{
			writer.write(sampleNames[x]);
			
			for(String s : taxaList)
			{
				writer.write( "\t" +  map.get(s).get(x));
			}
			
			writer.write("\n");
			writer.flush();
		}
		
		OtuWrapper wrapper = new OtuWrapper(outFile);
		wrapper.writeNormalizedLoggedDataToFile(new File(ConfigReader.getTingDir() 
				+ File.separator +  "may_2017_rerun" 
				+ File.separator + "otuAsColumnsLogNorm_rerun_" + aLevel + ".txt"));
	}
	
	public static void main(String[] args) throws Exception
	{
		pivotForALevel(2);
		pivotForALevel(6);
	}
	
	public static void pivotForALevel(int aLevel) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getTingDir() 
				+ File.separator + "may_2017_rerun" + File.separator + 
				"20170512_Casp11_DSS_5groups_16S_DeNovo_NoPhiX_NoPrimerSeq_L" + aLevel + ".txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			System.out.println(s);
		
		HashMap<String, List<Integer>> map = new HashMap<String,List<Integer>>();
		
		for(int x=0; x < 17; x++)
			reader.readLine();
		
		String s = reader.readLine();
		
		while( s.trim().length() > 0 )
		{
			System.out.println(s);
			
			String[] splits = s.split("\t");
			
			if(splits.length != topSplits.length)
				throw new Exception("No");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("NO");
			
			List<Integer> list =new ArrayList<Integer>();
			
			map.put(key, list);
			list.add(-1);
			
			for( int x=1; x < splits.length; x++)
				list.add(Integer.parseInt(splits[x].replace("\"", "").replace(",", "")));
			
			s= reader.readLine();
		}
		
		reader.close();
		writePivotTable(map, topSplits, aLevel);
	}
}
