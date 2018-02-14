package scripts.ChinaDec2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	
	private static HashMap<String,String> getLineMap() throws Exception
	{
		HashMap<String,String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDecember2017Dir() + File.separator + 
				"metadata_01032018.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			String key = splits[1];
			
			if( map.containsKey(key))
				throw new Exception("No " + key);
			
			map.put(key, s);
		}
		
		reader.close();
		
		return map;
	}
	
	private static String getFirstMetaLine() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDecember2017Dir() + File.separator + 
				"metadata_01032018.txt")));
		
		String returnVal = reader.readLine();
		
		reader.close();
		
		return returnVal;
		
	}
	
	
	public static void main(String[] args) throws Exception
	{
		String firstLine = getFirstMetaLine();
		HashMap<String, String> metaMap = getLineMap();
		

		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++ )
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDecember2017Dir() + File.separator + "tables" + 
			File.separator + 
				"pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getChinaDecember2017Dir() + File.separator + "tables" + 
							File.separator + 
					"pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + "WithFirstChar.txt")));
			
			writer.write("id\tfirstChar\t" +firstLine + "\t" + 
			reader.readLine().replaceAll("\"", "") + "\n");
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				s = s.replaceAll("\"", "");
				String[] splits = s.split("\t");
				
				writer.write(splits[0] + "\t" + splits[0].charAt(0));
				
				String key = new StringTokenizer(splits[0], "_").nextToken();
				
				writer.write("\t" + metaMap.get(key));
				
				if( ! metaMap.containsKey(key))
					throw new Exception("No " + key);
				
				for( int y=1; y < splits.length; y++)
				writer.write("\t" + splits[y]);
				
				writer.write("\n");
			}
			
			writer.flush();  writer.close();
			
			reader.close();
		}
	}
}
