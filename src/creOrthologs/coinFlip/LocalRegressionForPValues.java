package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class LocalRegressionForPValues
{
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, List<Holder>> map = getContigMap();
		 
		 for(String s : map.keySet())
		 {
			 System.out.println( s + " " + map.get(s).size());
			 
			 for(Holder h : map.get(s))
			 {
				 System.out.println("\t" + h.start + "\t" + h.averagePValue + "\t" + h.annotation );
			 }
		 }
			 
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String geneName;
		String contig;
		int start;
		int stop;
		double averagePValue;
		private String annotation;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return this.start -arg0.start;
		}
	}
	
	/*
	 * key is the contig.
	 * This has many so many dependencies that it will probably be faster just
	 * to recode from scratch then figure them all out...
	 */
	private static HashMap<String, List<Holder>> getContigMap() throws Exception
	{
		HashMap<String, List<Holder>> outerMap = 
				new HashMap<String,List<Holder>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + File.separator + 
					"resistantAnnotation" + File.separator + "genesWithRanks.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String contig = splits[1];
			
			List<Holder> list = outerMap.get(contig);
			
			if( list == null )
			{
				list = new ArrayList<Holder>();
				outerMap.put(contig,list);
			}
			
			Holder h = new Holder();
			list.add(h);
			h.geneName = splits[0];
			h.contig = contig;
			h.start = Integer.parseInt(splits[2]);
			h.stop =Integer.parseInt(splits[3]);
			h.averagePValue = Double.parseDouble(splits[6]);
			h.annotation = splits[11];
			
		}
		
		for(List<Holder> list: outerMap.values())
			Collections.sort(list);
		
		return outerMap;
	}
}
