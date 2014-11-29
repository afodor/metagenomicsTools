package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.AbundOTUClustParser;
import utils.ConfigReader;

public class AbundantOTUToThreeColumn
{	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getChinaDir() +
				File.separator + "forwardReadToSample"));
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		int x=0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put( splits[0], splits[1].substring(2));
			
			x++;
			
			if( x % 1000000==0)
				System.out.println(x);
			
		}
					
		System.out.println("Got map " + map.size());
		
		AbundOTUClustParser.abundantOTUToSparseThreeColumn(ConfigReader.getChinaDir() + File.separator + 
				"abundantOTU" + File.separator +  "chinaForward.clust.gz", 
				ConfigReader.getChinaDir() + File.separator + "abundantOTU" + File.separator + 
					"sparseForwardThreeFileColumn.txt", map);
	}
}
