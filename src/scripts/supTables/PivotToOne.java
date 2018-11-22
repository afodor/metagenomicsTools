package scripts.supTables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PivotToOne
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"c:\\temp\\table2.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			System.out.println(s);
		
		// key is taxa
		HashMap<String, List<Double>> taxaMap = new LinkedHashMap<>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( topSplits.length != splits.length)
				throw new Exception("No");
			
			if(taxaMap.containsKey(splits[0]))
					throw new Exception("No");
			
			List<Double> innerList = new ArrayList<>();
			taxaMap.put(splits[0],innerList);
			
			for(int x=1; x < splits.length; x++)
			{
				innerList.add(Double.parseDouble(splits[x]));
			}
		}
		
		reader.close();
		
		BufferedWriter writer =new BufferedWriter( new FileWriter(new File( "c:\\temp\\merged.txt")));
		
		writer.write("id\tpfs\tos");
		
		for( String taxaName : taxaMap.keySet())
			writer.write("\t"+ taxaName.replaceAll(" ", "_").replaceAll(";", "@"));
		
		writer.write("\n");
		
		HashMap<String, Holder> metaMap =getMetaMap();
		
		for( int x=1; x < topSplits.length; x++)
		{
			writer.write(topSplits[x].replaceAll(" ", "_").replaceAll(";", "@"));
			
			Holder h = metaMap.get(topSplits[x]);
			
			if( h == null)
				throw new Exception("No " + topSplits[x]);
			
			writer.write("\t" + h.pfs + "\t" + h.os );
			
			for(String taxaName : taxaMap.keySet())
				writer.write("\t" + taxaMap.get(taxaName).get(x-1));
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		
	}
	
	private static HashMap<String, Holder> getMetaMap() throws Exception
	{
		HashMap<String, Holder> map =new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("c:\\temp\\table1.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits= s.split("\t");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			Holder h = new Holder();
			h.os = Integer.parseInt(splits[2]);
			h.pfs = Integer.parseInt(splits[1]);
			map.put(key, h);
			
		}
		
		reader.close();
		return map;
	}
	
	private static class Holder
	{
		int pfs;
		int os;
	}
}
