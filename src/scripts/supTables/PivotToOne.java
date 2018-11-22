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
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t"+ topSplits[x]);
		
		writer.write("\n");
		
		reader = new BufferedReader(new FileReader(new File("c:\\temp\\table1.txt")));
		
		reader.readLine();
		
		String[] splits= reader.readLine().split("\t");
		
		String key = splits[0];
		
		writer.write(key + "\t" + splits[1] + "\t" + splits[2]);
		
		List<Double> innerList = taxaMap.get(key);
		
		if(innerList== null)
			throw new Exception("Could not find " + key);
		
		for( int x=1; x < innerList.size(); x++)
		{
			writer.write("\t" + innerList.get(x));
		}
		
		writer.write("\n");
		
		reader.close();
		
		writer.flush();  writer.close();
		
	}
}
