package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class CompareMDSByID
{
	private static class Holder
	{
		Double read1;
		Double read4;
	}
	
	private static HashMap<String, Holder> getMap() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String,Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"merged" + File.separator + "mds_genusPlusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Holder h = map.get(splits[1]);
			
			if( h == null)
			{
				h = new Holder();
				map.put(splits[1], h);
			}
			
			if( Integer.parseInt(splits[2]) == 1 )
			{
				h.read1 = Double.parseDouble(splits[9]);
			}
			else if( Integer.parseInt(splits[2]) == 4 )
			{
				h.read4 = Double.parseDouble(splits[9]);
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getTopeOneAtATimeDir() + File.separator + "merged" + File.separator + 
			"pariedGenusMDS1.txt")));
		
		writer.write("key\tread1\tread4\n");
		
		for(String s : map.keySet())
		{
			writer.write( s + "\t" + map.get(s).read1 + "\t"  + map.get(s).read4 + "\n");
		} 
		
		writer.flush();  writer.close();
	}
}
