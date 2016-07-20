package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class MakeBinFile
{
	public static float increment =0.005f;
	
	public static class Holder
	{
		double sum=0;
		int n=0;
		double low;
		double high;
	}

	private static List<Holder> getHolderList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		double start = 0;
		
		while( start < 1.00f)
		{
			Holder h = new Holder();
			h.low = start;
			h.high = start + increment;
			start += increment;
			
			list.add(h);
		}
		
		return list;
	}
	
	private static void populateList(List<Holder> list) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + 
				File.separator + "resistantAnnotation" + File.separator + "resistantVsSuc_kneu.txt")));
		
		reader.readLine();
		
		int index=0;
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			double val = Double.parseDouble(splits[6]);
			Holder h = findOne(list, val);
			h.n++;
			h.sum += -Math.log10( Double.parseDouble(splits[5]));
			
			index++;
			
			if( index % 100000 == 0)
				System.out.println(index);
		}
		
		reader.close();
	}
	
	// todo: This is brute force and inefficient taking o(n) for what should be o(log(n))
	private static Holder findOne(List<Holder> list, double val) throws Exception
	{
		for(Holder h : list)
			if( val >= h.low && val <= h.high)
				return h;
		
		throw new Exception("Could not find " + val);
	}
	
	private static void writeResults(List<Holder> list ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBioLockJDir() + 
				File.separator + "resistantAnnotation" + File.separator + "resistantVsSucSummary.txt")));
		
		writer.write("low\thigh\tn\taverage\n");
		
		for(Holder h : list)
			if( h.n > 0 )
				writer.write(h.low + "\t" + h.high + "\t" + h.n + "\t" + (float)(h.sum/h.n) + "\n");
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getHolderList();
		
		for(Holder h : list)
			System.out.println(h.low +" " + h.high);
		
		populateList(list);
		writeResults(list);
		
	}
}
