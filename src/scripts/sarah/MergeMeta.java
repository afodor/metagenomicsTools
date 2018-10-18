package scripts.sarah;

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
					"c:\\sarahScripts\\loggedPlusMeta_" + level + ".txt" )));
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"C:\\sarahScripts\\logged_" + level + ".txt")));
			
			String[] topSplits =reader.readLine().split("\t");
			
			writer.write(topSplits[0]);
			
			writer.write("\tplantSpecies");
			
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
				
				writer.write("\t" + meta );
				
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
				new File("c:\\sarahScripts\\mapping_file_pitcher_plants_corrected.txt")));
				
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s =s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
			{
				throw new Exception("No");
			}
			
			map.put(splits[0], splits[3]);
		}
		
		return map;
	}
}
