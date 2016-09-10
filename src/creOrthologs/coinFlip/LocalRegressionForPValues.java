package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;
import utils.Regression;

public class LocalRegressionForPValues
{
	public static final int REGRESSION_WIDTH = 11;
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, List<Holder>> map = getContigMap();
		 
		 for(String s : map.keySet())
		 {
			 System.out.println( s + " " + map.get(s).size());
		 }
		 
		 addRegressions(map);
		 writeResults(map);
	}
	
	private static void writeResults( HashMap<String, List<Holder>> map  ) 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getBioLockJDir() + File.separator + 
							"resistantAnnotation" + File.separator + 
								"genesWithRanksPlusSlopes.txt")));
		writer.write("geneName\tcontig\tstart\tstop\taveragePValue\tslope\tannotation\n");
		for(String contig : map.keySet())
			
		{
			for(Holder h : map.get(contig))
			{
				if( ! h.contig.equals(contig))
					throw new Exception("No");
				
				writer.write(h.geneName  + "\t");
				writer.write(h.contig + "\t");
				writer.write(h.start+ "\t");
				writer.write(h.stop + "\t");
				writer.write(h.averagePValue + "\t");
				writer.write(h.regressionSlope + "\t");
				writer.write(h.annotation + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String geneName;
		String contig;
		int start;
		int stop;
		double averagePValue;
		private String annotation;
		
		Double regressionSlope;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return this.start -arg0.start;
		}
	}
	
	private static Regression getLocalRegression( List<Holder> list, int index )
		throws Exception
	{
		Regression r = new Regression();
		
		List<Double> xList = new ArrayList<Double>();
		List<Double> yList = new ArrayList<Double>();
		
		for( int x=index; x<=index + REGRESSION_WIDTH; x++)
		{
			Holder h = list.get(x);
			xList.add( new Double( h.start));
			yList.add(new Double(h.averagePValue));
		}
		
		r.fitFromList(xList, yList);
		return r;
	}
	
	private static void addRegressions(HashMap<String, List<Holder>> map) throws Exception
	{
		for(String contig : map.keySet())
		{
			List<Holder> list = map.get(contig);
			
			if( list.size() > REGRESSION_WIDTH)
			{
				for( int x=0; x < list.size() - REGRESSION_WIDTH - 1; x++)
				{
					Regression r = getLocalRegression(list, x);
					Holder h = list.get(x);
					h.regressionSlope = r.getB();
				}
			}
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
