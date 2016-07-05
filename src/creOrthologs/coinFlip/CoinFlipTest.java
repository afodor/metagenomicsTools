package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CoinFlipTest
{
	private static class Holder 
	{
		private int stop;
		private int start;
		
		Holder(int start, int stop)
		{
			this.stop = stop;
			this.start = start;
			
			if( this.stop < this.start)
			{
				int temp = this.stop;
				this.stop = this.start;
				this.start = temp;
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<Holder>> map = parseGTFFile();
		List<BinHolder> list = parseBinFile();
		
			
	}
	
	private static class BinHolder
	{
		private double low;
		private double high;
		
		private double average;
	}
	
	private static List<BinHolder> parseBinFile() throws Exception
	{
		List<BinHolder> list = new ArrayList<BinHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + 
		File.separator + "resistantAnnotation" + File.separator + "resistantVsSucSummary.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			BinHolder h = new BinHolder();
			h.low = Double.parseDouble(splits[0]);
			h.high = Double.parseDouble(splits[1]);
			h.average = Double.parseDouble(splits[3]);
			list.add(h);
		}
			
		
		return list;
	}
	
	private static HashMap<String, List<Holder>> parseGTFFile() throws Exception
	{
		HashMap<String, List<Holder>> map = new LinkedHashMap<String, List<Holder>>();
		
		BufferedReader reader =new BufferedReader(new FileReader(
			ConfigReader.getBioLockJDir() + File.separator + 
				"resistantAnnotation" + File.separator + 
						"klebsiella_pneumoniae_chs_11.0.genes.gtf"	));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = new StringTokenizer(splits[8], ";").nextToken();
			key = key.replace("gene_id", "").replaceAll("\"", "");
			
			List<Holder> list = map.get(key);
			
			if( list == null)
			{
				list = new ArrayList<Holder>();
				map.put(key,list);
			}
			
			Holder h = new Holder(Integer.parseInt(splits[3]), Integer.parseInt(splits[4]));
			list.add(h);
		}
		
		reader.close();
		
		return map;
	}
}
