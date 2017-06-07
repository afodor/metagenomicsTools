package scripts.uegp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class ProduceHighestDepthPerSample
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> depthMap = getHighestReadIds();
		
		for(String s : depthMap.keySet())
		{
			System.out.println(s + " " + depthMap.get(s).id + " " + depthMap.get(s).depth);
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getUEGPDir() + File.separator + "shortBREDwMetadata.txt")));
		
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getUEGPDir() + File.separator + "shortBREDwMetadataDotsRemovedOnlyDeepest.txt")));
		
		writer.write(reader.readLine().replaceAll("\\.", "_").replaceAll("\\|", "_").replaceAll("#", "@") + "\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			Holder h = depthMap.get(splits[2]);
			
			if( h.id.equals(splits[0]) )
				writer.write(s + "\n");
		}
		
		writer.flush();writer.close();
		reader.close();
		
	}
	
	private static class Holder
	{
		long depth;
		String id;
	}
	
	// key is sample_ID
	private static HashMap<String, Holder> getHighestReadIds() throws Exception
	{
		HashMap<String, Holder> map = new LinkedHashMap<String,Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getUEGPDir() + File.separator + "shortBREDwMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String key = splits[2];
			
			Long newVal = Long.parseLong(splits[35]);

			Holder h = map.get(key);
			
			if( h == null || h.depth < newVal)
			{
				h = new Holder();
				h.id = splits[0];
				h.depth = newVal;
				map.put(key, h);
			}
		}
		
		reader.close();
		
		return map;
	}
	
}
