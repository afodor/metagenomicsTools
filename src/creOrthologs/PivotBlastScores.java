package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class PivotBlastScores
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> counts = getCounts();
	}
	
	private static String getOrthologKey(String filepath)
	{
		return filepath.substring(filepath.lastIndexOf("_")+1, filepath.indexOf(".txt.gz"));
	}
	
	private static HashMap<String, Integer> getCounts() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader = 
			new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( 
						ConfigReader.getCREOrthologsDir() + File.separator + "blastResults.txt.gz"))));
						
						
		reader.readLine();
		
		int x=0;
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			//System.out.println(s);
			String[] splits = s.split("\t");
			String orthologKey = getOrthologKey(splits[0]);
			
			if( orthologKey.indexOf("@") != -1 || splits[3].indexOf("@") != -1)
				throw new Exception("No");
		
			String key = orthologKey + "@" + splits[3];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate " + key);
			
			String aVal = splits[4].substring(0, splits[4].indexOf("."));
			
			map.put(key, Integer.parseInt(aVal));
			
			x++;
			
			if( x % 10000 == 0 )
				System.out.println("read " + x);
			
		}
		
		reader.close();
		return map;
	}
}
