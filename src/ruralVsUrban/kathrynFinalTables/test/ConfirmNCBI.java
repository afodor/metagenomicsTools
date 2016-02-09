package ruralVsUrban.kathrynFinalTables.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class ConfirmNCBI
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> expectedMap = getExpectedBestVal();
	}
	
	private static HashMap<String, String> getExpectedBestVal() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
						"ncbiRuralVsUrban.txt")));
		
		
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
		}
		return map;
	}
}
