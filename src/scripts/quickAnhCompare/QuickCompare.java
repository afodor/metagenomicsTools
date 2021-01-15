package scripts.quickAnhCompare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import parsers.OtuWrapper;

public class QuickCompare
{
	private static class Holder
	{
		double rdpAverage =0;
		
		double qiimeCounts =0;
		double qiimeAverage = 0;
		
		@Override
		public String toString()
		{
			return "rdpAverage : " + this.rdpAverage + " qiime average " + qiimeAverage;
		}
		
	}
	
	private static void writeResults(HashMap<String, Holder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\Temp\\compare.txt")));
		
		writer.write("taxa\trdpAvg\tqiimeAvg\n");
		
		for(String s : map.keySet())
			writer.write(s + "\t" + map.get(s).rdpAverage + "\t" + map.get(s).qiimeAverage + "\n");
		
		writer.flush(); writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getRDPMap();
		addQiimeMap(map);

		System.out.println( map );
		writeResults(map);
	}
	
	private static void addQiimeMap( HashMap<String, Holder>  map ) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("c:\\Temp\\gloor_feature-table.txt"));
		
		reader.readLine();
		int numSamples = reader.readLine().split("\t").length -1;
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != numSamples +1)
				throw new Exception("No");
			
			String[] nameSplits = splits[0].split(";");
			
			if(  ! nameSplits[nameSplits.length-1].equals("__"))
			{
				if( nameSplits[nameSplits.length-1].indexOf("D_5__") == -1)
					throw new Exception("No " + nameSplits[nameSplits.length-1] + " " + s);
				
				String genus = nameSplits[nameSplits.length-1].replace("D_5__", "");
				genus = genus.replace("Unclassified ", "").trim();
				

				if( genus.equals("Prevotella 9"))
					genus= "Prevotella";
				
				
				Holder h = map.get(genus);
				
				if( h == null)
				{
					h = new Holder();
					map.put(genus,h);
				}
				
				for( int x=1; x < splits.length; x++)
				{
					h.qiimeCounts += Double.parseDouble(splits[x]);
				}
			}
		}
		
		reader.close();
		
		for( Holder h : map.values())
			if( h.qiimeCounts >0 )
				h.qiimeAverage = ((double)h.qiimeCounts) / numSamples;
	}
	
	private static HashMap<String, Holder> getRDPMap() throws Exception
	{
		OtuWrapper wrapper1 = new OtuWrapper("c:\\Temp\\gloor_amoss_2_2020Sep11_taxaCount_genus.tsv");
		
		HashMap<String, Holder> map = new LinkedHashMap<String, QuickCompare.Holder>();
		
		for( int x=0; x < wrapper1.getOtuNames().size(); x++)
		{
			String taxaName = wrapper1.getOtuNames().get(x);
			String mappedTaxaName= taxaName.replace("Unclassified ", "").trim();
			
			Holder h = map.get(mappedTaxaName);
			
			if( h== null)
			{
				h = new Holder();
				map.put(mappedTaxaName, h);
			}
			
			h.rdpAverage += ((double) wrapper1.getCountsForTaxa(taxaName)) / wrapper1.getOtuNames().size();
		}
		
		return map;
	}
}
