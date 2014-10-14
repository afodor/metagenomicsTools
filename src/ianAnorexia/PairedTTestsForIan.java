package ianAnorexia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import parsers.OtuWrapper;
import utils.Avevar;
import utils.ConfigReader;
import utils.TTest;

public class PairedTTestsForIan
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getIanAnorexiaDir() + File.separator + "anSamplesTaxaAsColumns.txt");
		
		 HashMap<Integer, Holder> map = getHolders(wrapper);
		
		 List<OtuHolder> list = getOTUList(map, wrapper);
		 writeResults(list);
	}
	
	private static void writeResults(List<OtuHolder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getIanAnorexiaDir() + File.separator + 
				"pairedTTestByTime.txt")));
		
		writer.write("otuID\tavgAD\tavdDS\tprevelance\tpValue\tbhFDR\n");
		
		float minPrev = 0.25f;
		float n =0;
		
		for( OtuHolder otu : list)
			if( otu.getPrevelance() >= minPrev)
				n++;
		
		int rank =1;
		for(OtuHolder otu : list )
			if( otu.getPrevelance() >= minPrev)
			{
				writer.write(otu.otuName + "\t");
				writer.write(new Avevar(otu.adSamples).getAve() + "\t");
				writer.write(new Avevar(otu.disSamples).getAve() + "\t");
				writer.write(otu.getPrevelance() + "\t");
				writer.write(otu.pValue + "\t");
				writer.write( (otu.pValue * n / rank) + "\n");
				rank++;
			}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder
	{
		List<Double> ad = null;
		List<Double> dis = null;
	}
	
	private static class OtuHolder implements Comparable<OtuHolder>
	{
		String otuName;
		double pValue = 1;
		
		List<Double> adSamples =  new ArrayList<Double>();
		List<Double> disSamples = new ArrayList<Double>();
		
		@Override
		public int compareTo(OtuHolder arg0)
		{
			return Double.compare(this.pValue, arg0.pValue);
		}
		
		public double getPrevelance()
		{
			double num=0;
			
			for( Double d : adSamples )
				if(d > 0)
					num++;
			
			for( Double d : disSamples)
				if( d > 0 )
					num++;
			
			return num/ (adSamples.size() + disSamples.size());
		}
	}
	
	private static List<OtuHolder> getOTUList(HashMap<Integer, Holder> map, OtuWrapper wrapper) throws Exception
	{
		List<OtuHolder> list = new ArrayList<OtuHolder>();
		
		List<Integer> keys = new ArrayList<Integer>();

		int numOTUS =wrapper.getOtuNames().size();
		
		
		for( Integer key : map.keySet())
		{
			Holder h = map.get(key);
			
			if( h.ad != null && h.dis != null)
			{
				keys.add(key);
				
				if( h.ad.size() != numOTUS || h.dis.size() != numOTUS)
					throw new Exception("Logic error");
			}
				
		}
		
		Collections.sort(keys);
		
		for( int x=0; x <numOTUS; x++)
		{
			OtuHolder o =  new OtuHolder();
			o.otuName = wrapper.getOtuNames().get(x);
			list.add(o);
			
			for( Integer i : keys)
			{
				Holder h = map.get(i);
				o.adSamples.add(h.ad.get(x));
				o.disSamples.add(h.dis.get(x));
			}
			
			try
			{
				o.pValue = TTest.pairedTTest(o.adSamples, o.disSamples).getPValue();
			}
			catch(Exception ex)
			{
				
			}
		}
		
		Collections.sort(list);
		return list;
	}
	
	
	
	private static Holder getOrCreate(int id, HashMap<Integer, Holder> map) throws Exception
	{
		Holder h = map.get(id);
		
		if( h != null)
			return h;
		
		h = new Holder();
		map.put(id, h);
		return h;
	}
	
	private static void addARow(String sampleID, List<Double> list, OtuWrapper wrapper)
		throws Exception
	{
		int sampleInt = wrapper.getIndexForSampleName(sampleID);
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			list.add(wrapper.getDataPointsNormalizedThenLogged().get(sampleInt).get(x));
	}
	
	private static HashMap<Integer, Holder> getHolders( OtuWrapper wrapper ) throws Exception
	{
		HashMap<Integer, Holder> map = new LinkedHashMap<Integer, Holder>();
		
		for(String s : wrapper.getSampleNames())
		{
			
			if( s.endsWith("ad") || s.endsWith("dis"))
			{
				Integer key = Integer.parseInt(s.replaceAll("ad", "").replace("dis", ""));
				Holder h= getOrCreate(key, map);
				
				if( s.endsWith("ad"))
				{
					h.ad = new ArrayList<Double>();
					addARow(s, h.ad, wrapper);
				}
				else if ( s.endsWith("dis"))
				{
					h.dis = new ArrayList<Double>();
					addARow(s, h.dis, wrapper);
				}
				else throw new Exception("Logic error");
			}
		}
		
		return map;
	}
}
