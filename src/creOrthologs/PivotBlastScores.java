package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class PivotBlastScores
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> counts = getCounts();
	}
	
	private static HashMap<String, Integer> getCounts() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + "blastResults.txt"	)));
		
		reader.readLine();
		
		int x=0;
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			System.out.println(s);
			String[] splits = s.split("\t");
			
			if( splits[1].indexOf("@") != -1 || splits[2].indexOf("@") != -1)
				throw new Exception("No");
		
			String key = splits[1] + "@" + splits[2];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate " + key);
			
			String aVal = splits[3].substring(0, splits[3].indexOf("."));
			
			map.put(key, Integer.parseInt(aVal));
			
			x++;
			
			if( x % 10000 == 0 )
				System.out.println("read " + x);
			
		}
		
		reader.close();
		return map;
	}
}
