package scripts.AdenomasRelease;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{
	public static HashMap<String, String> getCaseControl() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getAdenomasReleaseDir() + File.separator + 
			"caseControlTwoColumn.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine() )
		{
			String[] splits = s.split("\t");
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0],splits[1]);
		}
		
		reader.close();
				
		return map;
	}
}
