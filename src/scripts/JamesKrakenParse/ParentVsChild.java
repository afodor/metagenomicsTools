package scripts.JamesKrakenParse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;
import utils.Spearman;

public class ParentVsChild
{
	private static class Holder implements Comparable<Holder>
	{
		int index;
		double count;
		Double corrWithParent;
		Holder parentHolder;
		
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
			if( wrapper.getOtuNames().get(x).toLowerCase().indexOf("unclassified") == -1)
			{
				double count  = wrapper.getCountsForTaxa(x);
				
				Holder h = new Holder();
				h.count = count;
				h.index = x;
				list.add(h);
			}
		}
		
		Collections.sort(list);
		
		for( int x=1; x < list.size(); x++)
		{
			System.out.println(x +  " " + list.size());
			Holder childHolder = list.get(x);
			List<Double> childData = getTaxa(wrapper, childHolder.index);
			
			for( int y=0;  y < x; y++ )
			{
				Holder parentHolder = list.get(y);
				List<Double> parentData = getTaxa(wrapper, parentHolder.index);
				
				double spearman = Spearman.getSpearFromDouble(childData, parentData).getRs();
				spearman = spearman * spearman;
				
				if( childHolder.corrWithParent == null || childHolder.corrWithParent < spearman )
				{
					childHolder.corrWithParent = spearman;
					childHolder.parentHolder= parentHolder;
				}
			}
		}
		
		return list;
	}
	
	private static void writeResults(List<Holder> list, String level, OtuWrapper wrapper) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\JamesKraken\\"+ level + "Vanderbilt.txt")));
		
		writer.write("childName\tparentName\tchildAbundance\tparentAbundance\trSquared\t");
		
		for(Holder h : list)
		{
			writer.write(wrapper.getOtuNames().get(h.index) );
			
			if( h.parentHolder== null)
				writer.write("\tNA");
			else
				writer.write("\t" + wrapper.getOtuNames().get(h.parentHolder.index));
			
			writer.write("\t" + h.count);
			
			if( h.corrWithParent == null)
				writer.write("\tNA\tNA\n");
			else
				writer.write("\t" + h.parentHolder.count + "\t"  + h.corrWithParent + "\n");
			
		}
		
		writer.flush();  writer.close();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		File topDir = new File("C:\\JamesKraken");
		//String level = "phylum";
		
		String level = "genus";
		
		
		OtuWrapper wrapper = new OtuWrapper(topDir.getAbsolutePath() 
				+ File.separator + "WGS_Kraken2_Vanderbilt_Forward_2020Oct02_taxaCount_" + level + ".tsv");
		
		List<Holder> list = getSortedHolders(wrapper);
		
		writeResults(list, level, wrapper);
		
		
	}
}
