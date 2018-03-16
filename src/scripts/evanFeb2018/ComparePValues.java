package scripts.evanFeb2018;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;

import utils.ConfigReader;

public class ComparePValues
{
	private static class Holder
	{
		Double rdpPValue;
		Double qiimePValue;
	}
	
	public static void main(String[] args) throws Exception
	{	
		HashMap<String, Holder> map = parseFirstFile();
		addSecondFile(map);
		writeResults(map);
	}
	
	private static String getBlankOrVal(Double s)
	{
		if( s== null)
			return "";
		
		return s.toString();
	}
	
	private static void writeResults(HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getEvanFeb2018Dir() + File.separator + 
					"qiimeVsRDP_genus.txt")));
		
		writer.write("name\trdp\tqiime\n");
		
		for(String s : map.keySet())
		{
			Holder h= map.get(s);
			
			writer.write(s + "\t" + getBlankOrVal(h.rdpPValue) + "\t" + getBlankOrVal(h.qiimePValue) + "\n" );
		}
		
		writer.flush(); writer.close();
		
	}
	
	private static void addSecondFile(HashMap<String, Holder> map) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getEvanFeb2018Dir() + File.separator + "spreadsheets" + 
						File.separator + 
					"pValues_genus.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			Holder h = map.get(splits[0]);
			
			if( h == null)
			{
				h = new Holder();
				map.put(splits[0], h);
			}
			
			if( h.rdpPValue != null) 
				throw new Exception("No");
			
			h.rdpPValue = Double.parseDouble(splits[1]);
		}
	}
	
	private static HashMap<String, Holder> parseFirstFile() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		
		BufferedReader reader= new BufferedReader(new FileReader(ConfigReader.getEvanFeb2018Dir() + 
				File.separator + "Qiime_EoE_genus_Case_Control_models.tsv"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s =s.replaceAll("\"", "");
			String[] splits= s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			Holder h = new Holder();
			h.qiimePValue = Double.parseDouble(splits[1]);
			map.put(splits[0], h);
			
		}
		
		return map;
	
	}
}
