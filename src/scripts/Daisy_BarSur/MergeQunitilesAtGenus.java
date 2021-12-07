package scripts.Daisy_BarSur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MergeQunitilesAtGenus
{
	private static class Holder
	{
		Double[] krakenQunits = new Double[5];
		Double[] metaphlanQunits = new Double[5];
		
		
	}
	
	private static void writeResults( HashMap<String, Holder> map   ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\pivotGenusMetaphlanVsKraken.txt" )));
		
		List<String> list = new ArrayList<>(map.keySet());
		Collections.sort(list);
		
		writer.write("taxa\tquintile\tmetaphlan\tkraken\n");
		
		for(String s : list)
		{
			writer.write(s);
			
			Holder h = map.get(s);
			
			for( int x=0; x < 5; x++)
			{
				writer.write("\t" + x);
				
				writer.write("\t" + (h.metaphlanQunits[x] == null ? "NA" : ""+h.metaphlanQunits[x]));
				writer.write("\t" + (h.krakenQunits[x] == null ? "NA" : ""+h.krakenQunits[x])  + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map  =new HashMap<>();
		
		addAFile( map, new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\allPValuesToTimeByQuintileGenusKraken.txt"), true );

		addAFile( map, new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\allPValuesToTimeByQuintileGenusMetaphlan.txt"), false);
		
		writeResults(map);
	}
	
	private static void addAFile( HashMap<String, Holder> map, File f , boolean isKraken ) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			String taxa = splits[2];
			
			Holder h = map.get(taxa);
			
			if( h== null)
			{
				h = new Holder();
				map.put(taxa, h);
			}
			
			Double[] d = isKraken ? h.krakenQunits : h.metaphlanQunits;
			
			int index = Integer.parseInt(splits[1]) -1;
			
			if(  d[index] != null)
				throw new Exception("No");
			
			if( ! splits[0].equals("NA"))
			d[index] = Double.parseDouble(splits[0]);
		}
		
		reader.close();
	}
	
}
