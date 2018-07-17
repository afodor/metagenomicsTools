package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeSampleCounts
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> countMap =getSampleCounts();
		
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + 
				"IER Project Metadata.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + 
				"IER Project MetadataPlusCounts.txt")));
		
		writer.write(reader.readLine() + "\tcounts\n");
		
		for(String s= reader.readLine(); s != null; s=reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Integer count = countMap.get(splits[0]);
			
			if(count ==null )
				count =0;
			
			writer.write(s + "\t" + count + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, Integer> getSampleCounts() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader( new File(
		ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + "sampleCounts.txt")));
		
		for(String s = reader.readLine(); s!= null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Integer.parseInt(splits[1]));
		}
		
		reader.close();
		
		return map;
	}
}
