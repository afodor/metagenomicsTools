package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{

	private static HashMap<Integer, String> getMetadataLines() throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"HC Psych Data for Anthony_10.20.15_BlankColsDeleted.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			Integer val = Integer.parseInt(s.split("\t")[0].replace("HC", ""));
			
			if( map.containsKey(val))
				throw new Exception("No");
			
			map.put(val, s);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, String> metaMap = getMetadataLines();
		
	}
	
	
}
