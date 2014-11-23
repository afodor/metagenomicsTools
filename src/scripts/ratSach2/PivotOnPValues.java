package scripts.ratSach2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class PivotOnPValues
{
	private static class Holder
	{
		Double tValueCecum=null;
		Double tValueColon=null;
	}
	
	public static void main(String[] args) throws Exception
	{
		writeResults(getHolders());
	}
	
	private static void writeResults(  HashMap<String, Holder> map )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"mergedTCecumColon.txt")));
		
		writer.write("otu\ttCecum\ttColon\n");
		
		for(String s : map.keySet())
		{
			writer.write(s + "\t");
			
			Holder h = map.get(s);
			
			writer.write( (h.tValueCecum == null ? "" : h.tValueCecum ) + "\t"  );
			writer.write( (h.tValueColon == null ? "" : h.tValueColon) + "\n"  );
		}
		
		writer.flush();  writer.close();
	}
	
	public static HashMap<String, Holder> getHolders() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String, PivotOnPValues.Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"cecum_otus_compFDR.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("NO");
			
			Holder h = new Holder();
			h.tValueCecum = Double.parseDouble(splits[7]);
			map.put(splits[1],  h);
		}
		
		reader.close();
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + 
				"colon_otus_compFDR.txt")));
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Holder h = map.get(splits[1]);
			
			if(h==null)
			{
				h = new Holder();
				map.put(splits[1],  h);
			}
			
			h.tValueColon = Double.parseDouble(splits[7]);
			map.put(splits[1],  h);
		}
		
		
		return map;
	}
}
