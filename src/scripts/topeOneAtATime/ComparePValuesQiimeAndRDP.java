package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class ComparePValuesQiimeAndRDP
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String,Holder>();
		
		addToMap(map, new File(ConfigReader.getTopeOneAtATimeDir() + File.separator + 
						"merged" + File.separator + "metapValuesFor_otu_qiime_cr_read1_.txt"), true);
		

		addToMap(map, new File(ConfigReader.getTopeOneAtATimeDir() + File.separator + 
						"merged" + File.separator + "metapValuesFor_genus_read1_.txt"), false);
		
		writeResults(map);
	}
	
	private static class Holder
	{
		Double qiimePValue = null;
		Double rdpPValue= null;
		
	}
	
	private static void writeResults( HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + "qiimeVsRDPPValues.txt")));
		
		writer.write("taxa\tqiime\trdp\n");
		
		for(String s : map.keySet())
		{
			Holder h = map.get(s);
			
			writer.write(s );
			
			writer.write(h.qiimePValue == null ? "\t" : ("\t" + h.qiimePValue));
			writer.write(h.rdpPValue== null ? "\t\n" : ("\t" + h.rdpPValue+ "\n"));
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addToMap(HashMap<String, Holder> map, File inFile, boolean isQiime)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			Holder h = map.get(splits[0]);
			
			if( h==null)
			{
				h = new Holder();
				map.put(splits[0], h);
			}
			
			double pValue = Math.log10(Double.parseDouble(splits[5]));
			
			if (Double.parseDouble(splits[3]) > Double.parseDouble(splits[4]))
				pValue  = -pValue;
			
			if( isQiime)
				h.qiimePValue = pValue;
			else
				h.rdpPValue = pValue;
		}
	}
}
