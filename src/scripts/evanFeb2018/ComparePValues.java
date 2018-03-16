package scripts.evanFeb2018;

import java.io.FileReader;
import java.io.BufferedReader;
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
