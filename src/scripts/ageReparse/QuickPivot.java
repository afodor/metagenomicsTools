package scripts.ageReparse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class QuickPivot
{
	private static class Holder
	{
		Double pValueGloor;
		Double pValueGoodrich;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		
		addToMap(map, new File("C:\\anhDraft\\rPValuesOutGloor.txt"), true);

		addToMap(map, new File("C:\\anhDraft\\rPValuesOutGoodrich.txt"), false);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\anhDraft\\gloorVsGoodrich.txt")));
		
		writer.write("names\tpValuesGloor\tpValuesGoodrich\n");
		
		for(String s : map.keySet())
		{
			writer.write( s );
			
			Holder h = map.get(s);
			
			
			writer.write("\t" + getValOrNA(h.pValueGloor) + "\t" +  getValOrNA(h.pValueGoodrich) + "\n");
		}
		
		writer.flush();  writer.close();
		
		
	}
	
	private static String getValOrNA(Double d)
	{
		if( d == null)
			return "";
		
		return d.toString();
	}
	
	private static void addToMap(HashMap<String, Holder> map,  File file, boolean isGloor) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			if( splits.length != 2)
				throw new Exception("No");
			
			String key= splits[0].replaceAll("\"","");
			
			Holder h = map.get(key);
			
			if( h == null)
			{
				h = new Holder();
				
				map.put(key, h);
			}
			
			if( isGloor )
			{
				if( h.pValueGloor != null)
					throw new Exception("No");
				
				h.pValueGloor = Double.parseDouble(splits[1]);
			}
			else
			{
				if( h.pValueGoodrich != null)
					throw new Exception("No");
				
				h.pValueGoodrich = Double.parseDouble(splits[1]);
			}
		}
		
		reader.close();
	}
}
