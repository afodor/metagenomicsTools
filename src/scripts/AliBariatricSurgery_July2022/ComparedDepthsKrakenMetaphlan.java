package scripts.AliBariatricSurgery_July2022;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import parsers.OtuWrapper;

public class ComparedDepthsKrakenMetaphlan
{
	public static void main(String[] args) throws Exception
	{
		// key is sampleID
		HashMap<String, Holder> map = new LinkedHashMap<>();
		
		addToMap(map, new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\kraken2_raw_genus.txt"), true);
		addToMap(map, new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\metaphlan_raw_genus.txt"), false);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\bariatricSurgery_Daisy\\fromAli_July5_2022\\MetaPhlAn2_count_tables\\AF_OUT\\genusCountsComparison.txt")));
		
		writer.write("sample\tkrakenCounts\tmetaphlanCounts\n");
		
		for( String s : map.keySet() )
		{
			writer.write(s + "\t" + Math.log10( map.get(s).sumKraken+1) + "\t" + Math.log10(map.get(s).sumMetaphlan+1) + "\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static void addToMap( HashMap<String, Holder> map , File file, boolean isKraken ) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(file);
		
		for(String s : wrapper.getSampleNames())
		{
			Holder h = map.get(s);
			
			if( h==null)
			{
				h = new Holder();
				map.put(s,h);
			}
			
			if( isKraken)
			{
				if( h.sumKraken != 0)
					throw new Exception("No");
				
				h.sumKraken = wrapper.getCountsForSample(s);
			}
			else
			{
				if( h.sumMetaphlan != 0)
					throw new Exception("No");
				
				h.sumMetaphlan= wrapper.getCountsForSample(s);
			}
				
		}
	}
	
	private static class Holder
	{
		double sumMetaphlan = 0;
		double sumKraken = 0;
	}
}
