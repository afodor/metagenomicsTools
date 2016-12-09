package scripts.LyteNov2016.mattFiles.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AddExperimentToSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = sampleToExp();
		HashMap<String, String> tissueMap = sampleToTissue();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\lyte_Nov10_2016\\Lyte_seqs_11102016\\mattFiles\\phylumFile.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\temp\\mergedPhyla.txt"));
		
		writer.write(splits[0] + "\tsampleID\treadNumber\texpControl\ttissue");
		
		for( int x=1; x < splits.length; x++ ) 
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			splits =s.split("\t");
			writer.write(splits[0]);
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			String key = sToken.nextToken();
			writer.write("\t" + key + "\t" + sToken.nextToken());
			
			String expControl = map.get(key);
			
			if( expControl == null)
				throw new Exception("No");
			
			writer.write("\t" + expControl  + "\t" + tissueMap.get(key));
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	
	private static HashMap<String, String> sampleToExp() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\lyte_Nov10_2016\\Lyte_seqs_11102016\\mattFiles\\ArgonneMetadata.txt")));
		
		reader.readLine();
		
		HashMap<String, String> map = new HashMap<String,String>();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[3]);
		}
		
		reader.close();
		
		return map;
	}
	

	
	private static HashMap<String, String> sampleToTissue() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\lyte_Nov10_2016\\Lyte_seqs_11102016\\mattFiles\\ArgonneMetadata.txt")));
		
		reader.readLine();
		
		HashMap<String, String> map = new HashMap<String,String>();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
		}
		reader.close();
		
		return map;
	}
}
