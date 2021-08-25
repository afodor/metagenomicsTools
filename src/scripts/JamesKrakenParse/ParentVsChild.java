package scripts.JamesKrakenParse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;

public class ParentVsChild
{
	private static class Holder implements Comparable<Holder>
	{
		int index;
		double count;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(o.count , this.count);
		}
	}
	
	private static List<Holder> getSortedHolders(OtuWrapper wrapper ) throws Exception
	{
		List<Holder> list = new ArrayList<>();
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			double count  = wrapper.getCountsForTaxa(x);
			
			Holder h = new Holder();
			h.count = count;
			h.index = x;
			
			list.add(h);
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		File topDir = new File("C:\\JamesKraken");
		
		OtuWrapper wrapper = new OtuWrapper(topDir.getAbsolutePath() 
				+ File.separator + "WGS_Kraken2_Vanderbilt_Forward_2020Oct02_taxaCount_phylum.tsv");
		
		List<Holder> list = getSortedHolders(wrapper);
		
		for( Holder h : list)
			System.out.println( h.index + " " + h.count );
	}
}
