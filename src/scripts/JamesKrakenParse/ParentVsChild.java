package scripts.JamesKrakenParse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;
import utils.Pearson;
import utils.Spearman;

public class ParentVsChild
{
	private static class Holder implements Comparable<Holder>
	{
		int index;
		double count;
		Double corrWithParent;
		Integer parentIndex;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(o.count , this.count);
		}
	}
	
	private static List<Double> getTaxa( OtuWrapper wrapper, int index ) throws Exception
	{
		List<Double> list = new ArrayList<>();
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
			list.add(wrapper.getDataPointsUnnormalized().get(x).get(index));
		
		return list;
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
		
		for( int x=1; x < list.size(); x++)
		{
			Holder childHolder = list.get(x);
			List<Double> childData = getTaxa(wrapper, childHolder.index);
			
			for( int y=0;  y < x; y++ )
			{
				Holder parentHolder = list.get(y);
				List<Double> parentData = getTaxa(wrapper, parentHolder.index);
				
				double spearman = Spearman.getSpearFromDouble(childData, parentData).getRs();
				
				if( childHolder.corrWithParent == null || childHolder.corrWithParent < spearman )
				{
					childHolder.corrWithParent = spearman;
					childHolder.parentIndex = parentHolder.index;
				}
			}
		}
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		File topDir = new File("C:\\JamesKraken");
		
		OtuWrapper wrapper = new OtuWrapper(topDir.getAbsolutePath() 
				+ File.separator + "WGS_Kraken2_Vanderbilt_Forward_2020Oct02_taxaCount_phylum.tsv");
		
		List<Holder> list = getSortedHolders(wrapper);
		
		for(Holder h : list )
			System.out.println( h.index + " " + h.count + " "+  h.parentIndex  + " " + h.corrWithParent);
	}
}
