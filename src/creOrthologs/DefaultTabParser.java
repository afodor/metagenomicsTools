package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TabReader;

public class DefaultTabParser
{
	private static final Integer DUPLICATE = -99;
	
	public static HashMap<String, Integer> getFileLineMap(  ) throws Exception
	{
		System.out.println("Reading annotations...");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader( 
				new File(ConfigReader.getCREOrthologsDir() + 
						File.separator + "mbgd_2015-01_default.tab.gz")));
		
		for( int x=0; x< 9; x++)
			reader.readLine();
		

		int lineNumber = 9;
		for(String s= reader.readLine();  s != null; s = reader.readLine())
		{
			
			TabReader tReader =new TabReader(s);
			
			for( int x=0; x < 8; x++)
				tReader.nextToken();
			
			while(tReader.hasMore())
			{
				String next = tReader.nextToken().trim();
				
				if( next.length() >0)
				{
					String key = new StringTokenizer(next, "(").nextToken();
					
					if( map.containsKey(key))
						map.put(key, DUPLICATE);
					else
						map.put(key, lineNumber);
				}
			}
			
			lineNumber++;
			
			if( lineNumber % 1000 == 0 )
				System.out.println(lineNumber);
				
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> map = getFileLineMap();
		System.out.println("Got " + map.size() );
		
		int numDupes = 0;
		
		for( Integer i : map.values())
			if( i == DUPLICATE)
				numDupes++;
		
		System.out.println(numDupes);
	}
}
