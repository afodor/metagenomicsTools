package scripts.Erin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MergeMeta
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = keyToMeta();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		String [] levels = { "p", "c","o","f","g" };
		
		for( String level : levels)
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"c:\\Erin\\loggedPlusMeta_" + level + ".txt" )));
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"C:\\Erin\\logged_" + level + ".txt")));
			
			String[] topSplits =reader.readLine().split("\t");
			
			writer.write(topSplits[0]);
			
			writer.write("\tmetaKey\tF1_vs_F2\tpreOrPost");
			
			for( int x=1; x < topSplits.length; x++)
				writer.write("\t" + topSplits[x]);
			
			writer.write("\n");
			
			
			for(String s =reader.readLine(); s != null; s= reader.readLine() )
			{
				String[] splits = s.split("\t");
				
				String meta = map.get(splits[0]);
				
				if( meta == null)
					throw new Exception("No");
				
				writer.write(splits[0]);
				
				writer.write("\t" + meta + "\t" + meta.substring(0, 2));
				
				if( meta.indexOf("pre") != -1)
					writer.write("\tpre");
				else if( meta.indexOf("post") != -1 )
					writer.write("\tpost");
				else throw new Exception("No");
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}
			writer.flush();  writer.close();
		}
	}
	
	private static HashMap<String, String> keyToMeta() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("c:\\Erin\\180112_UNC23_0130_000000000-BH9WC.mapping.txt")));
				
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s =s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
			{
				throw new Exception("No");
			}
			
			map.put(splits[0], splits[4]);
		}
		
		return map;
	}
}
